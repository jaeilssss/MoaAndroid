package com.moa.moakotlin.ui.concierge.needer

import android.net.Uri
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentManager
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


    var now = LocalDateTime.now()
    var title = ObservableField<String>("")
    var type = ObservableField<String>("")
    var count = ObservableField<String>("")
    var wage = ObservableField<String>("")
    var year= ObservableField<Int>(now.year)
    var month = ObservableField<Int>(now.monthValue)
    var day = ObservableField<Int>(now.dayOfMonth)
    var content = ObservableField<String>("")
    var imagelist : ArrayList<String> ?=null
    var list = ArrayList<String>()
    var mainCategory = ""
    var subCategory = ""
    var isNego = ObservableField<Boolean>(false)
    var i   = 0
    var isRe = ObservableField<Boolean>(false)


    fun test (list : ArrayList<String>){
        var repository = NeederRepository()
        var result = false
        var hopeDate = "${year.get()}년 ${month.get()}월 ${day.get()}일"
        var needer = Needer(title.get()!!,mainCategory,subCategory,hopeDate,isNego.get()!!, Timestamp.now(),null,content.get()!!,wage.get()!!,
                "","채용중",User.getInstance().uid,User.getInstance().aptCode,User.getInstance().aptName)
        CoroutineScope(Dispatchers.IO).async {
            if(list.size==0){
                result = repository.submit(mainCategory,needer)
            }else{
                var uploader = ImagePickerRepository()
                var images = ArrayList<String>()
                for(i in 0 until list.size){
                    images.add(uploader.upload("neederImages/",list.get(i))!!)
                    needer.images = images
                }
                result = repository.submit(mainCategory,needer)
            }

        }

    }



   suspend fun submit(list : ArrayList<String>){
        var repository = NeederRepository()
       var result = false
       var hopeDate = "${year.get()}년 ${month.get()}월 ${day.get()}일"
       var needer = Needer(title.get()!!,mainCategory,subCategory,hopeDate,isNego.get()!!, Timestamp.now(),null,content.get()!!,wage.get()!!,
               "","채용중",User.getInstance().uid,User.getInstance().aptCode,User.getInstance().aptName)
            if(list.size==0){
                 result = repository.submit(mainCategory,needer)
            }else{
                var uploader = ImagePickerRepository()
                var images = ArrayList<String>()
                for(i in 0 until list.size){
                    images.add(uploader.upload("neederImages/",list.get(i))!!)
                    needer.images = images
                }
                result = repository.submit(mainCategory,needer)
            }

    }


}