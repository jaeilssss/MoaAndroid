package com.moa.moakotlin.ui.concierge.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HelperMainCategoryViewModel : ViewModel() {

    var selectKid = MutableLiveData<Boolean>(false)
    var selectEdu = MutableLiveData<Boolean>(false)
    var selectPet = MutableLiveData<Boolean>(false)
    var selectInterior = MutableLiveData<Boolean>(false)
    var selectEtc = MutableLiveData<Boolean>(false)




}