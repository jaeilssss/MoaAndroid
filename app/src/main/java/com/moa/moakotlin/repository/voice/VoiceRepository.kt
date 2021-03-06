package com.moa.moakotlin.repository.voice

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.moa.moakotlin.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream

class VoiceRepository  {
    private lateinit var functions: FirebaseFunctions
    suspend fun generateToken(channelName: String,uid : Int): String{
        // Create the arguments to the callable function.
         functions = Firebase.functions("asia-northeast3")
         var token = ""
        val data = hashMapOf(
            "channelName" to channelName,
            "uid" to uid
        )
         functions
            .getHttpsCallable("generateToken")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as Map<String, String>
             token=   result.get("token") as String
            }.await()

        return token
     }


   suspend fun makeVoiceRoom(voiceChatRoom: VoiceChatRoom) : String{
        var documentId =""
        var db = FirebaseFirestore.getInstance()
        db.collection("VoiceChatRoom")
                .add(voiceChatRoom)
                .addOnSuccessListener {
                    documentId = it.id
                    }.await()

       return documentId
                }


  suspend fun upload(path : String ): String{

      var result =""
        var storageRef : StorageReference = FirebaseStorage.getInstance().reference


        var file = Uri.fromFile(File(path))

        var inputstream = FileInputStream(File(path))

        val riversRef = storageRef.child("VoiceChatImage"+"/" + file.lastPathSegment)
        val uploadTask = riversRef.putStream(inputstream)

        uploadTask.continueWithTask { riversRef.downloadUrl }.addOnCompleteListener { task ->
                result = task.result.toString()
            }.await()
        return result
        }

    suspend fun getVoiceRoomList() : ArrayList<VoiceChatRoom>{
        var voiceChatRoomList = ArrayList<VoiceChatRoom>()

        var db = FirebaseFirestore.getInstance()

        db.collection("VoiceChatRoom")
                .whereArrayContainsAny("aroundApt", arrayListOf(User.getInstance().aptCode,"All"))
                .orderBy("peopleCount",Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    for(document in it.documents){
                        var data = document.toObject(VoiceChatRoom::class.java)

                        data?.documentID = document.id

                        if(data!=null){
                            voiceChatRoomList.add(data)
                        }

                    }
                }.await()

        return voiceChatRoomList
    }

    suspend fun goToVoiceRoom(voiceRoomDocumentID:String,voiceUser : VoiceUser) : Boolean{
        var check = false
        var db = FirebaseFirestore.getInstance()

        db.collection("VoiceChatRoom")
                .document(voiceRoomDocumentID)
                .collection("VoiceUser")
                .document(voiceUser.uid)
                .set(voiceUser)
                .addOnSuccessListener {
                    check = true
                }.await()
        return check
    }

    fun setSnapShot(voiceChatID : String) : Query{
        var db = FirebaseFirestore.getInstance()
        var query =  db.collection("VoiceChatRoom").document(voiceChatID)
                .collection("VoiceUser")
        return query
    }

    fun setRequestSpeakerUser(voiceChatID : String) : Query{
        var db = FirebaseFirestore.getInstance()

        var query = db.collection("VoiceChatRoom")
                .document(voiceChatID)
                .collection("RequestUser")
        return query
    }

    fun update(voiceChatRoom: VoiceChatRoom){
        var db = FirebaseFirestore.getInstance()

        db.collection("VoiceChatRoom")
                .document(voiceChatRoom.documentID)
                .set(voiceChatRoom)

    }

    fun changeVoiceChatRoomCount(documentID: String,num : Long){
        var db = FirebaseFirestore.getInstance()

        db.collection("VoiceChatRoom")
                .document(documentID)
                .update("peopleCount", FieldValue.increment(num))
    }

    fun changeSpeakersCount(documentID: String,num: Long){
        var db = FirebaseFirestore.getInstance()
        db.collection("VoiceChatRoom")
                .document(documentID)
                .update("speakersCount", FieldValue.increment(num))
    }
    fun deCreasePeopleCount(documentID: String,num: Double){
        var db = FirebaseFirestore.getInstance()

        db.collection("VoiceChatRoom")
                .document(documentID)
                .update("peopleCount", FieldValue.increment(-1))
    }
     fun deleteVoiceUser(voiceChatRoomDocumentID : String ,uid: String){
        var db = FirebaseFirestore.getInstance()
        var check = false

        db.collection("VoiceChatRoom")
                .document(voiceChatRoomDocumentID)
                .collection("VoiceUser")
                .document(uid)
                .delete()
                .addOnSuccessListener { println("deleteVoiceUser") }
    }

    fun deleteVoiceChatRoom(documentId :String){
        var db = FirebaseFirestore.getInstance()
        db.collection("VoiceChatRoom")
                .document(documentId)
                .delete()
    }


    fun requestSpeaker(documentID: String,requestUser: RequestUser){
        var db = FirebaseFirestore.getInstance()

        db.collection("VoiceChatRoom")
                .document(documentID)
                .collection("RequestUser")
                .document(requestUser.uid)
                .set(requestUser)

    }

    fun deleteRequestUser(documentID: String, uid : String){
        var db = FirebaseFirestore.getInstance()

        db.collection("VoiceChatRoom")
                .document(documentID)
                .collection("RequestUser")
                .document(uid)
                .delete()
    }

    suspend fun checkVoiceRoomOwner(documentID: String) : Boolean{
        var check = false
        var db = FirebaseFirestore.getInstance()

        db.collection("VoiceChatRoom")
                .document(documentID)
                .get()
                .addOnSuccessListener {
                    check = !it.exists()
                }.await()

        return check
    }
    }
