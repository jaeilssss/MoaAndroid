package com.moa.moakotlin.ui.concierge.needer

import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.chat.ChattingRoomRepository

class NeederCompletionViewModel : ViewModel() {

   suspend fun getChattingRoomList() :ArrayList<ChattingRoom>{
        var repository = ChattingRoomRepository()

        return repository.getChattingRoomList(User.getInstance().uid)
    }

}