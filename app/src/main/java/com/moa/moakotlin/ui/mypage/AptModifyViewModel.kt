package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.user.UserRepository

class AptModifyViewModel : ViewModel() {
    val aptName = MutableLiveData<String>("")
    val aptCode = MutableLiveData<String>("")
    var address = ""
    val dong = MutableLiveData<String>("")
    val hosoo = MutableLiveData<String>("")

    fun check() : Boolean{
        return aptName.value?.length!! >0 &&
                aptCode.value?.length!!>0 &&
                address.isNotEmpty() &&
                dong.value?.length!!>0 &&
                hosoo.value?.length!!>0
    }


    suspend  fun modifyApt() : Boolean{
        var repository = UserRepository()
        address = "${address} ${dong.value}동 ${hosoo.value}호"
        if(repository.modifyApt(aptName.value!!,aptCode.value!!, address)){
            User.getInstance().aptName = aptName.value!!
            User.getInstance().aptCode = aptCode.value!!
            User.getInstance().address = address
            User.getInstance().certificationStatus = "미인증"
            return true
        }else{
            return false
        }
    }
}