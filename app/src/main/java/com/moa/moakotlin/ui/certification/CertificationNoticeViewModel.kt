package com.moa.moakotlin.ui.certification

import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.user.UserRepository

class CertificationNoticeViewModel() : ViewModel(){


   suspend fun signUp(user :User) : Boolean{
        var repository = UserRepository()

      return  repository.signUpUser(user)

    }




}



