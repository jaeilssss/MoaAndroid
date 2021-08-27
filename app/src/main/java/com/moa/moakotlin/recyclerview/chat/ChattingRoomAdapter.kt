package com.moa.moakotlin.recyclerview.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.ChattingRoom
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChattingRoomAdapter(var context: Context,var list: ArrayList<ChattingRoom>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    lateinit var roomList: ArrayList<String>
    var userList  = HashMap<Int,User>()
    var i  = 0
    var defaultImagePath ="https://firebasestorage.googleapis.com/v0/b/moakr-8c0ab.appspot.com/o/MoAImages%2FCONTENT_DEFAULT_200x200.png?alt=media&token=3e986a1b-7c21-4d89-98e8-85e6db5b7b5b"
    lateinit var mListener : OnItemClickListener


    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.chatting_room_item,parent,false)
        var viewholder = ChattingRoomViewHolder(view, this)
        return viewholder
    }
    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var response = list.get(position)
        if(holder is ChattingRoomViewHolder){
            holder.bind(response)
            var repository = UserRepository()
            CoroutineScope(Dispatchers.Main).launch {
                var user = repository.getUserInfo(response.opponentUid)
                if(user==null){
                    user = User()
                    user.uid = "-1"
                    user.nickName = "알수없음"
                    user.profileImage = defaultImagePath
                }
                    holder.nickname.text = user?.nickName
                val sdf = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm ")
                var date = sdf.format(response.timeStamp.toDate())
                    holder.latest_time.text = date
                if(user==null){
                }
                if (user != null) {
                    userList.put(position,user)
                }
                if(user!=null && user.profileImage!=null){
                    Glide.with(context).load(user.profileImage).into(holder.profile_Image)
                }
            }
        }
    }
    fun addList(chattingRoom: ChattingRoom){
        list.add(chattingRoom)
    }
    fun resetting(){
        notifyDataSetChanged()
    }
    inner class ChattingRoomViewHolder(var view : View, var adapter: ChattingRoomAdapter): RecyclerView.ViewHolder(view),View.OnClickListener {
            lateinit var latestMessage : TextView

            lateinit var latest_time : TextView

            lateinit var partner_name : TextView

            lateinit var profile_Image : ImageView

            lateinit var linearLayout: ConstraintLayout

            lateinit var readCheck : ImageView
            lateinit var nickname : TextView

        init {
                nickname = view.findViewById(R.id.itemChattingRoomNickname)
                latestMessage = view.findViewById(R.id.itemChattingRoomLatestMessage)
                latest_time = view.findViewById(R.id.itemChattingRoomDate)
                profile_Image = view.findViewById(R.id.itemChattingRoomProfile)
            linearLayout = view.findViewById(R.id.chatting_room_layout)
            readCheck = view.findViewById(R.id.chatting_room_item_read_check)
        }
            fun bind(chattingRoom: ChattingRoom){
                latestMessage = view.findViewById(R.id.itemChattingRoomLatestMessage)
                latestMessage.setText(chattingRoom.latestMessage)


                linearLayout.setOnClickListener(this)

                readCheck.isVisible = chattingRoom.isRead ==false
            }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.chatting_room_layout -> {
                    if(mListener!=null){
                        if(adapterPosition!=RecyclerView.NO_POSITION){
                            mListener?.onItemClick(view,adapterPosition)
                        }
                    }
                }
            }
        }
    }


}