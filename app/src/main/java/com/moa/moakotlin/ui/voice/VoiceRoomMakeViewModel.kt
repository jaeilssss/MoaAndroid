package com.moa.moakotlin.ui.voice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.VoiceChatRoom
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.repository.voice.VoiceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoiceRoomMakeViewModel : ViewModel() {
    var theme = MutableLiveData<String>("")
    var title = MutableLiveData<String>("")
    var range = MutableLiveData<String>("")
    var image = ""
    var documentID =""
    lateinit var voiceChatRoom : VoiceChatRoom
   suspend fun submit() : String{
        var repository = VoiceRepository()
        if(image.isNotEmpty()){

                var path = repository.upload(image)
               goToCheck(path)
                 documentID = repository.makeVoiceRoom(voiceChatRoom)
            var  token= repository.generateToken(documentID,User.getInstance().phoneNumber.toInt())
            return token
        }else{
                goToCheck("")
             documentID = repository.makeVoiceRoom(voiceChatRoom)
            var  token= repository.generateToken(documentID,User.getInstance().phoneNumber.toInt())
            return token
        }
    }

    fun goToCheck(path : String) {
        if(range.value?.equals("전국") == true){
            voiceChatRoom = VoiceChatRoom("",title.value!!,
                    range.value!!, Timestamp.now(), User.getInstance().nickName,User.getInstance().uid,1,1,path,
                    arrayListOf("ALL"),theme.value!!)
        }else if(range.value?.equals("인근")==true){
            voiceChatRoom = VoiceChatRoom("",title.value!!,
                    range.value!!, Timestamp.now(), User.getInstance().nickName,User.getInstance().uid,1,1,path,
                    aptList.getInstance().aroundApt,theme.value!!)
        }else {
            voiceChatRoom = VoiceChatRoom("",title.value!!,
                    range.value!!, Timestamp.now(), User.getInstance().nickName,User.getInstance().uid,1,1,path,
                    arrayListOf(User.getInstance().aptCode),theme.value!!)
        }
    }
    fun check() : Boolean{
        return theme.value?.length!!>0 &&
                title.value?.length!!>0 &&
                range.value?.length!!>0
    }
}