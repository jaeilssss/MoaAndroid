package com.moa.moakotlin.repository.concierge

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.aptList
import kotlinx.coroutines.tasks.await

class HelperRepository {

    suspend fun submit(mainCaregory: String,helper : Helper) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var result =false
        db.collection("Helper").document(mainCaregory)
                .collection(mainCaregory)
                .add(helper).addOnSuccessListener {
                    result = true
                }.await()

        return result

    }
    suspend fun modify(mainCaregory: String,helper : Helper) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var result = false
        helper.documentID?.let {

            db.collection("Helper").document(mainCaregory)
                    .collection(mainCaregory)
                    .document(it).set(helper).addOnSuccessListener{
                        result = true
                    }.await()
        }
        return result
    }
    suspend fun initSetList(mainCaregory : String) :ArrayList<Helper>{
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Helper>()
        db.collection("Helper").document(mainCaregory)
                .collection(mainCaregory)
                .whereIn("aptCode",aptList.getInstance().aroundApt)
                .orderBy("timeStamp",Query.Direction.DESCENDING)
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
    suspend fun getList(mainCaregory: String) : ArrayList<Helper> {
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Helper>()
        db.collection("Helper")
                .document(mainCaregory)
                .collection(mainCaregory)
                .orderBy("timeStamp",Query.Direction.DESCENDING)
                .whereIn("aptCode",aptList.getInstance().aroundApt)
                .limit(50)
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