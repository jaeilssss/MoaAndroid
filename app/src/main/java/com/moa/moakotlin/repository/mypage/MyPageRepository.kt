package com.moa.moakotlin.repository.mypage

import android.net.Uri
import android.os.Bundle
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.moa.moakotlin.R
import com.moa.moakotlin.data.*
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream
import java.lang.Exception

class MyPageRepository {
    var db = FirebaseFirestore.getInstance()

    suspend fun modifyProfile(content : String,nickname:String,image : String) : Boolean{
        var result = false
      return try{
            db.collection("User").document(User.getInstance().uid)
                    .update(
                            "introduction",content,
                            "nickname",nickname,
                        "profileImage",image
                    ).addOnSuccessListener {
                        println("good!!!")
                        result = true
                    }.addOnFailureListener {
                        result = false
                    }.await()
          result
        }catch (e : Exception){
            result = false
          result
        }
    }

    suspend fun uploadImage(path : String) : String{
        var result = ""
        var storageRef = FirebaseStorage.getInstance().getReference()

        var file = Uri.fromFile(File(path))

        var inputstream = FileInputStream(File(path))

        val riversRef = storageRef.child("user/" + file.lastPathSegment)

        val uploadTask = riversRef.putStream(inputstream)

        uploadTask.continueWithTask { riversRef.downloadUrl }.addOnCompleteListener { task ->

         result = task.result.toString()
        }.await()
        return result
    }

    suspend fun getAppQuestion() : ArrayList<AppQuestion>{
        var db  = FirebaseFirestore.getInstance()
        var list = ArrayList<AppQuestion>()

        db.collection("MyPage")
            .document("MyPage")
            .collection("Question")
                .orderBy("timeStamp",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for(document in it.documents){
                    var appQuestion  = document.toObject(AppQuestion::class.java)
                    if(appQuestion!=null){
                        list.add(appQuestion)
                    }
                }
            }.await()
        return list
    }

    suspend fun getAppNotice() : ArrayList<Notice>{
        var db  = FirebaseFirestore.getInstance()
        var list = ArrayList<Notice>()

        db.collection("MyPage")
                .document("MyPage")
                .collection("Notice")
                .orderBy("timeStamp",Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    for(document in it.documents){
                        var notice  = document.toObject(Notice::class.java)
                        if(notice!=null){
                            list.add(notice)
                        }
                    }
                }.await()
        return list
    }
}