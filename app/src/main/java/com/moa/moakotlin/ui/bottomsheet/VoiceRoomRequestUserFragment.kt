package com.moa.moakotlin.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.custom.AptCertificationImageAlertDialog
import com.moa.moakotlin.data.RequestUser
import com.moa.moakotlin.recyclerview.bottom.VoiceRoomRequestAdapter

class VoiceRoomRequestUserFragment(var documentID : String  , val itemCLick: (Int) -> Unit): BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.bottom_sheet_voice_room_request_user, container,false)

        var rcv = view.findViewById<RecyclerView>(R.id.BottomSheetRequestUserRcv)

        var adapter = VoiceRoomRequestAdapter()

        var db = FirebaseFirestore.getInstance()

        var list = ArrayList<RequestUser>()

        db.collection("VoiceChatRoom").document(documentID)
                .collection("RequestUser")
                .get()
                .addOnSuccessListener {
                    for(document in  it.documents){

                        var requestUser  = document.toObject(RequestUser::class.java)

                        if (requestUser != null) {

                            list.add(requestUser)
                            
                        }

                    }

                    rcv.adapter= adapter

                    adapter.submitList(list)

                    rcv.layoutManager = LinearLayoutManager(activity)
                }

        adapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {

                context?.let {
                    AptCertificationImageAlertDialog(it)
                            .setMessage("${adapter.currentList[position].nickName}님에게 발언권을 부여하시겠습니까?")
                            .setPositiveButton("네") {

                                var db = FirebaseFirestore.getInstance()

                                db.collection("VoiceChatRoom")
                                        .document(documentID)
                                        .collection("VoiceUser")
                                        .document(adapter.currentList[position].uid)
                                        .update("role","speaker")

                                db.collection("VoiceChatRoom")
                                        .document(documentID)
                                        .collection("RequestUser")
                                        .document(adapter.currentList[position].uid)
                                        .delete()
                                dismiss()
                            }.setNegativeButton { dismiss() }
                            .show()
                }
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}