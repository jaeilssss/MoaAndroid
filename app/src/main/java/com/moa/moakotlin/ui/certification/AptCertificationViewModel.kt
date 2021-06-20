package com.moa.moakotlin.ui.certification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.moa.moakotlin.data.ApartCertification
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.imagePicker.ImagePickerRepository
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class AptCertificationViewModel : ViewModel() {


    fun uploadImage(){
        var repository = ImagePickerRepository()
    }

     fun certification(images : List<String>){


         CoroutineScope(Dispatchers.IO).async {
             var imageUploadRepository = ImagePickerRepository()

             var imagePathList = ArrayList<String>()
             images.forEach {

                 println("이미지 업로드 반복문 입니다")
                 imageUploadRepository.upload("CertificationImages",it)?.let { it1 -> imagePathList.add(it1) }
             }

             var repository = UserRepository()
//
//             var apartCertification = ApartCertification(images, Timestamp.now(), User.getInstance().uid)
             var apartCertification = ApartCertification(imagePathList, Timestamp.now(), "test입니다 재일")

             repository.userCertification(apartCertification)
         }

    }

}