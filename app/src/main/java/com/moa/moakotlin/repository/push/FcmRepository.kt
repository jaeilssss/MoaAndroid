package com.moa.moakotlin.repository.push

import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.data.PushMessage

class FcmRepository {

     fun sendPushMessage( message : PushMessage){
         val data = hashMapOf(
                 "title" to message.title,
                 "body" to message.body,
                 "token" to message.token
         )
       var functions = Firebase.functions("asia-northeast3")
        functions.getHttpsCallable("sendCloudMessage").call(data)
    }
}