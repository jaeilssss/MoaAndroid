package com.moa.moakotlin.recyclerview.chat

import android.content.Context
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.data.User
import com.moa.moakotlin.data.Chat
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(var navController: NavController, var context: Context,var list: ArrayList<Chat>,var opponentUser : User) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {
        var user = User.getInstance()
        var chat = list.get(position)
        if(chat.uid.equals(user.uid)){
            // 0 과 2는 나
            if(chat.images!=null){
                return 2
            }else{
                return 0;
            }
        }else{
            if(chat.images!=null){
                return 3
            }else{
                return 1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view : View
            if(viewType==0){
                view = View.inflate(context , R.layout.chat_right_item,null)
                return rightViewHolder(view,this,context)
            }else if(viewType==1){
                view = View.inflate(context , R.layout.chat_left_item,null)
                return leftViewHolder(view,this,context)
            }else if(viewType==2){
                view = View.inflate(context,R.layout.chat_image_right_item,null)
                return rightImageViewHolder(view,this,context)
            }else{
                view = View.inflate(context,R.layout.chat_image_left_item,null)
                return leftImageViewHoldfer(view,this,context)
            }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun scrollUpdate(){
        notifyDataSetChanged()
    }
    fun joined(newList :ArrayList<Chat>){
        newList.addAll(list)
        var size = list.size
        list.clear()
        list.addAll(newList)
        scrollUpdate()
    }
    fun addChat(chat : Chat){
        list.add(chat)
    }
    fun changeSetting(){
        notifyDataSetChanged()
    }
    fun reverseList(){
        Collections.reverse(list)
    }
    fun resetting(){
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var chat = list.get(position)
        if(holder is rightViewHolder){

            holder.bind(chat)
        }else if(holder is leftViewHolder){

            holder.bind(chat)
        }else if(holder is rightImageViewHolder){


            holder.bind(chat)
        }else if(holder is leftImageViewHoldfer){
            holder.bind(chat)
        }
    }

    class leftViewHolder(var view : View,var adapter: ChatAdapter,var context: Context) : RecyclerView.ViewHolder(view) {
      lateinit var talk : TextView

        lateinit var nickname : TextView
        init {
            talk = view.findViewById(R.id.itemLeftChatMessage)
        }
            fun bind(chat : Chat){

                talk.setText(chat.talk)
            }
    }
    class rightViewHolder(var view : View , var adapter: ChatAdapter,var context: Context) : RecyclerView.ViewHolder(view){
        lateinit var talk : TextView

        init {

        }
        fun bind(chat : Chat){
            talk = view.findViewById(R.id.itemRightChatMessage)
            talk.setText(chat.talk)
        }
    }
    class rightImageViewHolder(var view : View , var adapter: ChatAdapter,var context: Context) : RecyclerView.ViewHolder(view){
        lateinit var image : ImageView
        lateinit var profile : ImageView
        lateinit var nickname : TextView
        init {
            profile = view.findViewById(R.id.chat_image_right_profile)
            nickname = view.findViewById(R.id.TextView_nickname)
        }
        fun bind(chat : Chat){
            image = view.findViewById(R.id.chat_image_right_image)
            Glide.with(context).load(chat.images?.get(0)).into(image)
        }
    }
    class leftImageViewHoldfer(var view : View , var adapter: ChatAdapter,var context: Context) : RecyclerView.ViewHolder(view){
        lateinit var image : ImageView
        lateinit var profile : ImageView
        lateinit var nickname : TextView
        init {
            profile = view.findViewById(R.id.chat_image_left_profile)
            nickname = view.findViewById(R.id.TextView_nickname)
        }
        fun bind(chat : Chat){
            image = view.findViewById(R.id.chat_image_left_image)
            Glide.with(context).load(chat.images?.get(0)).into(image)
        }
    }
}