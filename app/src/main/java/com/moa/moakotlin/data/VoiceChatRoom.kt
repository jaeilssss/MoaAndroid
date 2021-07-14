package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
@Parcelize
data class VoiceChatRoom(
        var documentID : String ="",
        var name : String ="",
        var range : String="",
        var timeStamp : Timestamp= Timestamp.now(),
        var nickName : String="",
        var owner : String="",
        var peopleCount : Int = 1,
        var speakersCount :Int  = 1,

        var image : String ="",

        var aroundApt : ArrayList<String> = ArrayList(),
        var topic : String="") : Parcelable {
}