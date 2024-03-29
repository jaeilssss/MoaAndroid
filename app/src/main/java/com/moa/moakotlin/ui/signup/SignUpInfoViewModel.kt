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
import com.moa.moakotlin.repository.apt.AptRepository
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

    suspend fun getMyAroundNeighborhood(list : ArrayList<String>) : ArrayList<String>{
        var repository = AptRepository()
        var aptNameList = ArrayList<String>()
        for(i in 0 until list.size){
            var aptName = repository.getMyAroundNeighborhood(list.get(i))
            aptNameList.add(aptName)
        }
        return aptNameList
    }

    fun setUserInstance() {
        User.getInstance().aptName = aptName.value.toString()

        User.getInstance().name = name.value.toString()

        User.getInstance().nickName = nickName.value.toString()

        User.getInstance().isMan = gender.value?.toInt() ==1 || gender.value?.toInt() ==3

        User.getInstance().birthday = birthDay.value!!

        User.getInstance().address = " ${User.getInstance().address} ${dong.value.toString()}동 ${hosoo.value.toString()}호"

        if(User.getInstance().isMan==true){
            User.getInstance().profileImage = "https://firebasestorage.googleapis.com/v0/b/moakr-8c0ab.appspot.com/o/MoAImages%2FMAN_DEFAULT.png?alt=media&token=18861244-4379-42b5-a6d6-37c1e77f5266"
        }else{
            User.getInstance().profileImage = "https://firebasestorage.googleapis.com/v0/b/moakr-8c0ab.appspot.com/o/MoAImages%2FWOMEN_DFAULT.png?alt=media&token=8ec5f0be-b61e-4355-b1d2-0763fd867385"
        }
    }

}