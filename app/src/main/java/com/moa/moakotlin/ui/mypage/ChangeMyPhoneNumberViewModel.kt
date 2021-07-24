package com.moa.moakotlin.ui.mypage

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.concierge.NeederRepository
import com.moa.moakotlin.repository.login.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class ChangeMyPhoneNumberViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var phoneNumber = MutableLiveData<String>("")
    var code = MutableLiveData<String>("")
    lateinit var activity : FragmentActivity
    lateinit var  loginRepository : LoginRepository
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
//    phoneNumber.get()?.let { loginRepository.sendMessage(it) }
    }

    suspend  fun checkCertificationMessage() : Boolean{

        if(loginRepository.storedVerificationId==null){
            return isChecked
        }else{

            CoroutineScope(Dispatchers.Default).async {
                isChecked =  loginRepository.signInWithPhoneAuthCredential(code.value!!)
            }.await()
        }

        if(isChecked){
            User.getInstance().phoneNumber = phoneNumber.value.toString()
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

        println(phoneNumberString)
    }
    fun unBoxingBundle(){
//        phoneNumberString = phoneNumber.get().toString()
//        phoneNumberString = phoneNumberString.plus("+82 ")
        phoneNumber.value?.let { settingPhoneNumber(it) }
    }


    fun checkPhoneNumber() : Boolean{
        return phoneNumber.value?.length==11
    }


    fun settingConciergeData(){
        var neederRepository = NeederRepository()
        var helperRepository = HelperRepository()

        neederRepository.modifyConciergeList()
        helperRepository.modifyConciergeList()

    }


}