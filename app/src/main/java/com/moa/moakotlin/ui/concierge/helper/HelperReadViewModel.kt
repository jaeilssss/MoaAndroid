package com.moa.moakotlin.ui.concierge.helper

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.chat.ChattingRoomRepository
import com.moa.moakotlin.repository.concierge.HelperRepository

class HelperReadViewModel() : ViewModel() {

    lateinit var writerUid :String

    var newHelper = MutableLiveData<Helper>()
    var roomId = MutableLiveData<String>()

    suspend fun delete(mainCategory : String , documentId : String) : Boolean{
        var repository = HelperRepository()

        return repository.delete(mainCategory,documentId)
    }


    suspend fun getChattingRoom(writerUid: String){

        var repository = ChattingRoomRepository()

        var result = repository.goToChatting(writerUid)

        if(result.equals("").not()){
            roomId.value = result
        }else{
            makeChattingRoom(writerUid)
        }
    }

    suspend fun makeChattingRoom(writerUid: String){

        var repository = ChattingRoomRepository()
        var result =  repository.makeChattingRoom(User.getInstance().uid,writerUid)
        repository.opponentChatting(result,writerUid)
        roomId.value = result

    }

}
