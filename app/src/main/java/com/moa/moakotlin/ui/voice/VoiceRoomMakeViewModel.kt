package com.moa.moakotlin.ui.voice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VoiceRoomMakeViewModel : ViewModel() {
    var theme = MutableLiveData<String>("")
    var title = MutableLiveData<String>("")
    var voiceRoomAround = MutableLiveData<String>("")

    fun submit(){

    }

    fun check() : Boolean{
        return theme.value?.length!!>0 &&
                title.value?.length!!>0 &&
                voiceRoomAround.value?.length!!>0
    }
}