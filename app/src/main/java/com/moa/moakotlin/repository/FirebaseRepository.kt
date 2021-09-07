package com.moa.moakotlin.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository<T> {
    var result : T = TODO()
  suspend  fun getDocument(db : Task<DocumentSnapshot>) {

        db.addOnSuccessListener {

        }.addOnFailureListener {

        }
    }

  suspend  fun writeDocument(db : Task<DocumentSnapshot>) : Boolean {
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

   suspend fun getDocumentList(db : Task<DocumentSnapshot>){

        db.addOnSuccessListener {

        }.addOnFailureListener {

        }
    }

}