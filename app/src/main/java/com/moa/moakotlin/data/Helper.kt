package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Helper(
        var aptCode : String="",
        var aptName : String="",
        var uid : String="",
        var title : String="",
        var mainCategory : String="",
        var images : ArrayList<String>?=null,
        var content : String="",
        var timeStamp : Timestamp= Timestamp.now(),
        var hopeWage : String="",
        var documentID : String="",
        @field:JvmField
        var isNego : Boolean=false
): Parcelable {
}