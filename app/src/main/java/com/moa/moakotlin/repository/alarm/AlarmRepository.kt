package com.moa.moakotlin.repository.alarm

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.User

class AlarmRepository {




    fun setSnapShot() : Query {
        var db = FirebaseFirestore.getInstance()
        var query =  db.collection("User").document(User.getInstance().uid)
            .collection("Notification").orderBy("timeStamp", Query.Direction.DESCENDING)
        return query
    }

}