package com.moa.moakotlin.repository.concierge

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.main
import kotlinx.coroutines.tasks.await

class NeederRepository {

    suspend fun submit(needed : Needer) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var result =false
        db.collection("Needer")
                .add(needed).addOnSuccessListener{
                    result = true
                }.await()
        return result
    }
    suspend fun modify(needer : Needer) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var result = false
        needer.documentID?.let {
            db.collection("Needer")
                .document(it).set(needer).addOnSuccessListener{
                            result = true
                    }.await()
        }
        return result
    }
    suspend fun initSetList(mainCategory : String) :ArrayList<Needer>{
        println("${aptList.getInstance().aroundApt.get(0)}")
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Needer>()
        db.collection("Needer").orderBy("timeStamp",Query.Direction.DESCENDING).whereEqualTo("mainCategory",mainCategory)
                .whereIn("aptCode",aptList.getInstance().aroundApt)
                .limit(5).get().addOnSuccessListener {
                    for (document in it.documents) {
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
        db.collection("Needer").orderBy("timeStamp", Query.Direction.DESCENDING).limit(50).whereEqualTo("mainCategory",mainCategory)
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