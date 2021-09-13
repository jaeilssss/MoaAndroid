package com.moa.moakotlin.ui.claim

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.repository.concierge.NeederRepository

class ClaimModifyViewModel : ViewModel() {
    var isPrivate = MutableLiveData<Boolean>(false)
    var title = MutableLiveData<String>("")
    var content = MutableLiveData<String>("")
    var images =ArrayList<String>()
    var selectedPictureList = MutableLiveData<ArrayList<String>>()
    suspend fun submit(uploadedPosition : Int, needer : Needer){


        if(uploadedPosition==-1){
            if(selectedPictureList.value?.size!=0){
//                repository.upload(uploadedPosition,needer.mainCategory, selectedPictureList.value!!,needer){
//                    println("invoke 부분")
////                    newNeeder.value = repository.modify(needer.mainCategory,needer)
//                }
            }else{
//                newNeeder.value = repository.modify(needer.mainCategory,needer)
            }

        }else{
            if(selectedPictureList.value?.size!=0){
                println("여기 ${needer.images?.get(0)}")
                println("size 0>  ${selectedPictureList.value?.size}")
//                repository.upload(uploadedPosition,needer.mainCategory, selectedPictureList.value!!,needer){
////                    newNeeder.value = repository.modify(needer.mainCategory,needer)
//                }
            }
        }
    }
}