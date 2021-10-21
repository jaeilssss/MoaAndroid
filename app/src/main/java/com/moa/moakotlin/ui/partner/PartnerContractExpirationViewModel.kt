package com.moa.moakotlin.ui.partner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.Contract
import com.moa.moakotlin.data.PartnerNotice
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.repository.FirebaseRepository

class PartnerContractExpirationViewModel : ViewModel() {

    var contractList = MutableLiveData<ArrayList<Contract>>()
    suspend fun getDocumentList(){
        var repository = FirebaseRepository<PartnerNotice>()

        contractList.value =  repository.getDocumentList<Contract>(
                FirebaseFirestore.getInstance()
                        .collection("PartnerApart")
                        .document(User.getInstance().aptCode)
                        .collection("Contract")
                        .whereLessThan("contractEndDate", Timestamp.now())
                        .orderBy("contractEndDate",Query.Direction.DESCENDING)
                        .get()

        )

    }
}