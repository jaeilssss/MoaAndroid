package com.moa.moakotlin.data

import com.google.firebase.Timestamp


data class Review(

    var review : String = "",
    var timeStamp: Timestamp,
    var uid: String = ""
){

}