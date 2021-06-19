package com.moa.moakotlin.repository.apt

import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.Apt
import kotlinx.coroutines.tasks.await

class AptRepository  {

   suspend fun getMyAroundNeighborhood(aptCode : String) : String {
        var db = FirebaseFirestore.getInstance()
        var aptName =""
       println(aptCode)
        db.collection("Apart").document(aptCode).get()
                .addOnSuccessListener {
                    if(it.exists()){

                        var data = it.toObject(Apt::class.java)
                        aptName = data?.aptName.toString()
                        println("-------")
                        println(aptName)
                    }
                }.await()
       return aptName
    }

}