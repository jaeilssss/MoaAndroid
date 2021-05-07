package com.moa.moakotlin.ui.certification

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.*
import java.util.*

class CertificationSkipViewModel(navController: NavController,var context: Context) : BaseViewModel(navController){

lateinit var bundle: Bundle

    suspend fun signup() : Boolean{
        var repository = UserRepository()

        var result  = repository.signUpUser(User.getInstance())

        return result
    }
   fun next() {

       var user = User.getInstance()
       user.signUpDate = Timestamp.now()
       var db = FirebaseFirestore.getInstance()
               db.collection("User").document(FirebaseAuth.getInstance().uid!!)
                   .set(user)
                   .addOnSuccessListener {
                       Toast.makeText(context,"가입에 완료되었습니다",Toast.LENGTH_SHORT).show()
                       navController.navigate(R.id.action_certificationSkipFragment_to_signUpResultFragment)
                   }
    }
}