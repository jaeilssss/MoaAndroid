package com.moa.moakotlin.recyclerview.kid

import android.os.Bundle
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.R
import com.moa.moakotlin.data.ChattingRoom
import kotlinx.android.synthetic.main.chatting_room_item.view.*

class KidHireSuccessAdapter(var navController: NavController,var list : ArrayList<ChattingRoom>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = View.inflate(parent.context,R.layout.chatting_room_item,null)

        return KidHireSuccessViewHolder(view,this)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       holder as KidHireSuccessViewHolder

        holder.latestMessage.text = list.get(position).latestMessage

    }
    fun goToReview(position: Int){
        var bundle = Bundle()

        bundle.putString("opponentUid",list.get(position).opponentUid)

    }
    class KidHireSuccessViewHolder(view : View ,var adapter : KidHireSuccessAdapter) : RecyclerView.ViewHolder(view),View.OnClickListener{
         var latestMessage : TextView

         var latest_time : TextView

         var partner_name : TextView

         var profile_Image : ImageView

         var linearLayout: ConstraintLayout
         var readCheck : ImageView
        init {
            latestMessage = view.findViewById(R.id.chatting_room_item_latest_message)

            latest_time = view.findViewById(R.id.chatting_room_item_date)

            partner_name = view.findViewById(R.id.chatting_room_item_nickname)

            profile_Image = view.findViewById(R.id.chatting_room_item_profile)

            linearLayout = view.findViewById(R.id.chatting_room_layout)

            readCheck = view.findViewById(R.id.chatting_room_item_read_check)

            linearLayout.setOnClickListener(this)


        }

        override fun onClick(v: View?) {
           when(v?.id){
               R.id.chatting_room_layout ->{
                   adapter.goToReview(adapterPosition)
               }
           }
        }
    }


}