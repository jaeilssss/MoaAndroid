package com.moa.moakotlin.ui.concierge.needer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.concierge.NeederRepository

class NeederModifyViewModel : ViewModel() {
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
    var selectedPictureList = MutableLiveData<ArrayList<String>>()
    var mainCategory = MutableLiveData<String>("")
    var subCategory = MutableLiveData<String>("")
    var newNeeder = MutableLiveData<Needer>()


    fun checkEdit() : Boolean{
        return title.value!!.length>0 &&
                wage.value!!.length>0 &&
                content.value!!.length>0 &&
                hopeDate.value!!.length>0 &&
                mainCategory.value!!.length>0&&
                subCategory.value!!.length>0
    }

    suspend fun submit(uploadedPosition : Int, needer : Needer){
        var repository = NeederRepository()
        needer.title = title.value.toString()
        needer.isNego = isNego.value!!
        needer.hopeWage = wage.value.toString()
        needer.mainCategory = mainCategory.value.toString()
        needer.content = content.value.toString()
        needer.subCategory = subCategory.value.toString()

        if(uploadedPosition==-1){
            if(selectedPictureList.value?.size!=0){
                repository.upload(uploadedPosition,needer.mainCategory, selectedPictureList.value!!,needer){
                    println("invoke 부분")
                    newNeeder.value = repository.modify(needer.mainCategory,needer)
                }
            }else{
                newNeeder.value = repository.modify(needer.mainCategory,needer)
            }

        }else{
            if(selectedPictureList.value?.size!=0){
                println("여기 ${needer.images?.get(0)}")
                println("size 0>  ${selectedPictureList.value?.size}")
                repository.upload(uploadedPosition,needer.mainCategory, selectedPictureList.value!!,needer){
                    newNeeder.value = repository.modify(needer.mainCategory,needer)
                }
            }
        }
    }
}