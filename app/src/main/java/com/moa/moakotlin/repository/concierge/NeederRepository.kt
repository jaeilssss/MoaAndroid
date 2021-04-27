package com.moa.moakotlin.repository.concierge

import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.Needer
import kotlinx.coroutines.tasks.await

class NeederRepository {

    suspend fun submit(needed : Needer) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var result =false
        db.collection("Helper")
                .add(needed).addOnSuccessListener {
                    result = true
                }.await()

        return result

    }
    suspend fun modify(needed : Needer) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var result = false
        needed.documentID?.let {

            db.collection("Helper")
                .document(it).set(needed).addOnSuccessListener{
                            result = true
                    }.await()
        }
        return result
    }




}