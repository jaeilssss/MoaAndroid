package com.moa.moakotlin.data

import com.google.firebase.Timestamp

class ChattingRoom() {

     var latestMessage :String ? =null

    lateinit var timeStamp: Timestamp

//    lateinit var list : ArrayList<String>

    lateinit var opponentUid : String

    @field:JvmField
    var isRead = false

}