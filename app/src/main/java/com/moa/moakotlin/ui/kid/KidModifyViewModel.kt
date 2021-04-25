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
import com.moa.moakotlin.data.Kid
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.User
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime

class KidModifyViewModel (navController: NavController) : BaseViewModel(navController){
    var now = LocalDateTime.now()
    var title = ObservableField<String>("")
    var type = ObservableField<String>("")
    var count = ObservableField<String>("")
    var wage = ObservableField<String>("")
    var year= ObservableField<Int>(now.year)
    var month = ObservableField<Int>(now.monthValue)
    var day = ObservableField<Int>(now.dayOfMonth)
    var content = ObservableField<String>("")
    var imagelist = ArrayList<String>()

    var preUploadImage = ArrayList<String>()
    lateinit var kid: Kid
    fun initDataSetting(kid :Kid){
        this.kid = kid
        title.set(kid.title)
        type.set(kid.lifeCycle)
        count.set(kid.kidCount.toString())
        wage.set(kid.wage)

        var dateList = kid.hopeDate.split(" ")

        year.set(dateList[0].replace("년","").toInt())
        day.set(dateList[1].replace("월","").toInt())
        month.set(dateList[2].replace("일","").toInt())

        content.set(kid.content)


    }

    fun submit(imageList : ArrayList<String>){
        kid.title = title.get().toString()

        kid.lifeCycle = type.get().toString()

        kid.kidCount = count.get()?.toInt()!!

        kid.wage = wage.get().toString()

        kid.hopeDate = "${year.get()}년 ${month.get()}월 ${day.get()}일 "

        kid.content = content.get().toString()
        preUploadImage = imageList
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

        val riversRef = storageRef.child("kidImage/" + file.lastPathSegment)

        val uploadTask = riversRef.putStream(inputstream)

        uploadTask.continueWithTask { riversRef.downloadUrl }.addOnCompleteListener { task ->

            imagelist.add(task.result.toString())
            if(imagelist.size==preUploadImage.size){
                dbConnection()
            }else{
                uploadImageList(preUploadImage.get(imagelist.size-1))
            }


        }
    }

    fun dbConnection(){
        var user = User.getInstance()
        kid.timeStamp = Timestamp.now()
        kid.aptName = user.aptCode
        kid.uid = user.uid
        kid.wage = wage.get().toString()
        kid.title =title.get().toString()
        kid.lifeCycle = type.get().toString()
        kid.kidCount = count.get()?.replace("명","")?.toInt() ?: -1
        kid.content = content.get().toString()
        kid.content = "복원! "
        kid.hopeDate = "${year.get()}년 ${month.get()}월 ${day.get()}일 "
        kid.images = imagelist

        var db = FirebaseFirestore.getInstance()
        db.collection("Kid")
            .document(kid.documentID)
            .set(kid).addOnSuccessListener {
                var bundle  = Bundle()
                Picture.deleteInstance()
                bundle.putParcelable("kid",kid)
                navController.navigate(R.id.action_kidModifyFragment_to_kidReadFragment,bundle)
            }
    }
}