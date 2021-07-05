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
    var selectedPictureList = MutableLiveData<ArrayList<String>>()
    var helper = Helper()
    var repository = HelperRepository()
    var newHelper = MutableLiveData<Helper>()
    fun checkEdit() : Boolean{
        return title.value?.length!! >0 && category.value?.length!!>0 &&
                hopeWage.value?.length!!>0 && content.value?.length!!>0
    }
}