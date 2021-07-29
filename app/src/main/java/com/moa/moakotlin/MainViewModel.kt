package com.moa.moakotlin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.moa.moakotlin.data.Notification
import com.moa.moakotlin.firebase.TAG
import com.moa.moakotlin.repository.alarm.AlarmRepository

class MainViewModel : ViewModel() {

    lateinit var notificationListener : ListenerRegistration

     var notificationList = ArrayList<Notification>()

    var notificationLiveData = MutableLiveData<ArrayList<Notification>>(ArrayList())


    var isRead = MutableLiveData<Boolean>(true)
    var num = 0
    fun setAlarmSnapShot(){
        var repository = AlarmRepository()

        notificationListener = repository.setSnapShot().addSnapshotListener{value, error ->

            if (error != null) {
                Log.w(TAG, "Alarm Listen failed.", error)
                return@addSnapshotListener
            }
            if(value!=null){
                println("오호...")
                for(dc in value.documentChanges){

                    var notification = dc.document.toObject(Notification::class.java)

                    notification.documentID = dc.document.id

                    when(dc.type){
                        DocumentChange.Type.ADDED ->{
                            println(notification.title)
                            notificationList.add(notification)
                            if(num<1){
                                isRead.value = true
                            }else{
                                isRead.value = false
                            }

                        }
                        DocumentChange.Type.MODIFIED->{

                        }
                        DocumentChange.Type.REMOVED->{

                        }else ->{

                    }
                    }

                }
                notificationLiveData.value = notificationList
                num++
            }
        }
    }

    fun removeListener(){
        notificationListener.remove()
    }
}