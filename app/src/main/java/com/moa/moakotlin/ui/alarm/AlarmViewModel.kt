package com.moa.moakotlin.ui.alarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.moa.moakotlin.data.Notification
import com.moa.moakotlin.repository.alarm.AlarmRepository

class AlarmViewModel : ViewModel() {

    lateinit var notificationListener : ListenerRegistration

     var notificationList = MutableLiveData<ArrayList<Notification>>()
    var list = ArrayList<Notification>()
    fun SnapShot(){
       var repository = AlarmRepository()

       notificationListener = repository.getSnapShot().addSnapshotListener{value, error ->

           if(value!=null){

               for(dc in value.documentChanges){
                   var notification = dc.document.toObject(Notification::class.java)
                    notification.documentID = dc.document.id
                   when(dc.type){

                       DocumentChange.Type.ADDED->{
                           list.add(notification)
                       }
                       DocumentChange.Type.MODIFIED->{

                       }
                       DocumentChange.Type.REMOVED->{

                       }
                       else->{

                       }
                   }
               }

               notificationList.value = list
           }
       }
    }

    fun removeSnapShot(){
        notificationListener.remove()
    }
}