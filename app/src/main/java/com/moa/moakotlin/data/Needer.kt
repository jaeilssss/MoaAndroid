package com.moa.moakotlin.data

import com.google.firebase.Timestamp
import java.util.ArrayList

data class Needer(
        var title : String,
        var firstType : String,
        var secondType : String,
        var hopeDate : String,
        @field:JvmField
        var isNego : Boolean,
        var timeStamp: Timestamp = Timestamp.now(),
        var images : ArrayList<String> ?=null,
        var content : String,
        var wage : String,
        var documentID : String ?=null
        ){

}