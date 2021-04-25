package com.moa.moakotlin.ui.kid

import android.net.Uri
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.*
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime
import kotlin.collections.ArrayList

class KidWritePageViewModel(navController: NavController) :BaseViewModel(navController){


    var now = LocalDateTime.now()
    var title = ObservableField<String>("")
    var type = ObservableField<String>("")
    var count = ObservableField<String>("")
    var wage = ObservableField<String>("")
    var year= ObservableField<Int>(now.year)
    var month = ObservableField<Int>(now.monthValue)
    var day = ObservableField<Int>(now.dayOfMonth)
    var content = ObservableField<String>("")
    var imagelist : ArrayList<String> ?=null
    var list = ArrayList<String>()

    var isNego = ObservableField<Boolean>(false)
    var i   = 0
    var isRe = ObservableField<Boolean>(false)
    fun submit(list :ArrayList<String>){
        i=0
        this.list = list
        var bundle = Bundle()
        if(list.size==0){
            writeKid()
        }else{
            imagelist = ArrayList<String>()
            uploadImageList(list.get(i++),list.size)
        }
    }


    fun uploadImageList(picture : String, size:Int){

        var storageRef = FirebaseStorage.getInstance().getReference()

        var file = Uri.fromFile(File(picture))

        var inputstream = FileInputStream(File(picture))

        val riversRef = storageRef.child("kidImage/" + file.lastPathSegment)

        val uploadTask = riversRef.putStream(inputstream)

        uploadTask.continueWithTask { riversRef.downloadUrl }.addOnCompleteListener { task ->

            imagelist!!.add(task.result.toString())
            if(imagelist!!.size==size){
                val kid = Kid()
                var user = User.getInstance()
                kid.timeStamp = Timestamp.now()
                kid.aptName = user.aptCode
                kid.uid = user.uid
                kid.wage = wage.get().toString()
                kid.title =title.get().toString()
                kid.lifeCycle = type.get().toString()
                kid.kidCount = count.get()?.replace("명","")?.replace("이상","")?.toInt() ?: -1
                kid.content = content.get().toString()
                kid.hopeDate = "${year.get()}년 ${month.get()}월 ${day.get()}일 "
                kid.images = imagelist
                kid.aptCode = user.aptCode
                var db = FirebaseFirestore.getInstance()
                db.collection("Kid")
                    .add(kid).addOnSuccessListener {
                            var bundle = Bundle()
                            bundle.putParcelable("kid",kid)
                            Picture.deleteInstance()
                        imagelist =null
                        navController.navigate(R.id.action_kidWritePageFragment_to_kidReadFragment,bundle)
                    }
            }else{
                uploadImageList(list.get(i++),list.size)
            }
        }
    }

    fun writeKid(){
        val kid = Kid()
        var user = User.getInstance()
        kid.timeStamp = Timestamp.now()
        kid.aptName = user.aptCode
        kid.uid = user.uid
        kid.wage = wage.get().toString()
        kid.title =title.get().toString()
        kid.lifeCycle = type.get().toString()
        kid.kidCount = count.get()?.replace("명","")?.replace("이상","")?.toInt() ?: -1
        kid.content = content.get().toString()
        kid.hopeDate = "${year.get()}년 ${month.get()}월 ${day.get()}일 "
        kid.aptCode = user.aptCode
        var db = FirebaseFirestore.getInstance()
        db.collection("Kid")
                .add(kid).addOnSuccessListener {
                    var bundle = Bundle()
                    bundle.putParcelable("kid",kid)
                    Picture.deleteInstance()
                    navController.navigate(R.id.action_kidWritePageFragment_to_kidReadFragment,bundle)
                }
    }
}