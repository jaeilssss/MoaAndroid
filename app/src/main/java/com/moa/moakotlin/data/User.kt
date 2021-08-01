package com.moa.moakotlin.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import java.sql.Time
@Parcelize
data class User(

    var uid : String = "",
    var name : String="",
    var birthday : String = "",
    @field:JvmField
    var isMan : Boolean = false,
    var aptCode : String="",
    var certificationStatus : String ="미인증",
    var nickName :  String = "",
    var phoneNumber : String = "",
    var profileImage :String ="",
    var address : String ="",
    var aptName : String ="",
    var signUpDate : Timestamp = Timestamp.now(),
    var introduction :String = "",
    @field:JvmField
    var isAgreeMarketing : Boolean = false,
    @field:JvmField
    var isAgreeEventAlarm : Boolean = true,
    @field:JvmField
    var isAgreeChattingAlarm : Boolean = true,
    var pushToken : String="",
    var phoneUid : String=""

): Parcelable{
    companion object{
        // 자기변수 선언하기
        @Volatile private var instance : User ? = null

        fun getInstance() : User = instance ?: synchronized(this){
            instance ?: User().also {
                instance = it
            }
        }
    fun setInstance(user : User){
        getInstance()?.name= user.name
        getInstance()?.uid = user.uid
        getInstance()?.birthday = user.birthday
        getInstance()?.isMan = user.isMan
        getInstance()?.aptCode = user.aptCode
        getInstance()?.certificationStatus = user.certificationStatus
        getInstance()?.nickName = user.nickName
        getInstance()?.phoneNumber = user.phoneNumber
        getInstance()?.profileImage = user.profileImage
        getInstance()?.address = user.address
        getInstance()?.signUpDate = user.signUpDate
        getInstance()?.aptName = user.aptName
        getInstance()?.introduction = user.introduction
        getInstance()?.isAgreeMarketing = user.isAgreeMarketing
        getInstance()?.isAgreeEventAlarm = user.isAgreeEventAlarm
        getInstance()?.isAgreeChattingAlarm = user.isAgreeChattingAlarm
        getInstance()?.pushToken = user.pushToken
        getInstance()?.phoneUid = user.phoneUid
    }
        fun deleteUser(){
            instance = null
        }
    }
}