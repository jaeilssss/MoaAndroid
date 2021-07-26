package com.moa.moakotlin.repository.review

import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.Review
import kotlinx.coroutines.tasks.await

class ReviewRepository {

   suspend fun getReviewList(helperUid : String) : ArrayList<Review>{

       var db = FirebaseFirestore.getInstance()

       var list = ArrayList<Review>()

       db.collection("Review")
           .whereEqualTo("helperUid",helperUid)
           .get()
           .addOnSuccessListener {
               for(document in it.documents){
                   var review  = document.toObject(Review::class.java)

                   if (review != null) {
                       list.add(review)
                   }
               }
           }.await()



       return list
    }
}