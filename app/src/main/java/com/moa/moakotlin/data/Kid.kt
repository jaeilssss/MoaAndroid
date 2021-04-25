package com.moa.moakotlin.data

import android.os.Parcel
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates
@Parcelize

data class Kid(
        var uid: String? ="",
        var lifeCycle: String = "",
        var kidCount: Int = 0,
        var hopeDate: String = "",
        var isRegular:Boolean = false,
        var title: String = "",
        var content: String = "",
        var wage: String = "",
        @field:JvmField
        var isNegotiable:Boolean =false,
        var aptName: String = "",
        var aptCode : String = "",
        var likeCount: Int =0,
        var hireStatus: String="채용중",
        var images:ArrayList<String>? =null,
        var timeStamp: Timestamp = Timestamp.now(),
        var documentID :String =""
// apt code 있어야 할듯
): Parcelable{

}

