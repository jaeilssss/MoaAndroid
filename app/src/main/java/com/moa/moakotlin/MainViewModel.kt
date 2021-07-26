package com.moa.moakotlin

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.moa.moakotlin.repository.alarm.AlarmRepository

class MainViewModel : ViewModel() {

    lateinit var mlistener : ListenerRegistration


    fun setAlarmSnapShot(){
        var repository = AlarmRepository()


        mlistener = repository.setSnapShot().addSnapshotListener{value, error ->
            if(value!=null){


            }
        }
    }
}