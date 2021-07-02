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

class ChattingRoomAdapter(var navconroller: NavController, var context: Context, var list: ArrayList<ChattingRoom>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    lateinit var roomList: ArrayList<String>
    var userList  = HashMap<Int,User>()
    var i  = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.chatting_room_item,parent,false)
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
                    holder.nickname.text = user?.nickName
                val sdf = SimpleDateFormat("dd/MM/yy hh:mm a")
                var date = sdf.format(response.timeStamp.toDate())
                    holder.latest_time.text = date
                if(user==null){
                }
                if (user != null) {
                    userList.put(position,user)
                }
                if(user!=null && user.profileImage!=null){
                    Glide.with(context).load(user.profileImage).into(holder.profile_Image)
                }else if(user?.profileImage==null){
                    holder.profile_Image.setImageResource(R.drawable.profile_human)
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

    fun goToChat(position: Int){
        var bundle  = Bundle()
        bundle.putString("roomId",roomList.get(position))
        bundle.putParcelable("opponentUser",userList.get(position))
        navconroller.navigate(R.id.action_chattingRoomFragment_to_ChatFragment,bundle)
    }
    class ChattingRoomViewHolder(var view : View, var adapter: ChattingRoomAdapter): RecyclerView.ViewHolder(view),View.OnClickListener {
            lateinit var latestMessage : TextView

            lateinit var latest_time : TextView

            lateinit var partner_name : TextView

            lateinit var profile_Image : ImageView

            lateinit var linearLayout: ConstraintLayout

            lateinit var readCheck : ImageView
            lateinit var nickname : TextView

        init {

        }
            fun bind(chattingRoom: ChattingRoom){

                latestMessage.setText(chattingRoom.latestMessage)

                linearLayout = view.findViewById(R.id.chatting_room_layout)
                linearLayout.setOnClickListener(this)

                readCheck = view.findViewById(R.id.chatting_room_item_read_check)

                if(chattingRoom.isRead ==false){
                    println("true 로 바뀜")
                    readCheck.isVisible = true
                }else{
                    println("false 로 바뀜")
                    readCheck.isVisible = false
                }
            }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.chatting_room_layout -> adapter.goToChat(adapterPosition)
            }
        }
    }


}