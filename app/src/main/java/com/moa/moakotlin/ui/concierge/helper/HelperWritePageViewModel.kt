package com.moa.moakotlin.ui.concierge.helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.imagePicker.ImagePickerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class HelperWritePageViewModel() :ViewModel(){

   var title = MutableLiveData<String>("")
    var category = MutableLiveData<String>("")
    var isNego = MutableLiveData<Boolean>(false)
    var hopeWage = MutableLiveData<String>("")
    var content = MutableLiveData<String>("")
 var uploadedPathList = ArrayList<String>()
    var selectedPictureList = MutableLiveData<ArrayList<String>>()
 var helper = Helper()
 var repository = HelperRepository()
 var newHelper = MutableLiveData<Helper>()

 fun check(){
  isNego.value=true
 }
 fun checkEdit() : Boolean{
  return title.value?.length!! >0 && category.value?.length!!>0 &&
          hopeWage.value?.length!!>0 && content.value?.length!!>0
 }

 suspend fun submit(){

  var i=1
  helper = Helper()

  helper.aroundApt = aptList.getInstance().aroundApt
  helper.aptCode = User.getInstance().aptCode
  helper.aptName = User.getInstance().aptName
  helper.content = content.value!!
  helper.title = title.value!!
  helper.hopeWage = hopeWage.value!!
  helper.mainCategory = category.value!!
  helper.uid = User.getInstance().uid
  helper.isNego = isNego.value!!

  if(selectedPictureList.value!!.size==0){
   println("??왜 0이야???")
   newHelper.value = repository.submit(helper.mainCategory,helper)
  }else{

    repository.upload(-1,helper.mainCategory, selectedPictureList.value!!,helper) {
     newHelper.value = repository.submit(helper.mainCategory,helper)

   }
  }
  }

}