package com.moa.moakotlin.recyclerview.voice

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.VoiceChatRoom
import com.moa.moakotlin.databinding.ItemVoiceMainBinding


class VoiceMainAdapter() : ListAdapter<VoiceChatRoom, VoiceMainAdapter.VoiceMainViewHolder>(diffUtil){


    private var mListener : OnItemClickListener ?=null

    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceMainViewHolder {
        return VoiceMainViewHolder(ItemVoiceMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: VoiceMainViewHolder, position: Int) {
        holder.binding(currentList[position])
    }
    inner class VoiceMainViewHolder(var binding : ItemVoiceMainBinding) : RecyclerView.ViewHolder(binding.root){


        fun binding(voiceChatRoom: VoiceChatRoom){
            if(voiceChatRoom.image.isNotEmpty()){
                Glide.with(binding.root).load(voiceChatRoom.image).into(binding.itemVoiceMainImage)
            }
            binding.itemVoiceMainRoomMakerNickname.text = voiceChatRoom.nickName
            binding.itemVoiceMainTitle.text = "[${voiceChatRoom.topic}] ${voiceChatRoom.name}"
            binding.itemVoiceMainRange.text = voiceChatRoom.range

            binding.itemVoiceMainLayout.setOnClickListener(ButtonClick())
        }
        inner class ButtonClick : View.OnClickListener{
            override fun onClick(v: View?) {
                if(mListener!=null){
                    if(adapterPosition != RecyclerView.NO_POSITION){
                        if (v != null) {
                            mListener!!.onItemClick(v,adapterPosition)
                        }
                    }
            }

        }
    }



    }
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<VoiceChatRoom>(){
            override fun areItemsTheSame(oldItem: VoiceChatRoom, newItem: VoiceChatRoom): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: VoiceChatRoom, newItem: VoiceChatRoom): Boolean {
                return oldItem.documentID == newItem.documentID
            }

        }
    }


}