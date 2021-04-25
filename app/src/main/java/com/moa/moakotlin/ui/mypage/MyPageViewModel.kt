package com.moa.moakotlin.ui.mypage

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.login.LoginRepository

class MyPageViewModel:ViewModel() {

    fun logOut(activity : FragmentActivity){
        var api = LoginRepository(activity)
        api.logOut()
        User.deleteUser()
    }
}