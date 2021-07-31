package com.moa.moakotlin.data

import com.google.firebase.Timestamp

class DropOut(
        var address : String ="",
        var birthday : String="",
        var isMan : Boolean =false,
        var reason : String ="",
        var timeStamp : Timestamp = Timestamp.now()
) {
}