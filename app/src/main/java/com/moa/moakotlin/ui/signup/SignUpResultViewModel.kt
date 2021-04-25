package com.moa.moakotlin.ui.signup

import android.content.Context
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList

class SignUpResultViewModel(navController: NavController) : BaseViewModel(navController){

    fun getMyaroundApt(db : FirebaseFirestore){
        db.collection("Apart").document(User.getInstance().aptCode)
            .get().addOnSuccessListener {
                if(it.exists()){
                    println("존재!!")
                    var list = it.toObject(aptList::class.java)
                    if (list != null) {
                        println(list.aroundApt.get(0))
                        aptList.setInstance(list)

                    }
                }
            }
    }


}