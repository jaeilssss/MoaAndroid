package com.moa.moakotlin.ui.certification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
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



    suspend fun signUp(){
        var repository = UserRepository()
        User.getInstance().phoneUid = FirebaseAuth.getInstance().currentUser?.uid!!
        var uid = repository.signUpUser(User.getInstance())
        if(uid!=null){
            User.getInstance().uid = uid
            repository.modify(User.getInstance())
        }

    }
     fun certification(images : List<String>){

         CoroutineScope(Dispatchers.IO).async {
             var imageUploadRepository = ImagePickerRepository()

             var imagePathList = ArrayList<String>()
             images.forEach {

                 imageUploadRepository.upload("CertificationImages",it)?.let { it1 -> imagePathList.add(it1) }
             }

             var repository = UserRepository()
//
//             var apartCertification = ApartCertification(images, Timestamp.now(), User.getInstance().uid)
             var apartCertification = FirebaseAuth.getInstance().currentUser?.let { ApartCertification(imagePathList, Timestamp.now(), it.uid) }

             if (apartCertification != null) {
                 repository.userCertification(apartCertification)
             }
         }

    }

}