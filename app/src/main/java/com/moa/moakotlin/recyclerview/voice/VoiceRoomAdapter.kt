package com.moa.moakotlin.recyclerview.voice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.data.VoiceChatRoom
import com.moa.moakotlin.data.VoiceUser
import com.moa.moakotlin.databinding.ItemVoiceUserBinding

class VoiceRoomAdapter() : ListAdapter<String, VoiceRoomAdapter.VoiceRoomViewHolder>(diffUtil){

    var map  = HashMap<String,VoiceUser>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceRoomViewHolder {
        return VoiceRoomViewHolder(ItemVoiceUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: VoiceRoomViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class VoiceRoomViewHolder(var binding : ItemVoiceUserBinding) : RecyclerView.ViewHolder(binding.root){

        fun binding(phoneNumber : String){
            var voiceUser = map.get(phoneNumber)
            binding.ItemVoiceUserNickName.text = voiceUser?.nickName
            if(voiceUser?.profileImage?.isNotEmpty() == true){
                Glide.with(binding.root).load(voiceUser?.profileImage).into(binding.ItemVoiceUserImage)
            }
            if(voiceUser?.role.equals("owner").not()){
                binding.ItemVoiceUserRoomMakerImg.isVisible = false
            }
        }
    }
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }
}