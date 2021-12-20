package com.moa.moakotlin.ui.partner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.PartnerNotice
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.repository.FirebaseRepository

class PartnerNoticeViewModel : ViewModel() {
    var noticeList = MutableLiveData<ArrayList<PartnerNotice>>()
    suspend fun getDocumentList(){
        var repository = FirebaseRepository<PartnerNotice>()

        var list = ArrayList<PartnerNotice>()



        noticeList.value =  repository.getDocumentList<PartnerNotice>(
                FirebaseFirestore.getInstance()
                        .collection("Notice")
                        .document(aptList.getInstance().gu)
                        .collection("Notice")
                        .whereIn("aptCode",listOf(User.getInstance().aptCode, aptList.getInstance().gu))
                        .orderBy("isPriority",Query.Direction.DESCENDING)
                        .orderBy("timeStamp",Query.Direction.DESCENDING)
                        .get()

        )


    }

}