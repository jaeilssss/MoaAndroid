package com.moa.moakotlin.ui.concierge.helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.repository.concierge.HelperRepository
import kotlin.collections.ArrayList

class HelperWritePageViewModel() :ViewModel(){

   var title = MutableLiveData<String>("")
    var category = MutableLiveData<String>("")
    var isNego = MutableLiveData<Boolean>(true)
    var hopeWage = MutableLiveData<String>("")
    var content = MutableLiveData<String>("")

    var selectedPictureList = MutableLiveData<ArrayList<String>>()





 fun checkEdit() : Boolean{
  return title.value?.length!! >0 && category.value?.length!!>0 &&
          hopeWage.value?.length!!>0 && content.value?.length!!>0
 }

 fun submit(){
//  var helper = Helper()


  var repository = HelperRepository()
  if(selectedPictureList.value?.size!!>0){

  }
 }

}