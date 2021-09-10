package com.moa.moakotlin.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirebaseRepository<T> {

  suspend inline fun <reified T> getDocument(db : Task<DocumentSnapshot>) : ArrayList<T> {
        var list = ArrayList<T>()
        db.addOnSuccessListener {
                if(it.exists()){
                    var data = it.toObject(T::class.java)
                    if (data != null) {
                        list.add(data)
                    }
                }
        }.addOnFailureListener {

        }.await()
      return list
    }

  suspend fun writeDocument(db :  Task<DocumentReference>) : Boolean {
        var check  = false
        db.addOnSuccessListener {
            check = true
        }.addOnFailureListener {
            check  = false
        }.await()
        return check
    }


   suspend fun deleteDocument(db : Task<DocumentSnapshot>) : Boolean{
    var check = false
        db.addOnSuccessListener {
            check = true
        }.addOnFailureListener {
            check = false
        }.await()
       return check
    }

   suspend inline fun <reified T> getDocumentList(db :  Task<QuerySnapshot>) : ArrayList<T>{

        var list = ArrayList<T>()
        db.addOnSuccessListener {

            for(document in it.documents){
                var data = document.toObject(T::class.java)
                if (data != null) {
                    list.add(data)
                }
            }


        }.addOnFailureListener {

        }.await()
       return list
    }




}