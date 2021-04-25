package com.moa.moakotlin.ui.signup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.firebase.phoneCertificationApi
import com.moa.moakotlin.repository.login.LoginRepository
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PhoneCertificationViewModel(navController: NavController,var context: Context) : BaseViewModel(navController){
    val code = ObservableField<String>("")
    lateinit var bundle: Bundle
     var db = FirebaseFirestore.getInstance()
    lateinit var phoneCertificationApi: phoneCertificationApi
     var phoneNumber =String()
    var result = "fail"
    lateinit var activity: FragmentActivity
 private var inputPhoneNumber =String()
    var isChecked = false
    lateinit var repository : LoginRepository
    fun init(activity : FragmentActivity){
        this.activity = activity
         unBoxingBundle()
        repository = LoginRepository(activity)
        repository.sendMessage(phoneNumber)
    }
    suspend fun check() :Boolean{
        code.get()?.let {
            CoroutineScope(Dispatchers.Default).async {
                isChecked =  repository.signInWithPhoneAuthCredential(code.get()!!)
            }.await()
        }
        return isChecked
    }
    suspend fun next() : Boolean {
        isChecked = true
        var user: User? = null
        if (isChecked == true) {
            var uid = FirebaseAuth.getInstance().currentUser.uid

            var userRepository = UserRepository()
            CoroutineScope(Dispatchers.Default).async {
                user = userRepository.getUserInfo(uid)
            }.await()

            if (user != null) {
                User.setInstance(user!!)
                return true
            }else{
                return false
            }

        }
        return false
    }
    fun settingPhoneNumber(inputData : String){
        phoneNumber= phoneNumber.plus(inputData.substring(1,3))
        phoneNumber = phoneNumber.plus("-")
        phoneNumber =phoneNumber.plus(inputData.substring(3,7))
        phoneNumber = phoneNumber.plus("-")
        phoneNumber = phoneNumber.plus(inputData.substring(7,11))
    }
    fun unBoxingBundle(){
        inputPhoneNumber = bundle.get("phoneNumber") as String
        phoneNumber = phoneNumber.plus("+82 ")
        settingPhoneNumber(inputPhoneNumber)
    }

}