package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAccountSettingViewModel : ViewModel() {
    var phoneNumber = MutableLiveData<String>(User.getInstance().phoneNumber)


    fun logout(){
        FirebaseAuth.getInstance().signOut()
        User.getInstance().pushToken = ""
        var repository = UserRepository()
        CoroutineScope(Dispatchers.Main).launch {
            repository.modify(User.getInstance())
            FirebaseMessaging.getInstance().unsubscribeFromTopic(User.getInstance().aptCode)
            User.deleteUser()
        }
    }
}