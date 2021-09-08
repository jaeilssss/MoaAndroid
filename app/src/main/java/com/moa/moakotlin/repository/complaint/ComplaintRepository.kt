package com.moa.moakotlin.repository.complaint

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import java.io.File
import java.io.FileInputStream


class ComplaintRepository {

  suspend fun getMyClaimList(aptCode : String) : ArrayList<Complaint>{

        var complaintList = ArrayList<Complaint>()
        var db = FirebaseFirestore.getInstance()

        db.collection("PartnerApart")
                .document(aptCode)
                .collection("Complaint")
                .whereEqualTo("uid", User.getInstance().uid)
                .get()
                .addOnSuccessListener {
                    if(it.isEmpty.not()){
                        for(document in it.documents){
                            var newData = document.toObject(Complaint::class.java)
                            if (newData != null) {
                                complaintList.add(newData)
                            }
                        }
                    }
                }.await()

      return complaintList

    }


    suspend fun write(complaint: Complaint) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var result = false
        db.collection("PartnerApart")
                .document(User.getInstance().aptCode)
                .collection("Complaint")
                .add(complaint)
                .addOnSuccessListener {
                    result = true
                }.addOnFailureListener {
                    result = false
                }.await()

        return result
    }

    fun upload(num : Int, pathString : String, picturePathList : ArrayList<String>, complaint : Complaint, action :suspend ()-> Unit){
//        var picture = adapter.list.get(adapter.checkBox)
        var i = 0
//            helper.images = ArrayList()
        var uploadedList = HashMap<Int,String>()
        for(picturePath in picturePathList){

            if(num!=-1 && num>=i){

                if(i==picturePathList.size-1){
                    CoroutineScope(Dispatchers.Main).async {
                        action.invoke()
                    }
                }
                i++
            }else{

                var result : String ?=null
                var storageRef : StorageReference = FirebaseStorage.getInstance().reference


                var file = Uri.fromFile(File(picturePath))

                var inputstream = FileInputStream(File(picturePath))

                val riversRef = storageRef.child(pathString+"/" + file.lastPathSegment)
                val number = i++
                val uploadTask = riversRef.putStream(inputstream)

                uploadTask.continueWithTask { riversRef.downloadUrl }.addOnCompleteListener { task ->
                    uploadedList.put(number, task.result.toString())
                    if(uploadedList.size==picturePathList.size){

                        for(i in 0 until picturePathList.size){
                            uploadedList[i]?.let { complaint.images?.add(it) }
                        }
                        CoroutineScope(Dispatchers.Main).async {
                            action.invoke()

                        }
                    }

                }
            }

        }

    }

}