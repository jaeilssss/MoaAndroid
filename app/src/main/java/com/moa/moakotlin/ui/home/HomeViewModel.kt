package com.moa.moakotlin.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.PushDTO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.logging.Handler

class HomeViewModel() : ViewModel() {

    private lateinit var functions: FirebaseFunctions

    fun init(){
//        deleteAtPath("test")
//        registerPushToken()
        var push = FcmPush()

        var token = "9PXLVgw8HvdGXt3ZRaMtMqCovDK2"
        push.sendMessage(token,"test!!","하이 재일99")

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

    private fun registerPushToken() {
        //v17.0.0 이전까지는
        ////var pushToken = FirebaseInstanceId.getInstance().token
        //v17.0.1 이후부터는 onTokenRefresh()-depriciated
        var pushToken: String? = null
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var map = mutableMapOf<String, Any>()
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            pushToken = instanceIdResult.token
            map["pushtoken"] = pushToken!!
            FirebaseFirestore.getInstance().collection("pushtokens").document(uid!!).set(map)
        }
    }


}