package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contract(
        var companyName : String= "",
        var contractStartDate : Timestamp = Timestamp.now(),
        var contractEndDate : Timestamp = Timestamp.now(),
        var price : Int = 0,
        var aptCode : String ="",
        var images: ArrayList<String> = ArrayList(),
        var documentId : String ="",
        var title : String = "",
        var contractInfo :String ="",
        var uid : String ="",
        var transaction : String ="",
        var status : String =""
): Parcelable {
}