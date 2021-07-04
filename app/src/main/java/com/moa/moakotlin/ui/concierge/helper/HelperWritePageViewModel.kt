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
  uploadedPathList = ArrayList<String>()
  helper.aroundApt = aptList.getInstance().aroundApt
  helper.aptCode = User.getInstance().aptCode
  helper.aptName = User.getInstance().aptName
  helper.content = content.value!!
  helper.title = title.value!!
  helper.hopeWage = hopeWage.value!!
  helper.mainCategory = category.value!!
  helper.uid = User.getInstance().uid
  helper.isNego = isNego.value!!


  helper.images = selectedPictureList.value
   helper.images?.let {
    repository.upload(helper.mainCategory, it,helper) {
   newHelper.value = repository.submit(helper.mainCategory,helper)
    }
   }
  }


 suspend fun upload(path : String,num : Int) : Boolean{
  var imageRepository = ImagePickerRepository()
  var uploadPath= imageRepository.upload("helper",path)
  if (uploadPath != null) {
   println("${num} 번째 ->  ${uploadedPathList.size} ")
   uploadedPathList.add(uploadPath)
  }
  if(num==uploadedPathList.size){
   helper.images = uploadedPathList

  return storeSubmit()

  }
  return false
 }

 suspend fun storeSubmit() : Boolean{
  var data=  repository.submit(helper.mainCategory,helper)
  return true
 }
}