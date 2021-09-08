package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PartnerApart(
    var aptCode : String="",
    var aptName : String="",
    var completed : Int = 0,
    var contractEnd : Timestamp = Timestamp.now(),
    var contractStart : Timestamp =Timestamp.now(),
    var inProgress : Int = 0,
    var phoneNumber : String ="",
    var requested: Int = 0,
    var uid : String = ""
): Parcelable
