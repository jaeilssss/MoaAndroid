package com.moa.moakotlin.ui.login

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.login.LoginRepository
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.*

class LoginViewModel() : ViewModel(){
    var phoneNumber = MutableLiveData<String>("")
    var code = MutableLiveData<String>("")
    lateinit var activity :FragmentActivity
    lateinit var  loginRepository :LoginRepository
    var isChecked  = false
    lateinit var phoneNumberString : String
    fun login(){
            var user = User.getInstance()
            user.phoneNumber=phoneNumber.value.toString()

            sendMessage()
        }
    fun init(activity : FragmentActivity){
        this.activity = activity

    }
 fun sendMessage(){
     unBoxingBundle()
     loginRepository  = LoginRepository(activity)
     loginRepository.sendMessage(phoneNumberString)

}

suspend fun getUserInfo(uid : String) : Boolean{
    var repository = UserRepository()
    return repository.getMyUserInfo(uid)!=null
}

    suspend  fun checkCertificationMessage() : Boolean{

        if(loginRepository.storedVerificationId==null){

            return isChecked
        }else{
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                isChecked = loginRepository.signInWithPhoneAuthCredential(code.value!!)
            }
        }

        if(isChecked){
            User.getInstance().phoneNumber = phoneNumber.value.toString()
        }else{

        }
        return isChecked

}
    fun settingPhoneNumber(inputData : String){
        phoneNumberString = String()
        phoneNumberString = phoneNumberString.plus("+82 ")
        phoneNumberString= phoneNumberString.plus(inputData.substring(1,3))
//        phoneNumberString = phoneNumberString.plus("-")
        phoneNumberString = phoneNumberString.plus(inputData.substring(3,7))
//        phoneNumberString = phoneNumberString.plus("-")
        phoneNumberString = phoneNumberString.plus(inputData.substring(7,11))

    }
    fun unBoxingBundle(){
//        phoneNumberString = phoneNumber.get().toString()
//        phoneNumberString = phoneNumberString.plus("+82 ")
        phoneNumber.value?.let { settingPhoneNumber(it) }
    }


    fun checkPhoneNumber() : Boolean{
        return phoneNumber.value?.length==11
    }
}