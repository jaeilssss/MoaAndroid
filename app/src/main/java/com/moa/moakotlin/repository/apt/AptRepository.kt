package com.moa.moakotlin.repository.apt

import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.Apt
import com.moa.moakotlin.data.RequestApart
import kotlinx.coroutines.tasks.await

class AptRepository  {

   suspend fun getMyAroundNeighborhood(aptCode : String) : String {
        var db = FirebaseFirestore.getInstance()
        var aptName =""
       println(aptCode)
       println("l;;sd;sdkd;sdkl;s")
        db.collection("Aparts").document(aptCode).get()
                .addOnSuccessListener {
                    if(it.exists()){

                        var data = it.toObject(Apt::class.java)
                        aptName = data?.aptName.toString()
                        println(aptName)
                    }
                }.await()
       return aptName
    }

  suspend fun claimNewApt(request : RequestApart) : Boolean{

        var result = false
        var db = FirebaseFirestore.getInstance()

        db.collection("RequestedApart").add(request).addOnSuccessListener {
            result = true
        }.await()
            return result
    }


}