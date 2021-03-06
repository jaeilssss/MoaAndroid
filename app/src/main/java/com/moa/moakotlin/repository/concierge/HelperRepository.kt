package com.moa.moakotlin.repository.concierge

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
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
        db.collection("Helper")
            .document(mainCaregory)
                .collection("HelperContent")
                .add(helper).addOnSuccessListener {
                    helper.documentID = it.id

                }.await()
        return helper
    }
    suspend fun modify(mainCategory: String,helper : Helper) : Helper{
        var db = FirebaseFirestore.getInstance()

        db.collection("Helper")
                .document(mainCategory)
                .collection("HelperContent")
                .document(helper.documentID)
                .set(helper).addOnCompleteListener {
                }.await()
        return helper
    }
  suspend fun initSetList(mainCategory: String) : ArrayList<Helper>{
        var db = FirebaseFirestore.getInstance()
      var result = ArrayList<Helper>()
            db.collection("Helper").document(mainCategory)
                    .collection("HelperContent")
                    .whereArrayContainsAny("aroundApt", arrayListOf(User.getInstance().aptCode,"All"))
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .limit(5).get().addOnSuccessListener {
                        if(!it.isEmpty){
                            for(document in it.documents){
                                var data = document.toObject(Helper::class.java)
                                data?.documentID = document.id
                                if (data != null) {
                                    result.add(data)
                                }
                            }
                        }
                    }.await()

      return result

    }

    suspend fun getList(mainCaregory: String) : ArrayList<Helper> {
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Helper>()
        db.collection("Helper")
                .document(mainCaregory)
                .collection("HelperContent")
                .whereArrayContainsAny("aroundApt", arrayListOf(User.getInstance().aptCode,"All"))
                .orderBy("timeStamp",Query.Direction.DESCENDING)
                .limit(20)
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

        fun upload(num : Int, pathString : String , picturePathList : ArrayList<String>,helper : Helper,action :suspend ()-> Unit){
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

                    if(uploadedList.size==picturePathList.size){

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


   suspend fun delete(mainCaregory: String,documentId : String) : Boolean{
       var db = FirebaseFirestore.getInstance()
       var check = false
       db.collection("Helper")
               .document(mainCaregory)
               .collection("HelperContent")
               .document(documentId)
               .delete()
               .addOnCompleteListener {
                   if(it.isSuccessful){
                       check=true
                   }
               }
               .await()
       return check
    }

    suspend fun getNextData(mainCategory: String,timeStamp : Timestamp) : ArrayList<Helper>{
        var db = FirebaseFirestore.getInstance()
        var result = ArrayList<Helper>()
        db.collection("Helper")
            .document(mainCategory)
                .collection("HelperContent")
                .whereArrayContainsAny("aroundApt", arrayListOf(User.getInstance().aptCode,"All"))
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .startAfter(timeStamp)
            .limit(20)
            .get()
            .addOnSuccessListener {
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

   suspend fun getMyHelperData() : ArrayList<Helper>{
        var db = FirebaseFirestore.getInstance()
        var list = ArrayList<Helper>()
        db.collectionGroup("HelperContent")
            .whereEqualTo("uid",User.getInstance().uid)
                .whereArrayContainsAny("aroundApt", arrayListOf(User.getInstance().aptCode))
            .orderBy("timeStamp",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for(document in it.documents){
                    var newHelper = document.toObject(Helper::class.java)
                    newHelper?.documentID = document.id
                    if (newHelper != null) {
                        list.add(newHelper)
                    }
                }
            }.await()

       return list
    }

    fun modifyConciergeList() {
        var db = FirebaseFirestore.getInstance()
        db.collectionGroup("HelperContent")
            .whereEqualTo("uid",User.getInstance().uid)
            .get()
            .addOnSuccessListener {
                for(document in it.documents){
                    var helper = document.toObject(Helper::class.java)
                    helper?.documentID = document.id
                    modifyHelperUid(helper!!)
                }
            }
    }

    fun modifyHelperUid(helper : Helper){
        var db = FirebaseFirestore.getInstance()

        db.collection("Helper")
            .document("HelperContent")
            .collection(helper.mainCategory)
            .document(helper.documentID)
            .update("uid",User.getInstance().uid)
    }

    fun deleteGetMyHelper(){
        var db = FirebaseFirestore.getInstance()
        var list = ArrayList<Helper>()
        db.collectionGroup("HelperContent")
                .whereEqualTo("uid",User.getInstance().uid)
                .orderBy("timeStamp",Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    for(document in it.documents){
                        CoroutineScope(Dispatchers.Default).async {
                            var newHelper = document.toObject(Helper::class.java)
                            newHelper!!.documentID = document.id
                            delete(newHelper!!.mainCategory,newHelper!!.documentID)
                        }

                    }
                }
    }
}



