package com.moa.moakotlin.ui.voice

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.VoiceChatRoom
import com.moa.moakotlin.data.VoiceUser
import com.moa.moakotlin.repository.voice.VoiceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class VoiceMainViewModel : ViewModel() {

    var channelName = ObservableField<String>("")

    var voiceChatRoomList = MutableLiveData<ArrayList<VoiceChatRoom>>()
    fun checkChannelName() : Boolean{
        return channelName.get()?.length!=0
    }
    suspend fun generateToken(documentID : String) : String{
        var repository = VoiceRepository()
//        var functions  = repository.generateToken("test", User.getInstance().phoneNumber.toInt())
        var token  = repository.generateToken(documentID, User.getInstance().phoneNumber.toInt())

        return token
    }



    suspend fun initGetVoiceChatRoomList(){
        var repository = VoiceRepository()

        var list = repository.getVoiceRoomList()

        voiceChatRoomList.value = list
    }


    suspend fun goToVoiceRoom(documentID: String) : Boolean{
        var repository = VoiceRepository()
        var voiceUser = VoiceUser()
//
        voiceUser.nickName = User.getInstance().nickName
        voiceUser.phoneNumber = User.getInstance().phoneNumber
        voiceUser.profileImage = User.getInstance().profileImage
        voiceUser.uid = User.getInstance().uid
        voiceUser.role = "audience"


        return repository.goToVoiceRoom(documentID,voiceUser)
    }

    fun increasePeopleCount(position : Int){


        var repository = VoiceRepository()

        repository.changeVoiceChatRoomCount(voiceChatRoomList.value?.get(position)!!.documentID,1)
    }
    }