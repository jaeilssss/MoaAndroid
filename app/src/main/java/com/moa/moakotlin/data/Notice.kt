package com.moa.moakotlin.data

import com.google.firebase.Timestamp

class Notice(

        var body : String = "",
        var id : String ="",
        var timeStamp : Timestamp = Timestamp.now(),
        var url : String =""
){
}