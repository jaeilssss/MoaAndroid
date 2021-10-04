package com.moa.moakotlin.ui.partner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.Contract
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.FirebaseRepository

class PartnerContractViewModel : ViewModel() {

    var partnerContractList = MutableLiveData<ArrayList<Contract>>()
    suspend fun  getDocumentList(){
        var repository = FirebaseRepository<Contract>()

        var list = repository.getDocumentList<Contract>(
                FirebaseFirestore.getInstance()
                        .collection("PartnerApart")
                        .document(User.getInstance().aptCode)
                        .collection("Contract")
                        .get()

        )
        partnerContractList.value = list
    }
}