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
                .add(needed).addOnSuccessListener {
                    result = true
                }.await()

        return result

    }
    suspend fun modify(needed : Needer) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var result = false
        needed.documentID?.let {

            db.collection("Needer")
                .document(it).set(needed).addOnSuccessListener{
                            result = true
                    }.await()
        }
        return result
    }
    suspend fun initSetList(mainCaregory : String) :ArrayList<Needer>{
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Needer>()
        db.collection("Needer").orderBy("timeStamp",Query.Direction.DESCENDING).whereEqualTo("mainCategory", mainCaregory)
                .whereEqualTo("aptCode",aptList.getInstance().aroundApt)
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
    suspend fun getList() : ArrayList<Needer> {
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Needer>()
        db.collection("Needer").orderBy("timeStamp", Query.Direction.DESCENDING).limit(50)
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