package com.moa.moakotlin.data

import com.google.firebase.Timestamp

class Report(
    var detailReason : String = "",
    var documentID : String = "",
    var reason : String = "",
    var uid : String ="",
    var timeStamp : Timestamp = Timestamp.now()

) {
}