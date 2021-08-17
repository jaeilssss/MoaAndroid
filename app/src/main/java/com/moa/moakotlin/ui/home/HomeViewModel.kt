package com.moa.moakotlin.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.Banner
import com.moa.moakotlin.data.Magazine
import com.moa.moakotlin.data.PushDTO
import com.moa.moakotlin.data.PushMessage
import com.moa.moakotlin.repository.banner.BannerRepository
import com.moa.moakotlin.repository.push.FcmRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.logging.Handler

class HomeViewModel() : ViewModel() {

    private lateinit var functions: FirebaseFunctions

      var magazineList = MutableLiveData<ArrayList<Magazine>>()

    var homeBannerList = MutableLiveData<ArrayList<Banner>>()
    fun init(){
//        deleteAtPath("test123/sss/test456")
//        registerPushToken()
//        var push = FcmPush()
//
//        var token = "9PXLVgw8HvdGXt3ZRaMtMqCovDK2"
//        push.sendMessage(token,"test!!","하이 재일99")


//        var pushRepository = FcmRepository()
//        var token  = "ezguf8fRReS_QUofFuI-DL:APA91bEtIYVddI-SEu2XTGndm4HYCKgJDd81oabRvy18AgVFpiImLJBVN5eJXvt-wc_KIUAWwNRLAov3nYO2LW8FKb5bN7AwAWXATpMM_vmtLcumI4Vba0ztNVKJo0XGuvZqmwJfoE2S"
//        var message = PushMessage("Moa","당신의 게시글을 좋아요 눌렀습니다!",token)
//        pushRepository.sendPushMessageTest(message)
//
//        FirebaseMessaging.getInstance().subscribeToTopic("test")

    }
    fun goToKid() {

    }

    fun goToShare() {

    }

    fun deleteAtPath(path: String) {
        functions = Firebase.functions("asia-northeast3")
        val deleteFn = functions.getHttpsCallable("recursiveDelete")
        deleteFn.call(hashMapOf("path" to path))
                .addOnSuccessListener {
                    // Delete Success
                    // ...
                    System.out.println("성공!!!")
                }
                .addOnFailureListener {
                    // Delete Failed
                    // ...
                    System.out.println("실패..!!!")
                }
    }
     fun registerPushToken() {
        //v17.0.0 이전까지는
        ////var pushToken = FirebaseInstanceId.getInstance().token
        //v17.0.1 이후부터는 onTokenRefresh()-depriciated
        var pushToken: String? = null

        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var map = mutableMapOf<String, Any>()
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            pushToken = instanceIdResult.token
            println("dddw3232")
            println(pushToken)
            map["pushtoken"] = pushToken!!
            FirebaseFirestore.getInstance().collection("pushtokens").document(uid!!).set(map)
        }
    }


   suspend fun getMoaMagazine(){
        var repository = BannerRepository()

       var list= repository.getMoaMagazine()

       magazineList.value = list

    }

    suspend fun getHomeBanner(){
        var repository = BannerRepository()

        var list = repository.getHomeBanner()

        homeBannerList.value = list
    }




}