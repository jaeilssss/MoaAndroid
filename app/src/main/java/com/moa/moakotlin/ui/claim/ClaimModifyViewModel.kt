package com.moa.moakotlin.ui.claim

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.FirebaseRepository
import com.moa.moakotlin.repository.concierge.NeederRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClaimModifyViewModel : ViewModel() {
    var isPrivate = MutableLiveData<Boolean>(false)
    var title = MutableLiveData<String>("")
    var content = MutableLiveData<String>("")
    var images =ArrayList<String>()
    var selectedPictureList = MutableLiveData<ArrayList<String>>()
    var category = MutableLiveData<String>("")
    lateinit var complaint : Complaint
     var uploadedPosition = 0
      var newData = MutableLiveData<Complaint>()
    fun check() : Boolean{

        return title.value?.length!! >0 && category.value?.length!!>0 && content.value?.length!!>0
    }

    suspend fun submit(pictureList : ArrayList<String>, complaint : Complaint) {

             var check  = false
             complaint.isPrivate = isPrivate.value!!
             complaint.title = title.value!!
             complaint.content = content.value!!
             complaint.category = category.value!!


             var repository = FirebaseRepository<Complaint>()
            if( pictureList.size==0){
                    check = repository.modifyDocument(
                            FirebaseFirestore.getInstance()
                                    .collection("PartnerApart")
                                    .document(User.getInstance().aptCode)
                                    .collection("Complaint")
                                    .document(complaint.documentId)
                                    .set(complaint)
                    )
                    if(check){
                        newData.value = complaint
                    }
            }else {
                    
                     repository.upload<Complaint>(0, category.value!!, pictureList) { map ->
                        println("업로드 된 메소드 ...")
                         for (key in map.keys) {

                             map[key]?.let { complaint.images?.add(it) }

                         }
                            CoroutineScope(Dispatchers.Main).launch {
                                check = repository.modifyDocument(
                                    FirebaseFirestore.getInstance()
                                        .collection("PartnerApart")
                                        .document(User.getInstance().aptCode)
                                        .collection("Complaint")
                                        .document(complaint.documentId)
                                        .set(complaint)
                                )
                                if(check){
                                    newData.value = complaint
                                }
                            }



                     }
                 }

    }


     fun setViewData(complaint: Complaint){
        this.complaint = complaint
        title.value = complaint.title

        content.value = complaint.content

        isPrivate.value = complaint.isPrivate

        selectedPictureList.value = complaint.images
         category.value = complaint.category
         if(complaint.images.size>0){
             uploadedPosition = complaint.images!!.size-1
         }
    }


}