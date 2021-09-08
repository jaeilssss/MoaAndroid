package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Complaint(
        var category : String ="",
        var content : String ="",
        var images : ArrayList<String> = ArrayList(),
        @field:JvmField
        var isPrivate : Boolean = false,
        var timeStamp : Timestamp = Timestamp.now(),
        var title : String = "",
        var uid :  String = "",
        var status : String = ""
                // requested  , inprogress , completed

): Parcelable
