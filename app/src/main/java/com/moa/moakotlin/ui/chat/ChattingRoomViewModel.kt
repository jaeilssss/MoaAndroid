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
    var roomList = ArrayList<String>()
    var TAG = "ChattingRoom"
    lateinit var mlistener : ListenerRegistration
    fun deleteSnapShot(){
        mlistener.remove()
    }
 fun setSnapShot(){
     chattingRoomData.value = ArrayList()
    var repository = ChattingRoomRepository()

    var snapShot = repository.setSnapShotListener(User.getInstance().uid)

     mlistener = snapShot.addSnapshotListener{ snapshot, e ->
         if (e != null) {
             Log.w(TAG, "Listen failed.", e)
             return@addSnapshotListener
         }
         var list = chattingRoomData.value
         if (snapshot != null) {
             for(dc in snapshot.documentChanges){
                 when(dc.type) {
                     DocumentChange.Type.ADDED -> {
                         var chattingRoom = dc.document.toObject(ChattingRoom::class.java)
                         roomList.add(dc.document.id)
                            list?.add(chattingRoom)
                     }
                     DocumentChange.Type.MODIFIED -> {
                         println("modify")
                         var chattingRoom = dc.document.toObject(ChattingRoom::class.java)
                             var index = roomList?.indexOf(dc.document.id) ?: -1
                         list?.removeAt(index)
                         list?.add(0,chattingRoom)
                         roomList.removeAt(index)
                         roomList.add(0,dc.document.id)
                     }
                     DocumentChange.Type.REMOVED -> {
                     }
                 }
             }
             chattingRoomData.value = list!!
         }
     }
}

}