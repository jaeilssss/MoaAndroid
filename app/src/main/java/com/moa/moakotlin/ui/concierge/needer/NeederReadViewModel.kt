package com.moa.moakotlin.ui.concierge.needer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.chat.ChattingRoomRepository
import com.moa.moakotlin.repository.concierge.NeederRepository
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NeederReadViewModel : ViewModel() {

    var roomId = MutableLiveData<String>()

    suspend fun delete(mainCategory : String , documentId : String) : Boolean{

        var repository = NeederRepository()

        return repository.delete(mainCategory ,documentId)
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
    suspend fun getWriterInfo(writerUid : String): User? {


        var repository = UserRepository()

        return repository.getUserInfo(writerUid)
    }
}