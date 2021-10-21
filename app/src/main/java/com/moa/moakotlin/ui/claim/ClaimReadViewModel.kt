package com.moa.moakotlin.ui.claim

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.Comment
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.FirebaseRepository

class ClaimReadViewModel : ViewModel() {


    var comment = MutableLiveData<String>("")
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

    suspend fun getComment(documentId : String , aptCode : String) : ArrayList<Comment>{

        var repository = FirebaseRepository<Comment>()

        return repository.getDocumentList(
                FirebaseFirestore.getInstance()
                        .collection("PartnerApart")
                        .document(aptCode)
                        .collection("Complaint")
                        .document(documentId)
                        .collection("Comment")
                        .orderBy("timeStamp")
                        .get()
        )

    }

    suspend fun sendComment(documentId : String , aptCode : String) : Boolean{

        var data = Comment()

        data.content = comment.value!!

        data.uid = User.getInstance().uid

        data.timeStamp = Timestamp.now()

        var repository = FirebaseRepository<Comment>()

         return repository.writeDocument(
                FirebaseFirestore.getInstance()
                        .collection("PartnerApart")
                        .document(aptCode)
                        .collection("Complaint")
                        .document(documentId)
                        .collection("Comment")
                        .add(data)
        )
    }
    suspend fun delete(complaint : Complaint) : Boolean{
        var repository = FirebaseRepository<Complaint>()

        println(complaint.documentId)
        return repository.deleteDocument(
                FirebaseFirestore.getInstance()
                        .collection("PartnerApart")
                        .document(User.getInstance().aptCode)
                        .collection("Complaint")
                        .document(complaint.documentId)
                        .delete()
        )
    }
    suspend fun deleteComment(documentId: String, aptCode: String , commentId : String) : Boolean{

        var repository = FirebaseRepository<Comment>()

        return repository.deleteDocument(
                FirebaseFirestore.getInstance()
                        .collection("PartnerApart")
                        .document(aptCode)
                        .collection("Complaint")
                        .document(documentId)
                        .collection("Comment")
                        .document(commentId)
                        .delete()
        )


    }


}