package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contract(
        var companyName : String= "",
        var contractStartDate : Timestamp = Timestamp.now(),
        var contractEndDate : Timestamp = Timestamp.now(),
        var price : String = "",
        var aptCode : String ="",
        var images: ArrayList<String> = ArrayList(),
        var documentId : String ="",
        var title : String = ""
): Parcelable {
}