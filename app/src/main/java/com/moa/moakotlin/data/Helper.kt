package com.moa.moakotlin.data

import com.google.firebase.Timestamp

data class Helper(
        var aptCode : String,
        var aptName : String,
        var uid : String,
        var title : String ,
        var firstType : String,
        var images : ArrayList<String>?=null,
        var content : String,
        var timeStamp : Timestamp,
        var wage : String,
        var documentId : String,
        var isNego : Boolean
){
}