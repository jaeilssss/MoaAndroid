package com.moa.moakotlin.ui.claim

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.complaint.ComplaintRepository

class ClaimWriteViewModel : ViewModel() {


    var title = MutableLiveData<String>("")

    var category = MutableLiveData<String>("")

    var content = MutableLiveData<String>("")

    var images = ArrayList<String>()

    var isPrivate = MutableLiveData<Boolean>(false)

    var isWriteComplete = MutableLiveData<Boolean>(false)
    var complaint = Complaint()

    fun check() : Boolean{

        return title.value?.length!! >0 && category.value?.length!!>0 && content.value?.length!!>0
    }


    suspend fun submit() {

        var repository = ComplaintRepository()



        complaint.title = title.value.toString()
        complaint.category = category.value.toString()
        complaint.content = content.value.toString()

        complaint.timeStamp = Timestamp.now()
        complaint.isPrivate = isPrivate.value!!
        complaint.uid = User.getInstance().uid
        complaint.status = "requested"

        if(images.isNotEmpty()){
            repository.upload(-1,complaint.category, images,complaint) {
                isWriteComplete.value = repository.write(complaint)
            }

        }else{
            isWriteComplete.value = repository.write(complaint)
        }

    }

}