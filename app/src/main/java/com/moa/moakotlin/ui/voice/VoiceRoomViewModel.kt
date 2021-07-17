package com.moa.moakotlin.ui.voice

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.moa.moakotlin.data.RequestUser
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.VoiceUser
import com.moa.moakotlin.repository.voice.VoiceRepository
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine

class VoiceRoomViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private lateinit var rtcEngine: RtcEngine
    var TAG = "VoiceRoomViewModel"
    private lateinit var mRtcEventHandler: IRtcEngineEventHandler

     var speakers = MutableLiveData<ArrayList<String>>()

    lateinit var mlistener: ListenerRegistration
     var requestSpeakerListener : ListenerRegistration ? =null

     var audiences = MutableLiveData<ArrayList<String>>()
     var speakerList = ArrayList<String>()
     var audienceList = ArrayList<String>()

    var speakerListMap = HashMap<String,VoiceUser>()
    var audienceListMap = HashMap<String,VoiceUser>()
    var isDelete = MutableLiveData<Boolean>(false)
    var myVoiceUser = MutableLiveData<VoiceUser>()

    var requestUsers = MutableLiveData<ArrayList<RequestUser>>()
    var requestUserList = ArrayList<RequestUser>()
    var requestUsermap = HashMap<String,RequestUser>()

    var temp = ArrayList<String>()

     var channelID = ""
    private var muteState = false;


    fun setRequestSpeakerSnapShotListener(channelID: String){
        var db = VoiceRepository()
        var snapshot = db.setRequestSpeakerUser(channelID)

        requestSpeakerListener = snapshot.addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(TAG, "Listen failed.", error)
                return@addSnapshotListener
            }
            if(snapshot != null){
                println(User.getInstance().phoneNumber)
                for(dc in value!!.documentChanges){

                    var requestUser = dc.document.toObject(RequestUser::class.java)

                    when(dc.type){
                        DocumentChange.Type.ADDED ->{
                            requestUserList.add(requestUser)

                        }
                        DocumentChange.Type.MODIFIED->{

                        }
                        DocumentChange.Type.REMOVED->{

                        }else ->{

                    }
                    }
                }
                if(requestUserList.size!=0){
                    requestUsers.value = requestUserList
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
                println(User.getInstance().phoneNumber)
                for(dc in snapshot.documentChanges){
                    var voiceUser = dc.document.toObject(VoiceUser::class.java)
                    when(dc.type){
                        DocumentChange.Type.ADDED ->{
                            if(voiceUser.uid==User.getInstance().uid){
                                myVoiceUser.value = voiceUser
                            }
                            println("added")
                            if(voiceUser.role.equals("audience")){
                                audienceList.add("${voiceUser.phoneNumber.toString()}")
                                audienceListMap.put(voiceUser.phoneNumber,voiceUser)
                            }else{
                                speakerList.add("${voiceUser.phoneNumber.toString()}")
                                speakerListMap.put(voiceUser.phoneNumber,voiceUser)
                            }
                            if(voiceUser.role.equals("owner")){
                                owner = true
                            }
                        }
                        DocumentChange.Type.MODIFIED->{
                            if(voiceUser.uid==User.getInstance().uid){

                            }
                            println("수정됨!!!")
                        }
                        DocumentChange.Type.REMOVED->{
                            println("삭제됨!!!")
                            if(voiceUser.role.equals("audience")){
                                audienceList.remove("${voiceUser.phoneNumber.toString()}")
                                audienceListMap.remove(voiceUser.phoneNumber)
                            }
                            if(voiceUser.role.equals("owner")){
                                speakerList.remove("${voiceUser.phoneNumber}")
                                isDelete.value = true
                            }
                        }else ->{
                            println("else")
                        }
                    }
                }
                if(speakerList.size!=0){
                    speakers.value = speakerList
                }
                audiences.value = audienceList

                if(owner ==false){
                    println("오너가 없어요")
                }
            }
        }
    }

   fun changeAudienceCount(documentID : String){
        var repository = VoiceRepository()

       repository.deCreasePeopleCount(documentID,-1.0)
   }

    fun changeSpeakersCount(documentID: String,num: Long){
        var repository = VoiceRepository()

        repository.changeSpeakersCount(documentID,num)
    }

    fun deleteVoiceUser(voiceChatRoomID : String,uid :String) {
        var repository = VoiceRepository()

      return repository.deleteVoiceUser(voiceChatRoomID,uid)
    }

    fun deleteSnapShot(){
        mlistener.remove()
        requestSpeakerListener?.remove()

    }

    fun deleteVoiceChatRoom(documentID: String){
        var repository = VoiceRepository()
        repository.deleteVoiceChatRoom(documentID)

    }

    fun deleteRequestUser(documentID: String,uid:String){
        var repository = VoiceRepository()

        repository.deleteRequestUser(documentID,uid)
    }
    fun requestSpeakerUser(documentID: String){
        var repository = VoiceRepository()

        var requestUser = RequestUser()

        requestUser.uid =myVoiceUser.value!!.uid
        requestUser.nickName = myVoiceUser.value!!.nickName
        requestUser.phoneNumber = myVoiceUser.value!!.phoneNumber
        requestUser.role = myVoiceUser.value!!.role

        requestUser.profileImage = myVoiceUser.value!!.profileImage
        repository.requestSpeaker(documentID,requestUser)

    }

}