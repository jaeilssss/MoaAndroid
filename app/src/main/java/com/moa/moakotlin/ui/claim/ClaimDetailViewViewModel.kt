package com.moa.moakotlin.ui.claim

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.FirebaseRepository

class ClaimDetailViewViewModel : ViewModel() {

    var complaintList = MutableLiveData<ArrayList<Complaint>>()
    suspend fun getDocumentListType(type : String) {
        var repository = FirebaseRepository<Complaint>()

        complaintList.value =  repository.getDocumentList(
                            FirebaseFirestore.getInstance()
                              .collection("PartnerApart")
                            .document(User.getInstance().aptCode)
                            .collection("Complaint")
                            .whereEqualTo("status",type)
                                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                            .get()
        )
    }

    suspend fun getDocumentList(){

            var repository = FirebaseRepository<Complaint>()

            complaintList.value =  repository.getDocumentList(
                    FirebaseFirestore.getInstance()
                            .collection("PartnerApart")
                            .document(User.getInstance().aptCode)
                            .collection("Complaint")
                            .orderBy("timeStamp",Query.Direction.DESCENDING)
                            .get()
            )
        }

}