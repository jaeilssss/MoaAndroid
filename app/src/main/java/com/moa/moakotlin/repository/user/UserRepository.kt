package com.moa.moakotlin.repository.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.data.ApartCertification
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import kotlinx.coroutines.tasks.await



class UserRepository {

    suspend fun getUserInfo(documentId : String): User ?{
        var user :User ?=null
        User.getInstance()
        var db  = FirebaseFirestore.getInstance()

        db.collection("User").document(documentId)
            .get().addOnSuccessListener {
                if(it.exists()){

                    user = it.toObject(User::class.java)!!
                    user!!.uid = it.id

                }else{
                    user = null
                }

            }.await()
        return user
    }

    suspend fun signUpUser(user : User) : Boolean{
        var result = false
        var db = FirebaseFirestore.getInstance()

        db.collection("User").document(FirebaseAuth.getInstance().uid!!).set(user)
            .addOnSuccessListener {
                    result = true
            }.await()
        return result
    }


    suspend fun getaroundApt(aptCode : String) : Boolean{
        var result = false
        var db = FirebaseFirestore.getInstance()
        aptList.getInstance()
        db.collection("Apart").document(aptCode)
            .get().addOnSuccessListener {
                if(it.exists()){
                    var list = it.toObject(aptList::class.java)
                    if (list != null) {
                        println(list.aroundApt.get(0))
                        aptList.setInstance(list)
                    }
                    result = true
                }
            }.await()
        return result
    }

    suspend fun checkNickName(nickname :String) : Boolean{
        var result = false

        var db = FirebaseFirestore.getInstance()

        db.collection("User").whereEqualTo("nickName",nickname)
                .get().addOnSuccessListener {
                    if(it.isEmpty){
                        result = true
                    }
                }.await()
        return result
    }

    fun userCertification(certification : ApartCertification){

        var db = FirebaseFirestore.getInstance()

        db.collection("ApartCertification").add(certification).addOnSuccessListener {

        }
    }
}