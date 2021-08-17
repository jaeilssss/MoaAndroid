package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.moa.moakotlin.data.ApartCertification
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.imagePicker.ImagePickerRepository
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AptModifyCertificationViewModel : ViewModel() {

var oldAptCode =""

    fun certification(images : List<String>){


        CoroutineScope(Dispatchers.Main).launch {
            var imageUploadRepository = ImagePickerRepository()

            var imagePathList = ArrayList<String>()
            images.forEach {

                imageUploadRepository.upload("CertificationImages",it)?.let { it1 -> imagePathList.add(it1) }
            }

            var repository = UserRepository()
//
//             var apartCertification = ApartCertification(images, Timestamp.now(), User.getInstance().uid)
            var apartCertification =  ApartCertification(imagePathList, Timestamp.now(), User.getInstance().uid)

            if (apartCertification != null) {
                repository.userCertification(apartCertification)
            }
        }

    }

    suspend fun userCertificationModify(){
        var repository = UserRepository()

        repository.modify(User.getInstance())
    }




}