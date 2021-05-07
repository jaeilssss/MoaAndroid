package com.moa.moakotlin.ui.imagepicker

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.Chat
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.User
import java.io.File
import java.io.FileInputStream

class KidImagePickerViewModel() : ViewModel(){

     fun prev() {
        Picture.deleteInstance()
    }
}
