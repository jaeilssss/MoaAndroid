package com.moa.moakotlin.data

import com.google.firebase.Timestamp
import com.moa.moakotlin.base.BaseModel

data class Comment (
        var content : String = "",
        var timeStamp : Timestamp = Timestamp.now(),
        var uid : String ="",
        @field:JvmField
        var isAuthorPartner : Boolean =  false

): BaseModel()
