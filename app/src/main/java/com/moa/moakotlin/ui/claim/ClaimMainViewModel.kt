package com.moa.moakotlin.ui.claim

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.FirebaseRepository

class ClaimMainViewModel : ViewModel() {

    var complaintList = MutableLiveData<ArrayList<Complaint>>()


  suspend  fun getMyClaimList(){
        var repository = FirebaseRepository<Complaint>()

        var list = repository.getDocumentList<Complaint>(
                FirebaseFirestore.getInstance()
                        .collection("PartnerApart")
                        .document(User.getInstance().aptCode)
                        .collection("Complaint")
                        .whereEqualTo("uid",User.getInstance().uid)
                        .orderBy("timeStamp",Query.Direction.DESCENDING)
                        .get()
        )
      complaintList.value = list
    }
}