package com.moa.moakotlin.ui.imagepicker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImagePickerViewViewModel : ViewModel() {

    var selectedPictureList = MutableLiveData<ArrayList<String>>(ArrayList<String>())

    var list = ArrayList<String>()

}