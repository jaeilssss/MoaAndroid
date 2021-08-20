package com.moa.moakotlin.ui.mypage

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.concierge.NeederRepository
import com.moa.moakotlin.repository.login.LoginRepository
import com.moa.moakotlin.repository.user.UserRepository
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
        isChecked = false
    }
    fun sendMessage(){
        unBoxingBundle()
        loginRepository  = LoginRepository(activity)
        loginRepository.sendMessage(phoneNumberString)
//    phoneNumber.get()?.let { loginRepository.sendMessage(it) }
    }

    suspend  fun checkCertificationMessage() : Boolean{
        var repository = UserRepository()
        if(loginRepository.storedVerificationId==null){
            return isChecked
        }else{
            FirebaseAuth.getInstance().signOut()
            CoroutineScope(Dispatchers.Default).async {
                isChecked =  loginRepository.signInWithPhoneAuthCredential(code.value!!)
                User.getInstance().phoneNumber  = phoneNumber?.value!!
                User.getInstance().phoneUid = FirebaseAuth.getInstance().uid.toString()
                repository.modify(User.getInstance())
            }.await()
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
        return phoneNumber.value?.length==11 && phoneNumber.value?.equals(User.getInstance().phoneNumber)!!.not()
    }

    suspend fun checkAlreadyPhone() : Boolean{
        var repository = UserRepository()

        return repository.checkAlreadyPhone(phoneNumber?.value!!)
    }

    fun settingConciergeData(){
        var neederRepository = NeederRepository()
        var helperRepository = HelperRepository()
        // 리뷰 있어야함 시발 ..


        neederRepository.modifyConciergeList()
        helperRepository.modifyConciergeList()

    }


}