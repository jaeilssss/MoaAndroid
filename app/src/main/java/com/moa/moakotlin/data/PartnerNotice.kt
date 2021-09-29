package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PartnerNotice(
        var aptCode : String ="",
        var content : String ="",
        var images : ArrayList<String> = ArrayList(),
        @field:JvmField
        var isPriority : Boolean =false,
        var timeStamp : Timestamp = Timestamp.now(),
        var title : String ="",
        var uid : String =""
): Parcelable {


}