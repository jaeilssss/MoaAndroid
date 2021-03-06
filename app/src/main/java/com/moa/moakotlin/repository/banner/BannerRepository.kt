package com.moa.moakotlin.repository.banner

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.Banner
import com.moa.moakotlin.data.Magazine
import kotlinx.coroutines.tasks.await

class BannerRepository{



    suspend fun getMoaMagazine() : ArrayList<Magazine>{
        var db = FirebaseFirestore.getInstance()
        var list = ArrayList<Magazine>()
        db.collection("Magazine")
                .orderBy("order",Query.Direction.DESCENDING)
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




    suspend fun getHomeBanner() : ArrayList<Banner>{
        var list = ArrayList<Banner>()

        var db = FirebaseFirestore.getInstance()

        db.collection("Banner")
                .whereEqualTo("type","main")
                .orderBy("order",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {

                for(document in it.documents){
                    var banner = document.toObject(Banner::class.java)
                    if (banner != null) {
                        list.add(banner)
                    }
                }
            }.addOnFailureListener {

                }.await()

        return list
    }

    suspend fun getVoiceBanner() : ArrayList<Banner>{
        var list = ArrayList<Banner>()

        var db = FirebaseFirestore.getInstance()

        db.collection("Banner")
                .whereEqualTo("type","voice")
                .orderBy("order",Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    for(document in it.documents){
                        var banner = document.toObject(Banner::class.java)
                        if (banner != null) {
                            list.add(banner)
                        }
                    }
                }.await()

        return list
    }
    suspend fun getConciergeBanner() : ArrayList<Banner>{

        var list = ArrayList<Banner>()

        var db = FirebaseFirestore.getInstance()

        db.collection("Banner")
                .whereEqualTo("type","concierge")
                .orderBy("order",Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    for(document in it.documents){
                        var banner = document.toObject(Banner::class.java)
                        if (banner != null) {
                            list.add(banner)
                        }
                    }
                }.await()

        return list
    }
}