package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
class Banner(
    var image : String ="",
    var timeStamp : Timestamp = Timestamp.now(),
    var type : String = "",
    var url : String = "",
    var order : Int = 0
) : Parcelable {
}