package com.moa.moakotlin.ui.mypage

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.mypage.MyPageRepository
import com.moa.moakotlin.repository.user.UserRepository
import com.moa.moakotlin.repository.voice.VoiceRepository
import kotlinx.coroutines.*

class UserProfileModifyViewModel : ViewModel() {
 var introduction =MutableLiveData<String>(User.getInstance().introduction)
    var nickname =MutableLiveData<String>(User.getInstance().nickName)
    var image = ""
    var check = false

    suspend fun submit() : Boolean{
        var check = false
        var repository = UserRepository()
        var user = User()
        if(image.isNotEmpty()){

            var path = repository.upload(image)
             user = setUser(path)
          check = repository.modify(user)

        }else{
             user = setUser(User.getInstance().profileImage)
            check = repository.modify(user)
        }

        if(check){
           User.getInstance().profileImage = user.profileImage
            User.getInstance().nickName = user.nickName
            User.getInstance().introduction = user.introduction
        }

        return check
    }

    private fun setUser(path : String) : User{
        var tempUser = User()

        tempUser.introduction = introduction.value!!
        tempUser.nickName = nickname.value!!
        tempUser.name = User.getInstance().name
        tempUser.profileImage = path
        tempUser.aptName = User.getInstance().aptName
        tempUser.aptCode = User.getInstance().aptCode
        tempUser.phoneNumber = User.getInstance().phoneNumber
        tempUser.address = User.getInstance().address
        tempUser.birthday = User.getInstance().birthday
        tempUser.isMan = User.getInstance().isMan
        tempUser.uid = User.getInstance().uid
        tempUser.signUpDate = User.getInstance().signUpDate
        tempUser.certificationStatus = User.getInstance().certificationStatus
        tempUser.isAgreeChattingAlarm = User.getInstance().isAgreeChattingAlarm
        tempUser.isAgreeEventAlarm = User.getInstance().isAgreeEventAlarm
        tempUser.isAgreeMarketing = User.getInstance().isAgreeMarketing
        tempUser.phoneUid = User.getInstance().phoneUid
        return tempUser
    }


    fun checkable() : Boolean{

        if(nickname.value.equals(User.getInstance().nickName)){
            return true
        }else {
            return check
        }
    }

    suspend fun checkNickName() : Boolean{
        var repository = UserRepository()
        check =  repository.checkNickName(nickname.value!!)
        return check
    }
}