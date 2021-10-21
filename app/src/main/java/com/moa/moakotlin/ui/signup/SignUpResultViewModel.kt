package com.moa.moakotlin.ui.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpResultViewModel() : ViewModel(){

    fun getMyaroundApt(db : FirebaseFirestore){

        db.collection("Apart").document(User.getInstance().aptCode)
            .get().addOnSuccessListener {
                if(it.exists()){

                    var list = it.toObject(aptList::class.java)
                    if (list != null) {
                        println(list.aroundApt.get(0))
                        aptList.setInstance(list)
                    }
                }
            }
    }

    fun getMyUser(){
        var repository = UserRepository()
        CoroutineScope(Dispatchers.Main).launch {
          var user = FirebaseAuth.getInstance().currentUser?.uid?.let { repository.getUserInfo(it) }
            User.getInstance().uid = user!!.uid
            User.getInstance().isAgreeMarketing = user!!.isAgreeMarketing
            User.getInstance().profileImage = user!!.profileImage
            User.getInstance().isMan = user!!.isMan
            User.getInstance().nickName = user!!.nickName
            User.getInstance().address = user!!.address
            User.getInstance().aptCode = user!!.aptCode
            User.getInstance().aptName = user!!.aptName

        }

    }


}