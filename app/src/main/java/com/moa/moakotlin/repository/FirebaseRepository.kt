package com.moa.moakotlin.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.moa.moakotlin.base.BaseModel
import com.moa.moakotlin.data.Comment
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.Needer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream

class FirebaseRepository<T> {

  suspend inline fun <reified T> getDocument(db : Task<DocumentSnapshot>) : ArrayList<T> {
        var list = ArrayList<T>()
        db.addOnSuccessListener {
                if(it.exists()){
                    var data = it.toObject(T::class.java)
                    if (data != null) {
                        list.add(data)
                    }
                }
        }.addOnFailureListener {

        }.await()
      return list
    }

  suspend fun writeDocument(db :  Task<DocumentReference>) : Boolean {
        var check  = false
        db.addOnSuccessListener {
            check = true
        }.addOnFailureListener {
            check  = false
        }.await()
      

        return check
    }

    suspend fun modifyDocument(db : Task<Void>) : Boolean{
        var check  = false
        db.addOnSuccessListener {
            check = true
        }.addOnFailureListener {
            check = false
        }.await()


        return check
    }



   suspend fun deleteDocument(db : Task<Void>) : Boolean{
    var check = false
        db.addOnSuccessListener {
            check = true
        }.addOnFailureListener {
            check = false
        }.await()
       return check
    }

   suspend inline fun <reified T> getDocumentList(db :  Task<QuerySnapshot>) : ArrayList<T>{

        var list = ArrayList<T>()
        db.addOnSuccessListener {

            for(document in it.documents){
                var data = document.toObject(T::class.java)
                if (data != null) {

                    if(data is Complaint){
                        data.documentId = document.id
                    }
                    if(data is BaseModel){
                        data.documentId = document.id
                    }
                    list.add(data)
                }
            }


        }.addOnFailureListener {

        }.await()
       return list
    }

     inline fun <reified T> upload(num : Int, pathString : String, picturePathList : ArrayList<String>, crossinline action : (HashMap<Int,String>)-> Unit){
//        var picture = adapter.list.get(adapter.checkBox)
        var i = 0
//            helper.images = ArrayList()
        var uploadedList = HashMap<Int,String>()
        for(index in num until picturePathList.size){

//            if(num!=-1 && num>=i){
//
//                if(i==picturePathList.size-1){
//                    CoroutineScope(Dispatchers.Main).launch{
//                        action.invoke(uploadedList)
//                    }
//                }
//                i++
//            }else{

                var result : String ?=null
                var storageRef : StorageReference = FirebaseStorage.getInstance().reference


                var file = Uri.fromFile(File(picturePathList[index]))

                var inputstream = FileInputStream(File(picturePathList[index]))

                val riversRef = storageRef.child(pathString+"/" + file.lastPathSegment)
                val number = i++
                val uploadTask = riversRef.putStream(inputstream)

                uploadTask.continueWithTask { riversRef.downloadUrl }.addOnCompleteListener { task ->
                    uploadedList.put(number, task.result.toString())
                    if(uploadedList.size==picturePathList.size){


                            action.invoke(uploadedList)


                    }

                }
            }

        }

//    }



}