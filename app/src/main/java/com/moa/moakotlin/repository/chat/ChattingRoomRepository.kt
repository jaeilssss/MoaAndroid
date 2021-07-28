package com.moa.moakotlin.repository.chat

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChattingRoomRepository {
     var livedata = MutableLiveData<ArrayList<ChattingRoom>>()
    suspend fun getChattingRoomList(uid : String) :ArrayList<ChattingRoom> {
            var db = FirebaseFirestore.getInstance()
        var result = ArrayList<ChattingRoom>()
        db.collection("User").document(uid)
            .collection("ChattingRoom")
                .orderBy("timeStamp",Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                for(document in it.documents){
                    var chattingRoom = document.toObject(ChattingRoom::class.java)

                    if (chattingRoom != null) {
                        if(chattingRoom.latestMessage!=null){
                            result.add(chattingRoom)
                        }
                    }
                }
            }.await()

        return result
    }
     fun setSnapShotListener(uid : String): Query{
        var db = FirebaseFirestore.getInstance()
        var listener =  db.collection("User").document(uid)
            .collection("ChattingRoom")
            .orderBy("timeStamp",Query.Direction.DESCENDING)

//                .orderBy("timeStamp", Query.Direction.DESCENDING)
        return listener
    }

   suspend fun goToChatting(writerUid : String) : String{
       var roomId = ""
       var db = FirebaseFirestore.getInstance()
       var list = ArrayList<String>()
       list.add(User.getInstance().uid)
       list.add(writerUid)
       db.collection("User").document(User.getInstance().uid)
           .collection("ChattingRoom").whereEqualTo("opponentUid",writerUid)
           .get().addOnSuccessListener {
               if(it.isEmpty){

               }else {
                   roomId = it.documents.get(0).id
               }
           }.await()
   return roomId
   }


    suspend fun makeChattingRoom(uid: String, writerUid: String): String{
         var roomId = ""
        var chattingRoom = ChattingRoom()
        chattingRoom.timeStamp = Timestamp.now()
        chattingRoom.latestMessage=null
        chattingRoom.opponentUid = writerUid
        var db = FirebaseFirestore.getInstance()
        db.collection("User").document(uid)
            .collection("ChattingRoom")
            .add(chattingRoom).addOnSuccessListener {documentReference ->
                opponentChatting(documentReference.id,writerUid)
                    roomId = documentReference.id
//                navController.navigate(R.id.action_kidReadFragment_to_ChatFragment,bundle)
            }.addOnFailureListener{
                println("failure")
            }.await()

        return roomId
    }

    fun opponentChatting(roomId : String , opponentUid: String){
        var chattingRoom = ChattingRoom()
        chattingRoom.timeStamp = Timestamp.now()
        chattingRoom.latestMessage=null
        chattingRoom.opponentUid = User.getInstance().uid
        var db = FirebaseFirestore.getInstance()
        db.collection("User").document(opponentUid)
            .collection("ChattingRoom")
            .document(roomId)
            .set(chattingRoom).addOnSuccessListener {documentReference ->

            }.addOnFailureListener{
                println("failure")
            }
    }
    fun deleteAtPath(path: String) {
        var functions: FirebaseFunctions = Firebase.functions("asia-northeast3")
        val deleteFn = functions.getHttpsCallable("recursiveDelete")
        deleteFn.call(hashMapOf("path" to path))
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }


}