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
    var isAgreeChattingAlarm : Boolean = true
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
        instance?.name= user.name
        instance?.uid = user.uid
        instance?.birthday = user.birthday
        instance?.isMan = user.isMan
        instance?.aptCode = user.aptCode
        instance?.certificationStatus = user.certificationStatus
        instance?.nickName = user.nickName
        instance?.phoneNumber = user.phoneNumber
        instance?.profileImage = user.profileImage
        instance?.address = user.address
        instance?.signUpDate = user.signUpDate
        instance?.aptName = user.aptName
        instance?.introduction = user.introduction
    }
        fun deleteUser(){
            instance = null
        }
    }
}