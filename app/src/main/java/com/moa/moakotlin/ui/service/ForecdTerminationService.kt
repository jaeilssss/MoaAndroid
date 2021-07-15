package com.moa.moakotlin.ui.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.VoiceUser

class ForecdTerminationService(var documentID:String , var uid : String) : Service() {
    var db = FirebaseFirestore.getInstance()
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }



    override fun onTaskRemoved(rootIntent: Intent?) {



        var db = FirebaseFirestore.getInstance()
        db.collection("VoiceChatRoom")
                .document(documentID)
                .collection("VoiceUser")
                .document(uid)
                .get()
                .addOnSuccessListener {
                    var voiceUser = it.toObject(VoiceUser::class.java)
                    if(voiceUser?.role.equals("speaker")){
                        decrementPeopleCount()
                        decrementSpeakerCount()
                        deleteVoiceUser()
                        stopSelf()
                    }else if(voiceUser?.role.equals("audience")){
                        decrementPeopleCount()
                        deleteRequestUser()
                        deleteVoiceUser()
                        stopSelf()
                    }else{
                        deleteVoiceUser()
                        deleteVoiceChatRoom()
                        stopSelf()
                    }
                }



    }
    fun decrementPeopleCount(){
        db.collection("VoiceChatRoom")
                .document(documentID)
                .update("peopleCount", FieldValue.increment(-1))
    }
    fun decrementSpeakerCount(){
        db.collection("VoiceChatRoom")
                .document(documentID)
                .update("speakersCount",FieldValue.increment(-1))
    }
    fun deleteRequestUser(){
        db.collection("VoiceChatRoom")
                .document(documentID)
                .collection("RequestUser")
                .document(uid)
                .delete()
    }
    fun deleteVoiceUser(){
        db.collection("VoiceChatRoom")
                .document(documentID)
                .collection("VoiceUser")
                .document(uid)
                .delete()
    }

    fun deleteVoiceChatRoom(){
        db.collection("VoiceChatRoom")
                .document(documentID)
                .delete()
    }

}

