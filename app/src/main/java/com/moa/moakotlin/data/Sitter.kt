package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sitter(
        var aptCode : String  = "",
        var aptName : String = "",
        var content : String ="",
        var documentID : String ="",
        var status : String ="",
        var images : ArrayList<String> ?=null,
        var type : String ="",
        @field:JvmField
        var isNegotiable:Boolean =false,
        var timeStamp: Timestamp = Timestamp.now(),
        var title : String ="",
        var uid : String = "",
        var wage : String=""
): Parcelable {

}