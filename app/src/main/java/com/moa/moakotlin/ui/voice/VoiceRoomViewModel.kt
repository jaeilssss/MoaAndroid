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
import com.moa.moakotlin.repository.user.UserRepository
import com.moa.moakotlin.repository.voice.VoiceRepository
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoiceRoomViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private lateinit var rtcEngine: RtcEngine
    var TAG = "VoiceRoomViewModel"
    private lateinit var mRtcEventHandler: IRtcEngineEventHandler

    var speakers = MutableLiveData<ArrayList<String>>()
    var talking = ArrayList<String>()
     var mlistener: ListenerRegistration ? =null
     var requestSpeakerListener : ListenerRegistration ? =null

     var audiences = MutableLiveData<ArrayList<String>>()
     var speakerList = ArrayList<String>()
     var audienceList = ArrayList<String>()

    var speakerListMap = HashMap<String,VoiceUser>()
    var audienceListMap = HashMap<String,VoiceUser>()
    var isDelete = MutableLiveData<Boolean>(false)
    var myVoiceUser = MutableLiveData<VoiceUser>()

    var requestUsers = MutableLiveData<ArrayList<String>>()
    var requestUserList = ArrayList<String>()
    var requestUsermap = HashMap<String,RequestUser>()

    var temp = ArrayList<String>()

     var channelID = ""
    private var muteState = false;

    fun clearViewModel(){
         speakers = MutableLiveData<ArrayList<String>>()
         talking = ArrayList<String>()



       audiences = MutableLiveData<ArrayList<String>>()
         speakerList = ArrayList<String>()
         audienceList = ArrayList<String>()

         speakerListMap = HashMap<String,VoiceUser>()
         audienceListMap = HashMap<String,VoiceUser>()
         isDelete = MutableLiveData<Boolean>(false)
         myVoiceUser = MutableLiveData<VoiceUser>()

         requestUsers = MutableLiveData<ArrayList<String>>()
         requestUserList = ArrayList<String>()
         requestUsermap = HashMap<String,RequestUser>()

         temp = ArrayList<String>()
    }
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
                            requestUserList.add("${requestUser.phoneNumber}")
                            //
                        }
                        DocumentChange.Type.MODIFIED->{

                        }
                        DocumentChange.Type.REMOVED->{
                            requestUserList.remove("${requestUser.phoneNumber}")
                            //
                        }else ->{

                    }
                    }
                }
                if(requestUserList.size!=0){
                    requestUsers.value = requestUserList
                }
                requestUsers.value = requestUserList
            }

        }
    }

    fun setSnapShotListener(channelID: String) {

        var db = VoiceRepository()
        var snapShot = db.setSnapShot(channelID)
        var owner = false
        this.channelID = channelID
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
                                myVoiceUser.value = voiceUser
                            }

                            if(voiceUser.role.equals("speaker")){
                                if(voiceUser.phoneNumber ==myVoiceUser.value?.phoneNumber){
                                    myVoiceUser.value = voiceUser
                                    changeSpeakersCount(channelID,1)
                                }
                                audienceList.remove("${voiceUser.phoneNumber}")
                                audienceListMap.remove(voiceUser.phoneNumber)
                                speakerList.add("${voiceUser.phoneNumber}")
                                speakerListMap.put(voiceUser.phoneNumber,voiceUser)

                            }else if(voiceUser.role.equals("audience")){
                                if(voiceUser.phoneNumber ==myVoiceUser.value?.phoneNumber){
                                    myVoiceUser.value = voiceUser
                                    changeSpeakersCount(channelID,-1)
                                }
                                speakerList.remove("${voiceUser.phoneNumber}")
                                speakerListMap.remove(voiceUser.phoneNumber)
                                audienceList.add("${voiceUser.phoneNumber}")
                                audienceListMap.put(voiceUser.phoneNumber,voiceUser)

                            }
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
                            if(voiceUser.role.equals("speaker")){
                                speakerList.remove("${voiceUser.phoneNumber}")
                                speakerListMap.remove("${voiceUser.phoneNumber}")
                            }
                        }else ->{

                        }
                    }
                }
                if(speakerList.size!=0) {
                    speakers.value = speakerList
                }
                    audiences.value = audienceList


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
        mlistener?.remove()
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

    fun checkVoiceRoom(){
         var repository = VoiceRepository()

        CoroutineScope(Dispatchers.Main).launch {
            if(repository.checkVoiceRoomOwner(channelID)){
                isDelete.value = true
            }
        }

    }

  suspend  fun getUserInfo(voiceUser: VoiceUser) : User?{

        var repository = UserRepository()

      var user = repository.getUserInfo(voiceUser.uid)

      return user
    }

}