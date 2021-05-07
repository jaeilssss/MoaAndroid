package com.moa.moakotlin.ui.concierge.helper

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.Sitter
import com.moa.moakotlin.data.User

class HelperReadViewModel() : ViewModel() {
    var nickname  =  ObservableField<String>("")
    var type = ObservableField<String>("")

fun initViewModel(sitter : Sitter){
getWriterInfo(sitter.uid)
}

    fun getWriterInfo(uid: String){
        var db = FirebaseFirestore.getInstance()

        db.collection("User").document(uid)
            .get().addOnSuccessListener {
                var writer  = it.toObject(User::class.java)
                    println("닉네임 : ${writer?.nickName}")
                nickname.set(writer?.nickName)
            }
    }
}