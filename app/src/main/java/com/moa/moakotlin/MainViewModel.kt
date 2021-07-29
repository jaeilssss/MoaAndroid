package com.moa.moakotlin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.data.Notification
import com.moa.moakotlin.data.User
import com.moa.moakotlin.firebase.TAG
import com.moa.moakotlin.repository.alarm.AlarmRepository
import com.moa.moakotlin.repository.chat.ChattingRoomRepository

class MainViewModel : ViewModel() {

    lateinit var notificationListener : ListenerRegistration

    lateinit var chattingRoomListener : ListenerRegistration


     var notificationList = ArrayList<Notification>()

    var notificationLiveData = MutableLiveData<ArrayList<Notification>>(ArrayList())
    var latestChatRoom = MutableLiveData<String>("")

    var isRead = MutableLiveData<Boolean>(true)
    var isChattingRoomRead = MutableLiveData<Boolean>(true)
    var num = 0

    var lastAlarmDocumentID = ""
    var lastChattingRoomTimestamp=""
    fun setAlarmSnapShot(lastAlarmDocumentID : String){
        var repository = AlarmRepository()

        notificationListener = repository.getSnapShotLimit().addSnapshotListener{value, error ->

            if (error != null) {
                Log.w(TAG, "Alarm Listen failed.", error)
                return@addSnapshotListener
            }
            if(value!=null){
                println("오호...")
                for(dc in value.documentChanges){

                    var notification = dc.document.toObject(Notification::class.java)

                    notification.documentID = dc.document.id
                    if(dc.document.id.equals(lastAlarmDocumentID)){
                        this.lastAlarmDocumentID = lastAlarmDocumentID
                        isRead.value = true
                    }else{
                        this.lastAlarmDocumentID = lastAlarmDocumentID
                        isRead.value = false
                    }
                }
                num++
            }
        }
    }

    fun setChattingRoomSnapShot(){
        var repository = ChattingRoomRepository()
      chattingRoomListener = repository.setSnapShotLimitListener(User.getInstance().uid).addSnapshotListener{value, error ->
            if(value!=null){

                for(dc in value.documentChanges){

                    var chattingRoom = dc.document.toObject(ChattingRoom::class.java)
                    latestChatRoom.value = chattingRoom.timeStamp.toString()
                }
                num++
            }

        }
    }
    fun removeListener(){
        notificationListener.remove()
    }
}