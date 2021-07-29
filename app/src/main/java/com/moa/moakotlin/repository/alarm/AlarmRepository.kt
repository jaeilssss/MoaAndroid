package com.moa.moakotlin.repository.alarm

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.Notification
import com.moa.moakotlin.data.User

class AlarmRepository {




    fun getSnapShot() :Query{
        var db = FirebaseFirestore.getInstance()
        var query =  db.collection("User")
                .document(User.getInstance().uid)
                .collection("Notification")
                .orderBy("timeStamp",Query.Direction.DESCENDING)
        return query
    }
    fun getSnapShotLimit() : Query {
        var db = FirebaseFirestore.getInstance()
        var query =  db.collection("User")
                .document(User.getInstance().uid)
            .collection("Notification")
                .orderBy("timeStamp",Query.Direction.DESCENDING)
                .limit(1)
        return query
    }

    fun sendNotification(notification: Notification , uid : String) {
        var db = FirebaseFirestore.getInstance()

        db.collection("User")
                .document(uid)
                .collection("Notification")
                .add(notification)
    }

}