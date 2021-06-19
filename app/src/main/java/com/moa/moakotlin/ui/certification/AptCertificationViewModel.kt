package com.moa.moakotlin.ui.certification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.moa.moakotlin.data.ApartCertification
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.imagePicker.ImagePickerRepository
import com.moa.moakotlin.repository.user.UserRepository

class AptCertificationViewModel : ViewModel() {


    fun uploadImage(){
        var repository = ImagePickerRepository()
    }

    suspend fun certification(images : ArrayList<String>){

        var imageUploadRepository = ImagePickerRepository()

        var images = ArrayList<String>()
        images.forEach {

            imageUploadRepository.upload("CertificationImages",it)?.let { it1 -> images.add(it1) }
        }

        var repository = UserRepository()

        var apartCertification = ApartCertification(images, Timestamp.now(), User.getInstance().uid)

        repository.userCertification(apartCertification)
    }

}