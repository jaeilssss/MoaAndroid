package com.moa.moakotlin.ui.chat

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.chat.ChattingRoomRepository

class ChattingRoomViewModel() : ViewModel(){

    var chattingRoomData = MutableLiveData<ArrayList<ChattingRoom>>()
    var list = ArrayList<ChattingRoom>()
    var roomList = ArrayList<String>()
    var TAG = "ChattingRoom"

    lateinit var mlistener : ListenerRegistration
    fun deleteSnapShot(){
        mlistener.remove()
    }
 fun setSnapShot(){

    var repository = ChattingRoomRepository()

    var snapShot = repository.setSnapShotListener(User.getInstance().uid)

     mlistener = snapShot.addSnapshotListener{ snapshot, e ->
         if (e != null) {
             Log.w(TAG, "Listen failed.", e)
             return@addSnapshotListener
         }

         if (snapshot != null) {
            println("snapshot.documents.size -> ${snapshot.documents.size}")
             for(dc in snapshot.documentChanges){
                    println("몇번일까요?")
                    when(dc.type) {
                     DocumentChange.Type.ADDED -> {

                         var chattingRoom = dc.document.toObject(ChattingRoom::class.java)
                         if(chattingRoom.latestMessage!=null){
                             roomList.add(dc.document.id)

                             list?.add(chattingRoom)
                         }

                     }
                     DocumentChange.Type.MODIFIED -> {
                         println("modify..")
                         var chattingRoom = dc.document.toObject(ChattingRoom::class.java)
                             var index = roomList?.indexOf(dc.document.id) ?: -1

                         if(index==-1){
                             list?.add(chattingRoom)
                             roomList.add(dc.document.id)
                         }else{
                             println("index -> ${index}")
                             list?.removeAt(index)
                             list?.add(0,chattingRoom)
                             roomList.removeAt(index)
                             roomList.add(0,dc.document.id)
                         }

                     }
                     DocumentChange.Type.REMOVED -> {

                     }else ->{
                         println("여기 채팅룸...")
                     }
                 }
             }
             chattingRoomData.value = list!!
         }
     }

}

}