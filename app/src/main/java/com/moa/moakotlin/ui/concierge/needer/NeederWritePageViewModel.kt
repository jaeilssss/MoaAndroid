package com.moa.moakotlin.ui.concierge.needer

import android.net.Uri
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.*
import com.moa.moakotlin.repository.concierge.NeederRepository
import com.moa.moakotlin.repository.imagePicker.ImagePickerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime
import kotlin.collections.ArrayList

class NeederWritePageViewModel() :ViewModel(){

   var title = MutableLiveData<String>("")
    var category = MutableLiveData<String>("")
    var isNego = MutableLiveData<Boolean>(true)
    var hopeWage = MutableLiveData<String>("")
    var content = MutableLiveData<String>("")

    var selectedPictureList = MutableLiveData<ArrayList<String>>()






}