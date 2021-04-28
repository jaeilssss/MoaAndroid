package com.moa.moakotlin.data

import com.google.firebase.Timestamp

data class Helper(
        var aptCode : String,
        var aptName : String,
        var uid : String,
        var title : String ,
        var mainCategory : String,
        var images : ArrayList<String>?=null,
        var content : String,
        var timeStamp : Timestamp,
        var hopeWage : String,
        var documentID : String,
        @field:JvmField
        var isNego : Boolean
){
}