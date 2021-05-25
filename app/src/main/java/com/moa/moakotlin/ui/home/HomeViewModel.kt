package com.moa.moakotlin.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.base.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.logging.Handler

class HomeViewModel() : ViewModel() {

    private lateinit var functions: FirebaseFunctions

    fun init(){
//        deleteAtPath("test")

        var db = FirebaseFirestore.getInstance()


        db.collection("test").whereArrayContains("array","q").get().addOnSuccessListener {
            if(it.isEmpty){
                System.out.println("없음!!")
            }else{
                System.out.println("있음!")
            }
        }
    }
    fun goToKid() {

    }

    fun goToShare() {

    }

    fun deleteAtPath(path: String) {
        functions = Firebase.functions("asia-northeast3")
        val deleteFn = functions.getHttpsCallable("recursiveDelete")
        deleteFn.call(hashMapOf("path" to path))
                .addOnSuccessListener {
                    // Delete Success
                    // ...

                    System.out.println("성공!!!")
                }
                .addOnFailureListener {
                    // Delete Failed
                    // ...
                    System.out.println("실패..!!!")
                }
    }
}