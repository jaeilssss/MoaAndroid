package com.moa.moakotlin.ui.imagepicker

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.Chat
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.recyclerview.imagepickrcv.ImagePickerAdapter
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.collections.ArrayList

class ImagePickerViewModel(
    navController: NavController,
    context: Context,
    var adapter: ImagePickerAdapter,
    var roomId : String,
    var otherId : String
) : BaseViewModel(navController){

    fun submit(){
        var bundle = Bundle()
      var picture = adapter.list.get(adapter.checkBox)

        var storageRef = FirebaseStorage.getInstance().getReference()

        var file = Uri.fromFile(File(picture))

        var inputstream = FileInputStream(File(picture))

        val riversRef = storageRef.child("chattingImage/" + file.lastPathSegment)

        val uploadTask = riversRef.putStream(inputstream)

        uploadTask.continueWithTask { riversRef.downloadUrl }.addOnCompleteListener { task ->
            val chat = Chat()
            chat.timeStamp = Timestamp.now()
            var user = User.getInstance()
            chat.uid = user.uid
            chat.talk = "사진을 보냈습니다"
            val images = ArrayList<String>()
            images.add(task.result.toString())
            chat.images = images
            var db = FirebaseFirestore.getInstance()
            navController.popBackStack()
                    //임시로 나의 pk와 상대 pk지정함

            db.collection("ChattingRoom").document(roomId)
                    .collection("Chat").add(chat)
                    .addOnCompleteListener(OnCompleteListener<DocumentReference?> {

                        var chattingRoom  = ChattingRoom()

                        chattingRoom.latestMessage = "사진을 보냈습니다"
                        chattingRoom.opponentUid = otherId

                        chattingRoom.timeStamp = Timestamp.now()
                        settingChattingRoomDb(user.uid,chattingRoom)
                        settingChattingRoomDb(otherId,chattingRoom)
                    })
        }
    }

    fun settingChattingRoomDb(myId:String ,chattingRoom: ChattingRoom){
        var firebase = FirebaseFirestore.getInstance()
        firebase.collection("User").document(myId)
                .collection("ChattingRoom").document(roomId)
                .set(chattingRoom).addOnCompleteListener {

                }
    }


}