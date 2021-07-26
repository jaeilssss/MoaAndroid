package com.moa.moakotlin.ui.bottomsheet

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.custom.AptCertificationImageAlertDialog
import com.moa.moakotlin.data.VoiceChatRoom
import com.moa.moakotlin.data.VoiceUser
import de.hdodenhof.circleimageview.CircleImageView

class VoiceRoomUserProfileBottomSheet(var voiceUser : VoiceUser ,var voiceChatRoom: VoiceChatRoom ,var myVoiceUser : VoiceUser , val itemCLick: (Int) -> Unit): BottomSheetDialogFragment() {


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.bottom_sheet_voice_user, container,false)

        var deprive : TextView = view.findViewById(R.id.bottomSheetVoiceUserDeprive)

        if(myVoiceUser.role == "owner" && voiceUser.role=="speaker"){

            deprive.isClickable =true
            deprive.setTextColor(Color.BLACK)
        }


        view.findViewById<TextView>(R.id.bottomSheetNickName).text = voiceUser.nickName
        var image = view.findViewById<CircleImageView>(R.id.bottomSheetUserProfile)
        if(!voiceUser.profileImage.isNotEmpty()){
            Glide.with(view).load(voiceUser.profileImage).into(image)
        }
        deprive.setOnClickListener {

            if((myVoiceUser.role == "owner") && voiceUser.role=="speaker"){
                context?.let {
                    AptCertificationImageAlertDialog(it)
                            .setMessage("${voiceUser.nickName}님에게 발언권을 박탈 하시겠습니까?")
                            .setPositiveButton("네") {

                                var db = FirebaseFirestore.getInstance()

                                db.collection("VoiceChatRoom")
                                        .document(voiceChatRoom.documentID)
                                        .collection("VoiceUser")
                                        .document(voiceUser.uid)
                                        .update("role","audience")

                                dismiss()
                            }.setNegativeButton { dismiss() }
                            .show()
                }
            }
        }
        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}