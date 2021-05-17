package com.moa.moakotlin.data

import com.google.firebase.Timestamp

data class VoiceChatRoom(
    var documentID : String ?="",
    var name : String ,
    var range : String,
    var timeStamp : Timestamp,
    var token : String,
    var topic : String){
}