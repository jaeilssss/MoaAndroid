package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize


@Parcelize
 data class Notification(
        var documentID : String = "",
        var image : String ="",
        @ServerTimestamp
        var timeStamp : Timestamp  = Timestamp.now(),
        var title : String =""
): Parcelable {
}