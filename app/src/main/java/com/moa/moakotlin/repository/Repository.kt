package com.moa.moakotlin.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class Repository<T> {
    var result : T = TODO()
    fun getDocument(db : Task<DocumentSnapshot>) {

        db.addOnSuccessListener {

        }.addOnFailureListener {

        }
    }

    fun writeDocument(db : Task<DocumentSnapshot>) {
        db.addOnSuccessListener {

        }.addOnFailureListener {

        }
    }

    fun deleteDocument(db : Task<DocumentSnapshot>){

        db
            .addOnSuccessListener {

        }.addOnFailureListener {

        }
    }

    fun getDocumentList(db : Task<DocumentSnapshot>){

        db.addOnSuccessListener {

        }.addOnFailureListener {

        }
    }

}