package com.moa.moakotlin.repository.push

import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.data.PushMessage
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList

class FcmRepository {

     fun sendPushMessage( message : PushMessage){
         val data = hashMapOf(
                 "title" to message.title,
                 "body" to message.body,
                 "token" to message.token
         )
       var functions = Firebase.functions("asia-northeast3")
        functions.getHttpsCallable("sendCloudMessageByToken").call(data)
    }


    fun sendPushMessageToNeighborhood( message : PushMessage){
        // function name 수정할것!!
        val data = hashMapOf(
                "title" to message.title,
                "body" to message.body,
                "aroundApt" to aptList.getInstance().aroundApt,
                "uid" to User.getInstance().uid
        )
        var functions = Firebase.functions("asia-northeast3")
        functions.getHttpsCallable("sendCloudMessageToNeighbor").call(data).addOnFailureListener {
            println("실패")
            println(it.message)
        }.addOnSuccessListener {
            println("...")
        }
    }
    fun sendPushMessageToMyApt( message : PushMessage){
        // function name 수정할것!!
        val data = hashMapOf(
                "title" to message.title,
                "body" to message.body,
                "aroundApt" to arrayListOf<String>(User.getInstance().aptCode),
                "uid" to User.getInstance().uid
        )
        var functions = Firebase.functions("asia-northeast3")
        functions.getHttpsCallable("sendCloudMessageToNeighbor").call(data).addOnFailureListener {
            println("실패")
            println(it.message)
        }.addOnSuccessListener {
            println("...")
        }
    }

    fun sendPushMessageToMoa( message : PushMessage){

        // function name 수정할것!!

        val data = hashMapOf(
                "title" to message.title,
                "body" to message.body,
                "topic" to "MOA"
        )
        var functions = Firebase.functions("asia-northeast3")
        functions.getHttpsCallable("sendCloudMessageByTopic").call(data)
    }
}