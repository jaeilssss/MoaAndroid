package com.moa.moakotlin.ui.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Messenger
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.VoiceUser
import com.moa.moakotlin.firebase.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ForecdTerminationService() : Service() {

var documentID = ""
    var voiceUser = VoiceUser()


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
        val db = FirebaseFirestore.getInstance()
        db.collection("VoiceChatRoom")
                .document(documentID)
                .delete()
                .addOnSuccessListener { println("dd") }

//        CoroutineScope(Dispatchers.Main).launch {
//            println("dd")
//            db.collection("VoiceChatRoom")
//                    .document(documentID)
//                    .delete()
//                    .addOnSuccessListener { println("good...tlqkf ") }
//            println("오호")
//        }


    }
suspend fun controller(){
    println("...ㅇㅇㅈㄷㅈ")


    if(voiceUser?.role.equals("speaker")){
        decrementPeopleCount()
        decrementSpeakerCount()
        deleteVoiceUser()
        println("speaker")


    }else if(voiceUser?.role.equals("audience")){
        decrementPeopleCount()
        deleteRequestUser()
        deleteVoiceUser()
        println("audience")

    }else{
        deleteVoiceUser()
        deleteVoiceChatRoom()
        println("owner")

    }
}

   suspend fun decrementPeopleCount(){
       val db = FirebaseFirestore.getInstance()
        db.collection("VoiceChatRoom")
                .document(documentID)
                .update("peopleCount", FieldValue.increment(-1)).await()
    }
    suspend fun decrementSpeakerCount(){
        val db = FirebaseFirestore.getInstance()
        db.collection("VoiceChatRoom")
                .document(documentID)
                .update("speakersCount", FieldValue.increment(-1)).await()
    }
    suspend fun deleteRequestUser(){
        val db = FirebaseFirestore.getInstance()
        db.collection("VoiceChatRoom")
                .document(documentID)
                .collection("RequestUser")
                .document(voiceUser.uid)
                .delete().await()
    }
  suspend  fun deleteVoiceUser(){
      val db = FirebaseFirestore.getInstance()
        db.collection("VoiceChatRoom")
                .document(documentID)
                .collection("VoiceUser")
                .document(voiceUser.uid)
                .delete()
                .await()
    }


    // 코루틴으로 해보자 ..  !!


   suspend fun deleteVoiceChatRoom() : String{
       var test = ""
        println("dd...")
       val db = FirebaseFirestore.getInstance()
        db.collection("VoiceChatRoom")
                .document(documentID)
                .delete()
                .addOnSuccessListener {
                    println(".....")
                    test  = "good"
                }.addOnFailureListener {
                    println("fail")
                }.await()
return test
    }



}

