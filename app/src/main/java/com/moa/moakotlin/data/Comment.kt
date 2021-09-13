package com.moa.moakotlin.data

import com.google.firebase.Timestamp

data class Comment(
        var content : String = "",
        var timeStamp : Timestamp = Timestamp.now(),
        var uid : String ="",
        @field:JvmField
        var isAuthorPartner : Boolean =  false
)
