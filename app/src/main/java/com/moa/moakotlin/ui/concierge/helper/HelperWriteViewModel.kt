package com.moa.moakotlin.ui.concierge.helper

import android.net.Uri
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.rpc.Help
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.Sitter
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.imagePicker.ImagePickerRepository
import java.io.File
import java.io.FileInputStream

class HelperWriteViewModel(navController: NavController):BaseViewModel(navController) {

    var title = ObservableField<String>("")
    var type = ObservableField<String>("")
    var wage = ObservableField<String>("")
    var content = ObservableField<String>("")
    lateinit var images : ArrayList<String>
    var pictureCount = ObservableField<String>("0")
    @field:JvmField
    var isNego = ObservableField<Boolean>(false)
    var imagelist : ArrayList<String> ?=null
    var list = ArrayList<String>()
    var i = 0
    var mainCategory = ""

    suspend fun submit(list : ArrayList<String>) : Boolean{
        var repository = HelperRepository()
        var result = false
        var helper = Helper(User.getInstance().aptCode,User.getInstance().aptName,User.getInstance().uid,title.get()!!,mainCategory,null,
                content.get()!!, Timestamp.now(),wage.get()!!,"",isNego.get()!!)
        if(list.size==0){
             result  = repository.submit(helper)
        }else{
            var uploader = ImagePickerRepository()
            var images = ArrayList<String>()
            for(i in 0 until list.size){
                images.add(uploader.upload("helperImages/",list.get(i))!!)
                helper.images = images
            }
            result = repository.submit(helper)
        }
        return result
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