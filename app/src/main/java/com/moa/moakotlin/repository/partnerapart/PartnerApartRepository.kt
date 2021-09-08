package com.moa.moakotlin.repository.partnerapart

import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.PartnerApart
import com.moa.moakotlin.data.User
import kotlinx.coroutines.tasks.await

class PartnerApartRepository {

   suspend fun findPartnerApart() : PartnerApart?{
       var result : PartnerApart ?=null
        var db = FirebaseFirestore.getInstance()

        db.collection("PartnerApart")
            .whereEqualTo("aptCode", User.getInstance().aptCode)
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    for(document in it.documents){
                        result = document.toObject(PartnerApart::class.java) 
                    }

                }
            }.await()

       return result
    }
}