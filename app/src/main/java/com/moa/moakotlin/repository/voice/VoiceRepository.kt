package com.moa.moakotlin.repository.voice

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

class VoiceRepository  {
    private lateinit var functions: FirebaseFunctions
     fun generateToken(channelName: String,uid : Int): Task<String?> {
        // Create the arguments to the callable function.
         functions = Firebase.functions("asia-northeast3")
        val data = hashMapOf(
            "channelName" to channelName,
            "uid" to uid,
            "expireTime" to 3000
        )
        return functions
            .getHttpsCallable("generateToken")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as Map<String, String>
                result.get("token")
            }
    }

    fun makeVoiceRoom(){

    }
}