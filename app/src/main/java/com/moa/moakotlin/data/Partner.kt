package com.moa.moakotlin.data

import com.google.firebase.Timestamp

data class Partner(

        var email: String ="" ,
        var name : String = "",
        var nickName : String = "",
        var phoneNumber : String ="",
        var profileImage : String ="",
        var signUpDate : Timestamp = Timestamp.now(),
        var uid : String = "",
        var aptCode : String ="",
        var aptName : String =""
)
