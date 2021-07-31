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
import com.moa.moakotlin.data.*
import com.moa.moakotlin.repository.chat.ChatRepository
import com.moa.moakotlin.repository.push.FcmRepository


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

    fun send(roomId: String,opponentUser: User){
            var repository = ChatRepository(roomId)
        var chat = Chat()
        chat.timeStamp = Timestamp.now()
        chat.talk = talk.get().toString()
        chat.uid = User.getInstance().uid

        repository.send(chat,opponentUser.uid)
        pushToken(opponentUser,chat.talk)
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
            if(value!=null){
                for(document in value.documents){
                    var chat = document.toObject(Chat::class.java)
                    var size = Chat.getInstance().get(roomId)?.size
                    if (size != 0) {
                        if (size != null) {
                            if(chat?.timeStamp !=Chat.getInstance().get(roomId)?.get(size-1)?.timeStamp){
                                if (chat != null) {
                                    msg.value = chat!!
                                    println("snapShot이 움직임!!")
                                    setReadTrue(roomId)
                                }
                            }
                        }
                    }else{
                        msg.value = chat!!
                    }
                }
            }

        }
    }

    fun pushToken(user:User, message : String){
        var pushRepository = FcmRepository()
        var message = PushMessage("${User.getInstance().nickName}",talk.get().toString(),user.pushToken)

        println(message.body)
        pushRepository.sendPushMessage(message)
    }
}