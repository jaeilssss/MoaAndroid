package com.moa.moakotlin.ui.concierge.needer

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
import com.moa.moakotlin.repository.concierge.NeederRepository
import com.moa.moakotlin.repository.imagePicker.ImagePickerRepository
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime
import kotlin.collections.ArrayList

class NeederWritePageViewModel(navController: NavController) :BaseViewModel(navController){


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
    var mainCategory = ""
    var subCategory = ""
    var isNego = ObservableField<Boolean>(false)
    var i   = 0
    var isRe = ObservableField<Boolean>(false)

   suspend fun submit(list : ArrayList<String>){
        var repository = NeederRepository()
       var result = false
       var hopeDate = "${year.get()}년 ${month.get()}월 ${day.get()}일"
       var needer = Needer(title.get()!!,mainCategory,subCategory,hopeDate,isNego.get()!!, Timestamp.now(),null,content.get()!!,wage.get()!!,
               "","채용중",User.getInstance().uid,User.getInstance().aptCode,User.getInstance().aptName)
            if(list.size==0){
                 result = repository.submit(mainCategory,needer)
            }else{
                var uploader = ImagePickerRepository()
                var images = ArrayList<String>()
                for(i in 0 until list.size){
                    images.add(uploader.upload("neederImages/",list.get(i))!!)
                    needer.images = images
                }
                result = repository.submit(mainCategory,needer)
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