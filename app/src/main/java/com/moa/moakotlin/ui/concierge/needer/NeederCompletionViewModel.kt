package com.moa.moakotlin.ui.concierge.needer

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.chat.ChattingRoomRepository
import com.moa.moakotlin.repository.concierge.NeederRepository

class NeederCompletionViewModel : ViewModel() {

   suspend fun getChattingRoomList() :ArrayList<ChattingRoom>{
        var repository = ChattingRoomRepository()

        return repository.getChattingRoomList(User.getInstance().uid)
    }

     fun skipReview(needer : Needer){
        var db  = FirebaseFirestore.getInstance()
        var repository = NeederRepository()
        repository.hireCompletion(needer)
    }

}