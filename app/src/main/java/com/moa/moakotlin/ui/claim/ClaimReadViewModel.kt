package com.moa.moakotlin.ui.claim

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.FirebaseRepository

class ClaimReadViewModel : ViewModel() {


   suspend fun getWriterUser(uid : String) : User? {
   var repository = FirebaseRepository<User>()

       var result = repository.getDocument<User>(
               FirebaseFirestore.getInstance()
                       .collection("User")
                       .document(uid)
                       .get()
       )

       if(result.size!=0){
           return result.get(0)
       }else{
           return null
       }

   }
}