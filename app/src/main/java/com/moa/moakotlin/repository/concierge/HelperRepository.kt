package com.moa.moakotlin.repository.concierge

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream

class HelperRepository {
    companion object{
        val mainHelperCategoryList = arrayListOf<String>("육아","기타","인테리어","반려동물케어","교육")
    }
    suspend fun submit(mainCaregory: String,helper : Helper) : Helper{
        var db = FirebaseFirestore.getInstance()
        db.collection("Helper").document(mainCaregory)
                .collection(mainCaregory)
                .add(helper).addOnSuccessListener {
                    helper.documentID = it.id

                }.await()
        return helper
    }
    suspend fun modify(mainCaregory: String,helper : Helper) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var result = false
        helper.documentID?.let {
            db.collection("Helper").document(mainCaregory)
                    .collection(mainCaregory)
                    .document(it).set(helper).addOnSuccessListener{
                        result = true
                    }.await()
        }
        return result
    }
    fun initSetList(map : HashMap<String,ArrayList<Helper>>,action: suspend () -> Unit){
        var db = FirebaseFirestore.getInstance()

        for(mainCategory in mainHelperCategoryList){
            db.collection("Helper").document(mainCategory)
                    .collection(mainCategory)
                    .whereArrayContains("aroundApt", User.getInstance().aptCode)
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .limit(5).get().addOnSuccessListener {
                        var result = ArrayList<Helper>()
                        for(document in it.documents){
                            var data = document.toObject(Helper::class.java)
                            data?.documentID = document.id
                            if (data != null) {
                                result.add(data)
                            }
                        }
                        map.put(mainCategory,result)
                        if(map.size== mainHelperCategoryList.size){
                            CoroutineScope(Dispatchers.Main).launch {
                                action.invoke()
                            }
                        }

                    }
        }
    }

    suspend fun getList(mainCaregory: String) : ArrayList<Helper> {
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Helper>()
        db.collection("Helper")
                .document(mainCaregory)
                .collection(mainCaregory)
                .whereArrayContains("aroundApt", User.getInstance().aptCode)
                .orderBy("timeStamp",Query.Direction.DESCENDING)
                .limit(50)
                .get().addOnSuccessListener {
                    for(document in it.documents){
                        var data = document.toObject(Helper::class.java)
                        data?.documentID = document.id
                        if (data != null) {
                            result.add(data)
                        }
                    }
                }.await()
        return result
    }

        fun upload(pathString : String , picturePathList : ArrayList<String>,helper : Helper,action :suspend ()-> Unit){
//        var picture = adapter.list.get(adapter.checkBox)
            var i = 0
            helper.images = ArrayList()
        var uploadedList = HashMap<Int,String>()
        for(picturePath in picturePathList){

            var result : String ?=null
            var storageRef : StorageReference = FirebaseStorage.getInstance().reference


            var file = Uri.fromFile(File(picturePath))

            var inputstream = FileInputStream(File(picturePath))

            val riversRef = storageRef.child(pathString+"/" + file.lastPathSegment)
            val number = i++
            val uploadTask = riversRef.putStream(inputstream)
            println("시작전")
//            uploadTask.addOnSuccessListener {
//                println("리스너~~~")
//                uploadedList.add(it.task.result.toString())
//                if(uploadedList.size==picturePathList.size){
//                    CoroutineScope(Dispatchers.Main).async {
//                        action.invoke()
//                    }
//                }
//            }
            uploadTask.continueWithTask { riversRef.downloadUrl }.addOnCompleteListener { task ->
                uploadedList.put(number, task.result.toString())
                println("실행중")
                println("순서 -> ${i}")
                if(uploadedList.size ==picturePathList.size){

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



