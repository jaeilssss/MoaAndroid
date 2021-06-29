package com.moa.moakotlin.ui.concierge.helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.imagePicker.ImagePickerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class HelperWritePageViewModel() :ViewModel(){

   var title = MutableLiveData<String>("")
    var category = MutableLiveData<String>("")
    var isNego = MutableLiveData<Boolean>(false)
    var hopeWage = MutableLiveData<String>("")
    var content = MutableLiveData<String>("")

    var selectedPictureList = MutableLiveData<ArrayList<String>>()





 fun check(){
  isNego.value=true
 }
 fun checkEdit() : Boolean{
  return title.value?.length!! >0 && category.value?.length!!>0 &&
          hopeWage.value?.length!!>0 && content.value?.length!!>0
 }

 suspend fun submit() : Helper{
  var helper = Helper()
  helper.aptCode = User.getInstance().aptCode
  helper.aptName = User.getInstance().aptName
  helper.content = content.value!!
  helper.title = title.value!!
  helper.hopeWage = hopeWage.value!!
  helper.mainCategory = category.value!!
  helper.uid = User.getInstance().uid
  helper.isNego = isNego.value!!
 var data = Helper()
  var repository = HelperRepository()
 var uploadedPathList = ArrayList<String>()
  var imageRepository = ImagePickerRepository()
   if(selectedPictureList.value?.size!!>0){

    for(path in selectedPictureList.value!!){

     var uploadPath= imageRepository.upload("helper",path)
     if (uploadPath != null) {
      uploadedPathList.add(uploadPath)
     }

    }


    helper.images = uploadedPathList

     data = repository.submit(helper.mainCategory,helper)
   }
  return data
 }

}