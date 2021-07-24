package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.User

class MyAccountSettingViewModel : ViewModel() {
    var phoneNumber = MutableLiveData<String>(User.getInstance().phoneNumber)


    fun logout(){
        FirebaseAuth.getInstance().signOut()
    }
}