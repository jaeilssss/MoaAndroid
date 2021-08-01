package com.moa.moakotlin.ui.certification

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.user.UserRepository

class CertificationNoticeViewModel() : ViewModel(){


   suspend fun signUp() : Boolean{
        var repository = UserRepository()
       User.getInstance().phoneUid = FirebaseAuth.getInstance().currentUser?.uid!!
       User.getInstance().certificationStatus = "미인증"
      var uid= repository.signUpUser(User.getInstance())
       if(uid !=null){
           User.getInstance().uid =uid
           return true
       }else{
           return false
       }
    }




}



