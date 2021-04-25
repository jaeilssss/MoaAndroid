package com.moa.moakotlin.ui.sitter

import android.net.Uri
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.Kid
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.Sitter
import com.moa.moakotlin.data.User
import java.io.File
import java.io.FileInputStream

class SitterWriteViewModel(navController: NavController):BaseViewModel(navController) {

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
    var i = 0
    fun submit(list :ArrayList<String>){
        i=0
        this.list = list
        var bundle = Bundle()
        if(list.size==0){
            writeSitter()
        }else{
            imagelist = ArrayList<String>()
            uploadImageList(list.get(i++),list.size)
        }

    }

    fun uploadImageList(picture : String, size:Int){

        var storageRef = FirebaseStorage.getInstance().getReference()

        var file = Uri.fromFile(File(picture))

        var inputstream = FileInputStream(File(picture))

        val riversRef = storageRef.child("sitterImage/" + file.lastPathSegment)

        val uploadTask = riversRef.putStream(inputstream)

        uploadTask.continueWithTask { riversRef.downloadUrl }.addOnCompleteListener { task ->

            imagelist!!.add(task.result.toString())
            if(imagelist!!.size==size){
             var sitter = content.get()?.let {
                 isNego.get()?.let { it1 ->
                     title.get()?.let { it2 ->
                         wage.get()?.let { it3 ->
                             type.get()?.let { it4 ->
                                 Sitter(User.getInstance().aptCode,User.getInstance().aptName, it,"","심사중",imagelist, it4
                                         , it1, Timestamp.now(), it2,User.getInstance().uid, it3)
                             }
                         }
                     }
                 }
             }
                var db = FirebaseFirestore.getInstance()
                if (sitter != null) {
                    db.collection("Sitter")
                            .add(sitter).addOnSuccessListener {
                                var bundle = Bundle()
                                bundle.putParcelable("sitter",sitter)
                                Picture.deleteInstance()
                                imagelist =null

                            }
                }
            }else{
                uploadImageList(list.get(i++),list.size)
            }
        }
    }

    fun writeSitter(){
        var sitter = content.get()?.let {
            isNego.get()?.let { it1 ->
                title.get()?.let { it2 ->
                    wage.get()?.let { it3 ->
                        type.get()?.let { it4 ->
                            Sitter(User.getInstance().aptCode,User.getInstance().aptName, it,"","심사중",imagelist, it4
                                    , it1, Timestamp.now(), it2,User.getInstance().uid, it3)
                        }
                    }
                }
            }
        }
        var db = FirebaseFirestore.getInstance()
        if (sitter != null) {
            db.collection("Sitter")
                    .add(sitter).addOnSuccessListener {
                        var bundle = Bundle()
                        bundle.putParcelable("sitter",sitter)
                        Picture.deleteInstance()

                    }
        }
    }
}