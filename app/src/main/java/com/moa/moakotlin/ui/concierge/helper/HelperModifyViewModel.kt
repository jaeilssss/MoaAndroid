package com.moa.moakotlin.ui.concierge.helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.repository.concierge.HelperRepository

class HelperModifyViewModel : ViewModel() {
    var title = MutableLiveData<String>("")
    var category = MutableLiveData<String>("")
    var isNego = MutableLiveData<Boolean>(false)
    var hopeWage = MutableLiveData<String>("")
    var content = MutableLiveData<String>("")
    var uploadedPathList = ArrayList<String>()
    var selectedPictureList = MutableLiveData<ArrayList<String>>(ArrayList())
    var repository = HelperRepository()
    var newHelper = MutableLiveData<Helper>()
    fun checkEdit() : Boolean{
        return title.value?.length!! >0 && category.value?.length!!>0 &&
                hopeWage.value?.length!!>0 && content.value?.length!!>0
    }

    suspend fun submit(uploadedPosition : Int,helper : Helper){
        var repository = HelperRepository()
        helper.title = title.value.toString()
        helper.isNego = isNego.value!!
        helper.hopeWage = hopeWage.value.toString()
        helper.mainCategory = category.value.toString()
        helper.content = content.value.toString()
        if(uploadedPosition==-1){
                if(selectedPictureList.value?.size!=0){
                    repository.upload(uploadedPosition,helper.mainCategory, selectedPictureList.value!!,helper){
                        newHelper.value = repository.modify(helper.mainCategory,helper)
                    }
                }else{
                    newHelper.value = repository.modify(helper.mainCategory,helper)
                }

        }else{
            if(selectedPictureList.value?.size!=0){
                repository.upload(uploadedPosition,helper.mainCategory, selectedPictureList.value!!,helper){
                    newHelper.value = repository.modify(helper.mainCategory,helper)
                }
            }
        }
    }

}