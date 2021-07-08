package com.moa.moakotlin.repository.concierge

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream

class NeederRepository {

    suspend fun submit(mainCategory: String,needer : Needer) : Needer{
        var db = FirebaseFirestore.getInstance()
        var result =false
        db.collection("Needer")
                .document(mainCategory)
                .collection(mainCategory)
                .add(needer).addOnSuccessListener{
                needer.documentID = it.id
                }.await()
        return needer
    }
    suspend fun modify(mainCategory: String,needer : Needer) : Needer{
        var db = FirebaseFirestore.getInstance()
        db.collection("Needer").document(mainCategory)
            .collection(mainCategory)
            .document(needer.documentID!!)
            .set(needer).addOnCompleteListener {
            }.await()
        return needer
    }
    suspend  fun initSetList(mainCategory: String) : ArrayList<Needer>{
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Needer>()
        db.collection("Needer").document(mainCategory)
            .collection(mainCategory)
            .whereArrayContains("aroundApt", User.getInstance().aptCode)
            .orderBy("timeStamp",Query.Direction.DESCENDING)
            .limit(5).get().addOnSuccessListener {
                for(document in it.documents){
                    var data = document.toObject(Needer::class.java)
                    data?.documentID = document.id
                    if (data != null) {
                        result.add(data)
                    }
                }
            }.await()
        return result

    }

    fun upload(num : Int, pathString : String , picturePathList : ArrayList<String>,helper : Needer,action :suspend ()-> Unit){
//        var picture = adapter.list.get(adapter.checkBox)
        var i = 0
//            helper.images = ArrayList()
        var uploadedList = HashMap<Int,String>()
        for(picturePath in picturePathList){

            if(num!=-1 && num>=i){

                if(i==picturePathList.size-1){
                    CoroutineScope(Dispatchers.Main).async {
                        println("ddd")
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
                    if(number+1==picturePathList.size){

                        for(i in 0 until picturePathList.size){
                            uploadedList[i]?.let { helper.images?.add(it) }
                        }
                        CoroutineScope(Dispatchers.Main).async {
                            action.invoke()

                        }
                    }

                }
            }

        }

    }

    suspend fun getList(mainCategory: String) : ArrayList<Needer> {
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Needer>()
        db.collection("Needer")
                .document(mainCategory)
                .collection(mainCategory)
                .whereArrayContains("aroundApt", User.getInstance().aptCode)
                .orderBy("timeStamp", Query.Direction.DESCENDING).limit(50)
                .get().addOnSuccessListener {
                    for(document in it.documents){
                        var data = document.toObject(Needer::class.java)
                        data?.documentID = document.id
                        if (data != null) {
                            result.add(data)
                        }
                    }
                }.await()
        return result
    }


}