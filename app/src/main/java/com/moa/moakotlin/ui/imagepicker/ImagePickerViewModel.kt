package com.moa.moakotlin.ui.imagepicker

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.Chat
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.recyclerview.imagepickrcv.ImagePickerAdapter
import com.moa.moakotlin.repository.chat.ChatRepository
import com.moa.moakotlin.repository.imagePicker.ImagePickerRepository
import kotlin.collections.ArrayList

class ImagePickerViewModel() : ViewModel() {

    suspend fun submit(path: String,roomId: String,opponentUid: String) :Boolean {


        var db = FirebaseFirestore.getInstance()
        //임시로 나의 pk와 상대 pk지정함
        var result = false
        var chatImagePickerRepository = ImagePickerRepository()
        var image = chatImagePickerRepository.upload("chattingImages/",path)
        val chat = Chat()
        chat.timeStamp = Timestamp.now()
        var user = User.getInstance()
        chat.uid = user.uid
        chat.talk = "사진을 보냈습니다"
        val images = ArrayList<String>()
        if (image != null) {
            result =true
            images.add(image)
            chat.images = images
        }
             var chatRepository = ChatRepository(roomId)
                 chatRepository.send(chat,opponentUid)
        return result

        db.collection("ChattingRoom").document(roomId)
                .collection("Chat").add(chat)
                .addOnCompleteListener(OnCompleteListener<DocumentReference?> {

                    var chattingRoom = ChattingRoom()

                    chattingRoom.latestMessage = "사진을 보냈습니다"
                    chattingRoom.opponentUid = opponentUid

                    chattingRoom.timeStamp = Timestamp.now()
                    settingChattingRoomDb(user.uid, chattingRoom,roomId)
                    settingChattingRoomDb(opponentUid, chattingRoom,roomId)
                })
    }


    fun settingChattingRoomDb(myId: String, chattingRoom: ChattingRoom,roomId: String) {
        var firebase = FirebaseFirestore.getInstance()
        firebase.collection("User").document(myId)
                .collection("ChattingRoom").document(roomId)
                .set(chattingRoom).addOnCompleteListener {

                }
    }


}