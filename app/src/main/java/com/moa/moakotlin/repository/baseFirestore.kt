package com.moa.moakotlin.repository

import com.google.firebase.firestore.FirebaseFirestore

abstract class baseFirestore {
    var db = FirebaseFirestore.getInstance()
}