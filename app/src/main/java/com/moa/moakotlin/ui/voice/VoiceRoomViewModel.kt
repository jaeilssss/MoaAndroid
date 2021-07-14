package com.moa.moakotlin.ui.voice

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.moa.moakotlin.data.VoiceUser
import com.moa.moakotlin.repository.voice.VoiceRepository
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine

class VoiceRoomViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private lateinit var rtcEngine: RtcEngine
    var TAG = "VoiceRoomViewModel"
    private lateinit var mRtcEventHandler: IRtcEngineEventHandler

     var speakers = MutableLiveData<ArrayList<VoiceUser>>()

    lateinit var mlistener: ListenerRegistration

     var audiences = MutableLiveData<ArrayList<VoiceUser>>()
     var speakerList = ArrayList<VoiceUser>()
     var audienceList = ArrayList<VoiceUser>()
     var channelID = ""
    private val ACTION_WORKER_CONFIG_ENGINE = 0X2012

    private var muteState = false;

    var EVENT_TYPE_ON_USER_AUDIO_MUTED = 7

    var EVENT_TYPE_ON_SPEAKER_STATS = 8

    var EVENT_TYPE_ON_AGORA_MEDIA_ERROR = 9

    var EVENT_TYPE_ON_AUDIO_QUALITY = 10

    var EVENT_TYPE_ON_APP_ERROR = 13

    var EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED = 18


    fun goToVoiceRoom(token: String) {

    }

    private fun createIRtcEnginHandler() {

        mRtcEventHandler = object : IRtcEngineEventHandler() {
            override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                println("성공!!!@@@@@@")
            }

            override fun onUserJoined(uid: Int, elapsed: Int) {
                super.onUserJoined(uid, elapsed)
                println("join~~~~~%%%%%%")
            }

            override fun onUserOffline(uid: Int, reason: Int) {
                super.onUserOffline(uid, reason)
                println("offline")
            }

            override fun onError(err: Int) {
                super.onError(err)
                println("error")
            }

            override fun onActiveSpeaker(uid: Int) {
                super.onActiveSpeaker(uid)
            }

            override fun onRemoteAudioStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
                super.onRemoteAudioStateChanged(uid, state, reason, elapsed)
                when (state) {

                }
            }
        }
    }


    fun setSnapShotListener(channelID: String) {
        var db = VoiceRepository()
        var snapShot = db.setSnapShot(channelID)
        var owner = false
        mlistener = snapShot.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if(snapshot != null){
                println("dc size - > ${snapshot.documentChanges.size}")
                for(dc in snapshot.documentChanges){
                    when(dc.type){
                        DocumentChange.Type.ADDED ->{
                            var voiceUser = dc.document.toObject(VoiceUser::class.java)
                            println("added")
                            if(voiceUser.role.equals("audience")){
                                audienceList.add(voiceUser)
                            }else{
                                speakerList.add(voiceUser)
                            }
                            if(voiceUser.role.equals("owner")){
                                owner = true
                            }
                        }
                        DocumentChange.Type.MODIFIED->{
                        println("수정됨!!!")
                        }
                        DocumentChange.Type.REMOVED->{

                        }
                    }
                }
                audiences.value = audienceList
                speakers.value = speakerList

                if(owner ==false){
                    println("오너가 없어요")
                }
            }
        }
    }

   fun updateVoiceChatRoom(documentID : String){
        var repository = VoiceRepository()

       repository.changeVoiceChatRoomCount(documentID,-1)
   }
}