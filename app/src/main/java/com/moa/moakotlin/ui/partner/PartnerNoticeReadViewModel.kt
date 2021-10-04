package com.moa.moakotlin.ui.partner

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.Partner
import com.moa.moakotlin.repository.FirebaseRepository

class PartnerNoticeReadViewModel : ViewModel() {


    suspend fun getWriterInfo(uid : String): Partner{
        var repository = FirebaseRepository<Partner>()

        return repository.getDocument<Partner>(
                FirebaseFirestore.getInstance()
                        .collection("Partner")
                        .document(uid)
                        .get()
        )[0]
    }
}