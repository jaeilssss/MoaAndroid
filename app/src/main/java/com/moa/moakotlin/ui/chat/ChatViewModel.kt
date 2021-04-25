package com.moa.moakotlin.ui.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.Chat
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.repository.chat.ChatRepository


class ChatViewModel() : ViewModel(){
var talk = ObservableField<String>("")
var msg = MutableLiveData<Chat>()
lateinit var mlistener : ListenerRegistration
    var TAG = "Chat"

    fun deleteSnapShot(){
        mlistener.remove()
    }
    fun setReadTrue(roomId: String){
        var repository = ChatRepository(roomId)

        repository.setReadTrue()
    }
    fun imagePicker(){


    }

    fun send(roomId: String,opponentUid: String){
            var repository = ChatRepository(roomId)
        var chat = Chat()
        chat.timeStamp = Timestamp.now()
        chat.talk = talk.get().toString()
        chat.uid = User.getInstance().uid

        repository.send(chat,opponentUid)
        talk.set("")
    }

    suspend fun initView(roomId : String) : Boolean{
        var repository = ChatRepository(roomId)
        var result = repository.initView()

        return result

    }

    suspend fun nextChat(roomId : String , oldList : ArrayList<Chat>) : Boolean{
        var repository = ChatRepository(roomId)
        var result = repository.nextChat(oldList)
        return result
    }
  suspend fun scroll(roomId: String,lastTime:Timestamp) : ArrayList<Chat>{
        var repository = ChatRepository(roomId)

      var result = repository.scrollChat(lastTime)

        return result
    }
     fun setSnapShot(roomId: String){

        var repository = ChatRepository(roomId)

        var clistener = repository.setSnapShot()

    mlistener= clistener.addSnapshotListener { value, error ->
            if(error !=null){
                Log.w(TAG, "Listen failed.", error)
                return@addSnapshotListener
            }
            var list = msg.value

            if(value!=null){
                for(document in value.documents){
                    var chat = document.toObject(Chat::class.java)
                    var size = Chat.getInstance().get(roomId)?.size
                    if (size != null) {
                        if(chat?.timeStamp !=Chat.getInstance().get(roomId)?.get(size-1)?.timeStamp){
                            if (chat != null) {
                                msg.value = chat!!
                                println("snapShot이 움직임!!")
                            }
                        }

                    }
                }
            }

        }
    }
fun settingChattingRoomDb(myId:String ,chattingRoom: ChattingRoom){
//    var firebase = FirebaseFirestore.getInstance()
//    firebase.collection("User").document(myId)
//            .collection("ChattingRoom").document(roomId)
//            .set(chattingRoom).addOnCompleteListener {
//
//            }
}
}