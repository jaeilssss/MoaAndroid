package com.moa.moakotlin.data

import com.google.firebase.Timestamp


data class Review(

    var review : String = "",
    var timeStamp: Timestamp= Timestamp.now(),
    var uid: String = "",
    var helperUid :String =""
){

}