package com.moa.moakotlin.data

import com.google.firebase.Timestamp
import java.util.ArrayList

data class Needer(
        var title : String,
        var mainCategory : String,
        var subCategory : String,
        var hopeDate : String,
        @field:JvmField
        var isNego : Boolean,
        var timeStamp: Timestamp = Timestamp.now(),
        var images : ArrayList<String> ?=null,
        var content : String,
        var hopeWage : String,
        var documentID : String ?=null,
        var hireStatus : String,
        var uid : String,
        var aptCode : String,
        var aptName : String
        ){

}