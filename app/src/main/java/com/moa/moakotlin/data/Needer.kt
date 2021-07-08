package com.moa.moakotlin.data

import com.google.firebase.Timestamp
import java.util.ArrayList

data class Needer(
        var title : String = "",
        var mainCategory : String = "",
        var subCategory : String="",
        var hopeDate : String="",
        @field:JvmField
        var isNego : Boolean=false,
        var timeStamp: Timestamp = Timestamp.now(),
        var images : ArrayList<String> ?= ArrayList<String>(),
        var content : String = "",
        var hopeWage : String="",
        var documentID : String ?="",
        var hireStatus : String = "",
        var uid : String="",
        var aptCode : String="",
        var aptName : String="",
        var aroundApt: ArrayList<String> =ArrayList<String>()
        ){
}