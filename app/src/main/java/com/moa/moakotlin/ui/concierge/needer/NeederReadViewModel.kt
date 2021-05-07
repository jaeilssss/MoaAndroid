package com.moa.moakotlin.ui.concierge.needer

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.data.User

class NeederReadViewModel() : ViewModel() {

    lateinit var writerUid :String
    fun goToChatting(writerUid : String){
        this.writerUid = writerUid
        var db = FirebaseFirestore.getInstance()
            var list = ArrayList<String>()
        list.add(User.getInstance().uid)
        list.add(writerUid)
        db.collection("User").document(User.getInstance().uid)
            .collection("ChattingRoom").whereEqualTo("opponentUid",writerUid)
            .get().addOnSuccessListener {
                if(it.isEmpty){
                    makChattingRoom(User.getInstance().uid,writerUid)
                }else {
                    println("이미 만들어저있는 채팅방!!")
                    var bundle = Bundle()
                    bundle.putString("roomId",it.documents.get(0).id)
                    println("roomId : ${it.documents.get(0).id}")
                    bundle.putString("writerUid",writerUid)
//                    navController.navigate(R.id.action_kidReadFragment_to_ChatFragment,bundle)
                }
            }
    }

    fun makChattingRoom(uid: String,opponentUid :String){
        var chattingRoom = ChattingRoom()
        chattingRoom.timeStamp = Timestamp.now()
        chattingRoom.latestMessage=null
        chattingRoom.opponentUid = opponentUid
        var db = FirebaseFirestore.getInstance()
        db.collection("User").document(uid)
            .collection("ChattingRoom")
            .add(chattingRoom).addOnSuccessListener {documentReference ->
               opponentChatting(documentReference.id,opponentUid)
                var bundle = Bundle()
                bundle.putString("roomId",documentReference.id)
                bundle.putString("opponentUid",opponentUid)
//                navController.navigate(R.id.action_kidReadFragment_to_ChatFragment,bundle)
            }.addOnFailureListener{
                println("failure")
            }
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

}
