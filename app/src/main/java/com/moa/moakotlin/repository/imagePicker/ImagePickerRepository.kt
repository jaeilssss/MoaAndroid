package com.moa.moakotlin.repository.imagePicker

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import com.moa.moakotlin.data.ChattingRoom
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream

class ImagePickerRepository  {

    suspend fun upload(pathString : String , picturePath : String) : String?{
//        var picture = adapter.list.get(adapter.checkBox)
        var result : String ?=null
        var storageRef = FirebaseStorage.getInstance().getReference()

        var file = Uri.fromFile(File(picturePath))

        var inputstream = FileInputStream(File(picturePath))

        val riversRef = storageRef.child(pathString + file.lastPathSegment)

        val uploadTask = riversRef.putStream(inputstream)

        uploadTask.continueWithTask { riversRef.downloadUrl }.addOnCompleteListener { task ->
            result = task.result.toString()

        }.await()


        return result
    }
}