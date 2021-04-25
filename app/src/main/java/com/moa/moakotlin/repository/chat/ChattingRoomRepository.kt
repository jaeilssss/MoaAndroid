package com.moa.moakotlin.repository.chat

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.ChattingRoom
import kotlinx.coroutines.tasks.await

class ChattingRoomRepository {
     var livedata = MutableLiveData<ArrayList<ChattingRoom>>()
    suspend fun getChattingRoomList(uid : String) :ArrayList<ChattingRoom> {
            var db = FirebaseFirestore.getInstance()
        var result = ArrayList<ChattingRoom>()
        db.collection("User").document(uid)
            .collection("ChattingRoom")
            .get().addOnSuccessListener {
                for(document in it.documents){
                    var chattingRoom = document.toObject(ChattingRoom::class.java)

                }
            }.await()

        return result
    }
     fun setSnapShotListener(uid : String): Query{
        var db = FirebaseFirestore.getInstance()
        var listener =  db.collection("User").document(uid)
            .collection("ChattingRoom").orderBy("timeStamp", Query.Direction.DESCENDING)


        return listener
    }
}