package com.moa.moakotlin.ui.concierge.helper

import android.net.Uri
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.Sitter
import com.moa.moakotlin.data.User
import java.io.File
import java.io.FileInputStream

class HelperModifyViewModel(navController: NavController) : BaseViewModel(navController) {
    var title = ObservableField<String>("")
    var type = ObservableField<String>("")
    var wage = ObservableField<String>("")
    var content = ObservableField<String>("")
    lateinit var images : ArrayList<String>
    var pictureCount = ObservableField<String>("0")
    @field:JvmField
    var isNego = ObservableField<Boolean>(false)
    var imagelist : ArrayList<String>  ?=null
    var list = ArrayList<String>()
    var preUploadImage = ArrayList<String>()
    lateinit var sitter : Sitter

    fun check() : String{
        if(title.get()?.length ==0){
            return "제목을 입력해주세요"
        }else if(type.get()?.length==0){
            return "시터 유형을 입력해주세요"
        }else if(wage.get()!!.length==0){
            return "희망 시급을 입력해주세요"
        }else if(content.get()!!.length==0){
            return "내용을 입력해주세요"
        }else{
            return "success"
        }
    }
    fun submit(imageList : ArrayList<String>){

        var user = User.getInstance()
        sitter.title = title.get().toString()

        sitter.type = type.get().toString()


        sitter.wage = wage.get().toString()

        sitter.content = content.get().toString()

        sitter.isNegotiable = isNego.get()!!


        sitter.timeStamp = Timestamp.now()
        sitter.aptName = user.aptCode
        sitter.uid = user.uid

        if(preUploadImage.size!=0){
            uploadImageList(preUploadImage.get(0))
        }else{
            dbConnection()
        }

    }
    fun uploadImageList(picture : String){

        var storageRef = FirebaseStorage.getInstance().getReference()

        var file = Uri.fromFile(File(picture))

        var inputstream = FileInputStream(File(picture))

        val riversRef = storageRef.child("sitterImage/" + file.lastPathSegment)

        val uploadTask = riversRef.putStream(inputstream)

        uploadTask.continueWithTask { riversRef.downloadUrl }.addOnCompleteListener { task ->

            imagelist?.add(task.result.toString())
            if(imagelist?.size==preUploadImage.size){
                sitter.images = imagelist
                dbConnection()
            }else{
                uploadImageList(preUploadImage.get(imagelist!!.size-1))
            }


        }
    }

    fun dbConnection(){

        println("sitter id -> ${sitter.documentID}")
        var db = FirebaseFirestore.getInstance()
        db.collection("Sitter")
            .document(sitter.documentID)
            .set(sitter).addOnSuccessListener {
                var bundle  = Bundle()
                Picture.deleteInstance()
                bundle.putParcelable("sitter",sitter)
                navController.navigate(R.id.action_sitterModifyFragment_to_sitterReadFragment,bundle)

            }
    }
}