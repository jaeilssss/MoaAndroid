package com.moa.moakotlin.repository.user

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.moa.moakotlin.data.ApartCertification
import com.moa.moakotlin.data.DropOut
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.aptList
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream


class UserRepository {


    suspend fun getMyUserInfo(phoneUid : String) : User?{
        var db = FirebaseFirestore.getInstance()
        var user :User ?=null
        db.collection("User")
                .whereEqualTo("phoneUid",phoneUid)
                .get()
                .addOnSuccessListener {
                    for(document in it.documents){
                        user = document.toObject(User::class.java)
                        user?.uid = document.id
                    }
                }
                .await()
        return user
    }

    suspend fun getUserInfo(documentId : String): User ?{
        var user :User ?=null
        User.getInstance()
        var db  = FirebaseFirestore.getInstance()

        db.collection("User").document(documentId)
            .get().addOnSuccessListener {
                if(it.exists()){

                    user = it.toObject(User::class.java)
                    user?.uid = it.id
                }else{
                    user = null
                }

            }.await()
        return user
    }

    suspend fun signUpUser(user : User) : String?{
        var result :String ?=null
        var db = FirebaseFirestore.getInstance()

        db.collection("User").add(user)
            .addOnSuccessListener {
                user.uid = it.id
                addUid(user)
                    result = it.id
            }.await()
        return result
    }


    suspend fun getaroundApt(aptCode : String) : Boolean{
        var result = false
        var db = FirebaseFirestore.getInstance()
        aptList.getInstance()

        db.collection("Aparts").document(aptCode)
            .get().addOnSuccessListener {
                if(it.exists()){
                    var list = it.toObject(aptList::class.java)
                    if (list != null) {

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

        db.collection("User")
                .whereEqualTo("nickName",nickname)
                .get().addOnSuccessListener {
                    if(it.isEmpty){
                        result = true
                    }
                }.await()
        return result
    }

    fun userCertification(certification : ApartCertification){

        var db = FirebaseFirestore.getInstance()

        db.collection("ApartCertification").add(certification)
                .addOnSuccessListener {

        }
    }

    suspend fun upload(path : String ): String{

        var result =""

        var storageRef : StorageReference = FirebaseStorage.getInstance().reference

        var file = Uri.fromFile(File(path))

        var inputstream = FileInputStream(File(path))

        val riversRef = storageRef.child("UserProfile"+"/" + file.lastPathSegment)

        val uploadTask = riversRef.putStream(inputstream)

        uploadTask.continueWithTask { riversRef.downloadUrl }.addOnCompleteListener { task ->
            result = task.result.toString()
        }.await()

        return result
    }

    fun addUid(user : User){
        var db = FirebaseFirestore.getInstance()

        db.collection("User").document(user.uid)
                .set(user)
    }

    suspend fun modify(user : User) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var check = false
        db.collection("User")
                .document(User.getInstance().uid)
                .set(user)
                .addOnSuccessListener {
                    check = true
                }.await()
        return check
    }

    suspend fun modifyApt(aptName :String , aptCode : String , address : String) : Boolean {
        var db = FirebaseFirestore.getInstance()
        var check = false
        db.collection("User")
                .document(User.getInstance().uid)
                .update("aptName", aptName,
                        "aptCode", aptCode,
                        "address", address,
                        "certificationStatus", "미인증")
                .addOnSuccessListener {
                    check = true
                }.await()
        return check
    }


    fun updateAlarmSetting(){
        var db = FirebaseFirestore.getInstance()

        db.collection("User")
            .document(User.getInstance().uid)
            .update("isAgreeChattingAlarm",User.getInstance().isAgreeChattingAlarm,
            "isAgreeEventAlarm",User.getInstance().isAgreeEventAlarm,
            "isAgreeMarketing",User.getInstance().isAgreeMarketing)

    }

   suspend fun dropOutUser() : Boolean{
        val user = FirebaseAuth.getInstance().currentUser
        var check = false
        user!!.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        check = true
                    }
                }.await()

       return check
    }

    fun registerPushToken() {

        //v17.0.0 이전까지는
        ////var pushToken = FirebaseInstanceId.getInstance().token
        //v17.0.1 이후부터는 onTokenRefresh()-depriciated

        var pushToken: String? = null

        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        var map = mutableMapOf<String, Any>()
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            pushToken = instanceIdResult.token
            User.getInstance().pushToken = pushToken as String
            map["pushtoken"] = pushToken!!
            FirebaseFirestore.getInstance().collection("User").document(User.getInstance().uid).set(User.getInstance())
        }
    }

    fun deleteAtPath(path: String) {
        var functions: FirebaseFunctions = Firebase.functions("asia-northeast3")
        val deleteFn = functions.getHttpsCallable("recursiveDelete")
        deleteFn.call(hashMapOf("path" to path))
                .addOnSuccessListener {
                    // Delete Success
                    // ...
                    System.out.println("성공!!!")
                }
                .addOnFailureListener {
                    // Delete Failed
                    // ...
                    System.out.println("실패..!!!")
                }
    }

    suspend fun writeDropOutReason(dropOut: DropOut) : Boolean{

        var db = FirebaseFirestore.getInstance()
        var result = false
        db.collection("MyPage")
                .document("MyPage")
                .collection("Dropout")
                .add(dropOut)
                .addOnSuccessListener {
                    result = true
                }.await()

        return result
    }

    suspend fun checkAlreadyPhone(phoneNumber : String) : Boolean{
        var db = FirebaseFirestore.getInstance()
        var result = false
        db.collection("User")
                .whereEqualTo("phoneNumber",phoneNumber)
                .get()
                .addOnSuccessListener {
                    result = it.isEmpty
                }
                .await()

        return result
    }

}