package com.moa.moakotlin.ui.signup

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SignUpInfoViewModel() : ViewModel(){
    var name = MutableLiveData<String>("")
    var birthDay = MutableLiveData<String>("")
    var gender = MutableLiveData<String>("")
    lateinit var bundle : Bundle
    var dong = MutableLiveData<String>("")
    var hosoo = MutableLiveData<String>("")
    var nickName = MutableLiveData<String>("")
    lateinit var db : FirebaseFirestore
    var nickCheck = ObservableField<Boolean>(false)
    var aptName = MutableLiveData<String>("")
    var latestNickName = ""
    suspend fun checkNickName() :Boolean {
        var result : Boolean
        if(nickName.value?.length==0){
            return false
        }else{
            var repository = UserRepository()
             result = repository.checkNickName(nickName.value!!)
            if(result){
                nickCheck.set(true)
            }else{
                nickCheck.set(false)
            }
        }
        return result
    }

    fun checkInfo() : Boolean{

        return checkNameLength() && checkGenderLength() && checkBirthDayLength() && nickCheck.get() == true &&
                checkDongLength() && checkHosooLength() && checkAptNameLength()
    }
    fun checkAptNameLength() : Boolean{
        return aptName.value?.length!!>0
    }
    fun checkNameLength() :Boolean{
        return name.value?.length!!>0
    }
    fun checkGenderLength(): Boolean{
        return gender.value.equals("1") || gender.value.equals("2") ||
                gender.value.equals("3") || gender.value.equals("4")
    }

    fun checkBirthDayLength() : Boolean{
        return birthDay.value?.length==6
    }


    fun checkDongLength() : Boolean {
        return dong.value?.length!!>0
    }
    fun checkHosooLength() : Boolean{
        return hosoo.value?.length!!>0
    }

    fun setLatestNickname(){
        latestNickName = nickName?.value.toString()
    }
    fun checkLatestNickname() : Boolean{
        return latestNickName == nickName.value
    }

}