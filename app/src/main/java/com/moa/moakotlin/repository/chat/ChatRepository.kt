package com.moa.moakotlin.repository.chat

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moa.moakotlin.data.Chat
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.data.User
import kotlinx.coroutines.tasks.await

class ChatRepository(var roomId: String) {


    fun send(chat : Chat, opponentUid :String){
        var db = FirebaseFirestore.getInstance()

        db.collection("ChattingRoom").document(roomId)
            .collection("Chat")
            .add(chat).addOnSuccessListener {
                        var chattingRoom = ChattingRoom()
                chattingRoom.isRead=  true
                chattingRoom.latestMessage = chat.talk
                chattingRoom.opponentUid =opponentUid
                chattingRoom.timeStamp = Timestamp.now()
                updateLatestMessage(User.getInstance().uid,chattingRoom)
                var chattingRoom2 = ChattingRoom()
                chattingRoom2.isRead=  false
                chattingRoom2.latestMessage = chat.talk
                chattingRoom2.opponentUid =User.getInstance().uid
                chattingRoom2.timeStamp = Timestamp.now()
                updateLatestMessage(opponentUid,chattingRoom2)
            }
    }

    fun updateLatestMessage(myId:String , chattingRoom : ChattingRoom){
        var db = FirebaseFirestore.getInstance()

        db.collection("User").document(myId)
            .collection("ChattingRoom").document(roomId)
            .set(chattingRoom).addOnCompleteListener {

            }
    }

   suspend fun initView() : Boolean {
       var list = ArrayList<Chat>()
       var result = false
        var db = FirebaseFirestore.getInstance()
            db.collection("ChattingRoom")
                .document(roomId).collection("Chat")
                .orderBy("timeStamp", Query.Direction.DESCENDING).limit(100)
                .get().addOnSuccessListener{
                    for( document in it.documents){
                        var chat = document.toObject(Chat::class.java)
                        if(chat!=null) list.add(chat)
                    }
                    list.reverse()
                    Chat.setInstance(roomId,list)
                    result =true
                }.await()
       return result
    }

    suspend fun nextChat(oldChatList : ArrayList<Chat>) : Boolean{
        var list : ArrayList<Chat>
        var result = false
        var db = FirebaseFirestore.getInstance()
        db.collection("ChattingRoom").document(roomId)
            .collection("Chat").orderBy("timeStamp",Query.Direction.DESCENDING)
            .endBefore(oldChatList.get(oldChatList.size-1).timeStamp)
            .get().addOnSuccessListener {
                 list = ArrayList<Chat>()
                var i = 1
                for(dc in it.documents){
                    var chat = dc.toObject(Chat::class.java)
                    if (chat != null) {
                        list.add(chat)
                    }
                }
                list.reverse()
                oldChatList.addAll(list)
                Chat.setInstance(roomId,oldChatList)
                result = true
//
//
//                rcv.scrollToPosition(adapter.itemCount-1)
//                //
//                snapShotListener(adapter)
            }.await()
        return result
    }

    suspend fun scrollChat(lastTime : Timestamp) : ArrayList<Chat>{
        var list = ArrayList<Chat>()
        var db = FirebaseFirestore.getInstance()
        db.collection("ChattingRoom")
            .document(roomId).collection("Chat")
            .orderBy("timeStamp",Query.Direction.DESCENDING)
            .startAfter(lastTime).limit(100)
            .get().addOnSuccessListener {
                for(document in it.documents){
                    var chat = document.toObject(Chat::class.java)
                    if(chat!=null) list.add(chat)
                }
            }.await()
        println("리스트 사이즈  -> ${list.size}")
        list.reverse()
        return list
    }


    fun setSnapShot() : Query{
        var db = FirebaseFirestore.getInstance()
       var query =  db.collection("ChattingRoom").document(roomId)
            .collection("Chat").orderBy("timeStamp",Query.Direction.DESCENDING).limit(1)
            return query
    }

fun setReadTrue(){
    var db = FirebaseFirestore.getInstance()
    db.collection("User")
        .document(User.getInstance().uid)
        .collection("ChattingRoom")
        .document(roomId)
        .update(
            "isRead",true
        ).addOnSuccessListener {

        }
}

}