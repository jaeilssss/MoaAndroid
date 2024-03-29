package com.moa.moakotlin.ui.concierge.needer

import android.net.Uri
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.moa.moakotlin.data.*
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.concierge.NeederRepository
import com.moa.moakotlin.repository.imagePicker.ImagePickerRepository
import com.moa.moakotlin.repository.push.FcmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.File
import java.io.FileInputStream

class NeederWriteViewModel():ViewModel() {

    var title = MutableLiveData<String>("")

    var wage = MutableLiveData<String>("")
    var content = MutableLiveData<String>("")
    lateinit var images : ArrayList<String>
    var pictureCount = MutableLiveData<String>("0")
    @field:JvmField
    var isNego = MutableLiveData<Boolean>(false)
    var imagelist : ArrayList<String> ?=null
    var list = ArrayList<String>()
    var hopeDate = MutableLiveData<String>()
    var i = 0
    var selectedPictureList = MutableLiveData<ArrayList<String>>(ArrayList())
    var mainCategory = MutableLiveData<String>("")
    var subCategory = MutableLiveData<String>("")
    var newNeeder = MutableLiveData<Needer>()

    fun check() : Boolean{
        return title.value?.length!!>0 &&
                content.value?.length!!>0 &&
                wage.value?.length!!>0 &&
                hopeDate.value?.length!!>0 &&
                mainCategory.value?.length!!>0 &&
                subCategory.value?.length!!>0
    }
    suspend fun submit(){

        var i=1
        var repository = NeederRepository()
      var needer = Needer()

        needer.aroundApt = aptList.getInstance().aroundApt
        needer.aptCode = User.getInstance().aptCode
        needer.aptName = User.getInstance().aptName
        needer.content = content.value!!
        needer.title = title.value!!
        needer.hopeWage = wage.value!!
        needer.hopeDate = hopeDate.value!!
        needer.mainCategory = mainCategory.value!!
        needer.subCategory = subCategory.value!!
        needer.uid = User.getInstance().uid
        needer.hireStatus = "모집중"
        needer.isNego = isNego.value!!

        if(selectedPictureList.value!!.size==0){
            newNeeder.value = repository.submit(needer.mainCategory,needer)
        }else{
            repository.upload(-1,needer.mainCategory, selectedPictureList.value!!,needer) {
                newNeeder.value = repository.submit(needer.mainCategory,needer)
            }
        }
    }

    fun sendPushMessage(){
        var repository = FcmRepository()

        var message = PushMessage("재능공유","이웃의 새로운 도움요청 글이 작성되었습니다 ",User.getInstance().pushToken)
        repository.sendPushMessageToNeighborhood(message)
    }

    fun sendPushMoa(){
        var repository = FcmRepository()

        var message = PushMessage("재능공유","모아의 새로운 도움요청 글이 작성되었습니다 ",User.getInstance().pushToken)
        repository.sendPushMessageToMoa(message)
    }

}