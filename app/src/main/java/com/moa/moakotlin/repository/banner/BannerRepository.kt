package com.moa.moakotlin.repository.banner

import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.Magazine
import kotlinx.coroutines.tasks.await

class BannerRepository{



    suspend fun getMoaMagazine() : ArrayList<Magazine>{
        var db = FirebaseFirestore.getInstance()
        var list = ArrayList<Magazine>()
        db.collection("Magazine")
            .get()
            .addOnSuccessListener {
                for(document in it.documents){
                    var magazine = document.toObject(Magazine::class.java)
                    if (magazine != null) {
                        list.add(magazine)
                    }
                }
            }.await()

        return list
    }


//    suspend fun getHomeBanner()
}