package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
class Magazine(
    var thumbnail : String ="",
    var timestamp: Timestamp = Timestamp.now(),
    var title : String = "",
    var url : String =""
): Parcelable {
}