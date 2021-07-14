package com.moa.moakotlin.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class VoiceUser(
        var nickName : String ="",
        var phoneNumber : String="",
        var profileImage: String="",
        var role : String ="",
        var uid : String =""
): Parcelable {
}