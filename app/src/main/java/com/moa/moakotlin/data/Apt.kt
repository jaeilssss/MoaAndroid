package com.moa.moakotlin.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Apt(
        var address : String="",
        var aptCode : String="",
        var aptName : String="",
        var aroundApt : ArrayList<String> = ArrayList(),
        var doroJuso : String="",
        var lat : Double=0.0,
        var lon : Double=0.0
): Parcelable
