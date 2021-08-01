package com.moa.moakotlin

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.user.UserRepository

class LoadingViewModel : ViewModel() {

    suspend fun initApp(uid : String,activity : Activity) : Boolean {

        var repository = UserRepository()

        var user = repository.getMyUserInfo(uid)
        User.setInstance(user!!)
        if(user==null){
            return false
        }
        if(user!=null){
            var aptList =  repository.getaroundApt(user.aptCode)
            if(aptList!=null){
                return true
            }
            // aptList 가 null 일 수도 있지 않을까?? 고민해보자
        }else{
            return false
        }
        return false
    }
}