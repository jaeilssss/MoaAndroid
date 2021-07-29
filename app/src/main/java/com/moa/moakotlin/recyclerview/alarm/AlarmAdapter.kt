package com.moa.moakotlin.recyclerview.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.Notification
import com.moa.moakotlin.databinding.ItemAlarmBinding
import java.text.SimpleDateFormat

class AlarmAdapter : ListAdapter<Notification,AlarmAdapter.AlarmViewHoler >(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmAdapter.AlarmViewHoler {
        return AlarmViewHoler(ItemAlarmBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: AlarmAdapter.AlarmViewHoler, position: Int) {
        holder.binding(currentList[position])
    }


    inner class AlarmViewHoler(var binding : ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(notification: Notification){

            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm")
            binding.itemAlarmTitle.text = notification.title
            binding.itemAlarmDate.text = dateFormat.format(notification.timeStamp.toDate())
            if(notification.image.isNotEmpty()){
                Glide.with(binding.root).load(notification.image).into(binding.itemAlarmProfileImg)
            }
        }
    }
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Notification>(){
            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem.documentID == newItem.documentID
            }

        }
    }
}