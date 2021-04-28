package com.moa.moakotlin.repository.concierge

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import kotlinx.coroutines.tasks.await

class HelperRepository {

    suspend fun submit(helper : Helper) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var result =false
        db.collection("Helper")
                .add(helper).addOnSuccessListener {
                    result = true
                }.await()

        return result

    }
    suspend fun modify(helper : Helper) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var result = false
        helper.documentID?.let {

            db.collection("Helper")
                    .document(it).set(helper).addOnSuccessListener{
                        result = true
                    }.await()
        }
        return result
    }
    suspend fun initSetList(mainCaregory : String) :ArrayList<Helper>{
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Helper>()
        db.collection("Helper").orderBy("timeStamp",Query.Direction.DESCENDING).whereEqualTo("mainCategory", mainCaregory)
                .limit(5).get().addOnSuccessListener {
                    for(document in it.documents){
                        var data = document.toObject(Helper::class.java)

                        data?.documentID = document.id
                        if (data != null) {
                            result.add(data)
                        }
                    }
                }.await()
        return result
    }
    suspend fun getList() : ArrayList<Helper> {
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Helper>()
        db.collection("Helper").orderBy("timeStamp",Query.Direction.DESCENDING).limit(50)
                .get().addOnSuccessListener {
                    for(document in it.documents){
                        var data = document.toObject(Helper::class.java)
                        data?.documentID = document.id
                        if (data != null) {
                            result.add(data)
                        }
                    }
                }.await()
        return result
    }

}