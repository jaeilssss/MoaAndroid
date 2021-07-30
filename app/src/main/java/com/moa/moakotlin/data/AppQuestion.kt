package com.moa.moakotlin.data

import com.google.firebase.Timestamp

class AppQuestion(
    var timeStamp : Timestamp = Timestamp.now(),
    var title : String = "",
    var url : String = ""
) {
}