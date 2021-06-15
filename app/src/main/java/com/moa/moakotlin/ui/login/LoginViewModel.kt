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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel(){
    var phoneNumber = ObservableField<String>("")
    var code = MutableLiveData<String>("")
    lateinit var activity :FragmentActivity
    lateinit var  loginRepository :LoginRepository
    var isChecked  = false
    lateinit var phoneNumberString : String
    fun login(){
            var user = User.getInstance()
            user.phoneNumber=phoneNumber.get().toString()

            sendMessage()
        }
    fun init(activity : FragmentActivity){
        this.activity = activity

    }
 fun sendMessage(){

    var loginRepository  = LoginRepository(activity)
    phoneNumber.get()?.let { loginRepository.sendMessage(it) }
}


    suspend private fun checkCertificationMessage() : Boolean{

        if(loginRepository.storedVerificationId==null){
            return isChecked
        }else{

                CoroutineScope(Dispatchers.Default).async {
                    isChecked =  loginRepository.signInWithPhoneAuthCredential(code.value!!)
                }.await()
        }

        if(isChecked){
            User.getInstance().phoneNumber = phoneNumber.get().toString()
        }
        return isChecked

}
    fun settingPhoneNumber(inputData : String){
        phoneNumberString= phoneNumberString.plus(inputData.substring(1,3))
        phoneNumberString = phoneNumberString.plus("-")
        phoneNumberString =phoneNumberString.plus(inputData.substring(3,7))
        phoneNumberString = phoneNumberString.plus("-")
        phoneNumberString = phoneNumberString.plus(inputData.substring(7,11))
    }
    fun unBoxingBundle(){
        phoneNumberString = phoneNumber.get().toString()
        phoneNumberString = phoneNumberString.plus("+82 ")
        settingPhoneNumber(phoneNumberString)
    }
}