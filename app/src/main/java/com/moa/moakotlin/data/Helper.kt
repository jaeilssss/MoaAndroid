package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Helper(
        var aroundApt : ArrayList<String> = ArrayList<String>(),
        var aptName : String="",
        var uid : String="",
        var title : String="",
        var mainCategory : String="",
        var images : ArrayList<String> = ArrayList<String>(),
        var content : String="",
        var timeStamp : Timestamp= Timestamp.now(),
        var hopeWage : String="",
        var documentID : String="",
        var aptCode : String ="",
        @field:JvmField
        var isNego : Boolean=false
): Parcelable {
}