package com.moa.moakotlin.repository.concierge

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import kotlinx.coroutines.tasks.await

class NeederRepository {

    suspend fun submit(mainCategory: String,needer : Needer) : Needer{
        var db = FirebaseFirestore.getInstance()
        var result =false
        db.collection("Needer")
                .document(mainCategory)
                .collection(mainCategory)
                .add(needer).addOnSuccessListener{
                needer.documentID = it.id
                }.await()
        return needer
    }
    suspend fun modify(mainCategory: String,needer : Needer) : Needer{
        var db = FirebaseFirestore.getInstance()
        db.collection("Needer").document(mainCategory)
            .collection(mainCategory)
            .document(needer.documentID!!)
            .set(needer).addOnCompleteListener {
            }.await()
        return needer
    }
    suspend  fun initSetList(mainCategory: String) : ArrayList<Needer>{
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Needer>()
        db.collection("Needer").document(mainCategory)
            .collection(mainCategory)
            .whereArrayContains("aroundApt", User.getInstance().aptCode)
            .orderBy("timeStamp",Query.Direction.DESCENDING)
            .limit(5).get().addOnSuccessListener {
                for(document in it.documents){
                    var data = document.toObject(Needer::class.java)
                    data?.documentID = document.id
                    if (data != null) {
                        result.add(data)
                    }
                }
            }.await()
        return result

    }

    suspend fun getList(mainCategory: String) : ArrayList<Needer> {
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Needer>()
        db.collection("Needer")
                .document(mainCategory)
                .collection(mainCategory)
                .whereArrayContains("aroundApt", User.getInstance().aptCode)
                .orderBy("timeStamp", Query.Direction.DESCENDING).limit(50)
                .get().addOnSuccessListener {
                    for(document in it.documents){
                        var data = document.toObject(Needer::class.java)
                        data?.documentID = document.id
                        if (data != null) {
                            result.add(data)
                        }
                    }
                }.await()
        return result
    }


}