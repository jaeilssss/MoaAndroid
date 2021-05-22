package com.moa.moakotlin.ui.voice

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.voice.VoiceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class VoiceMainViewModel : ViewModel() {

    var channelName = ObservableField<String>("")
    fun checkChannelName() : Boolean{
        return channelName.get()?.length!=0
    }
    suspend fun generateToken() : String{
        var result = ""
        var repository = VoiceRepository()

        println("chanel-> ${channelName.get()}")
//        var functions  = repository.generateToken("test", User.getInstance().phoneNumber.toInt())
        var functions  = repository.generateToken("test", 1)

            functions.addOnCompleteListener {
                if(it.isSuccessful) result = it.result as String
            }.await()
        return result
    }


}