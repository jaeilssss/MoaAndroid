package com.moa.moakotlin.ui.claim

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.FirebaseRepository
import com.moa.moakotlin.repository.complaint.ComplaintRepository

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
                        .get()
        )
      complaintList.value = list
    }
}