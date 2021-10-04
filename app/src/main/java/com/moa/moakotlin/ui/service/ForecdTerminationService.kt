package com.moa.moakotlin.ui.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Messenger
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.VoiceUser
import com.moa.moakotlin.firebase.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ForecdTerminationService() : Service() {

var documentID = ""
    var voiceUser = VoiceUser()
val db = FirebaseFirestore.getInstance()

    override fun onBind(p0: Intent?): IBinder? {

        return null
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return Service.START_STICKY
        else setData(intent)
        return super.onStartCommand(intent, flags, startId);
    }

    fun setData(intent: Intent?){
        documentID = intent?.getStringExtra("documentID").toString()
        voiceUser  = intent?.getParcelableExtra<VoiceUser>("voiceUser")!!

    }

    override fun onTaskRemoved(rootIntent: Intent?) {

        Thread.sleep(5000)
//        super.onTaskRemoved(rootIntent)
        stopSelf()

    }



}

