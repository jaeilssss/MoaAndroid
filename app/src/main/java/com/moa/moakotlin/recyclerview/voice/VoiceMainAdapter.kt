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
    var defaultUrl ="https://firebasestorage.googleapis.com/v0/b/moakr-8c0ab.appspot.com/o/CONCIERGE_DEFAULT_200x200.png?alt=media&token=8ba33ee5-1b36-4d39-b400-6af648439187"
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
            }else{
                Glide.with(binding.root).load(defaultUrl).into(binding.itemVoiceMainImage)
            }
            binding.itemVoiceMainRoomMakerNickname.text = voiceChatRoom.nickName
            binding.itemVoiceMainTitle.text = "[${voiceChatRoom.topic}] ${voiceChatRoom.name}"
            binding.itemVoiceMainRange.text = voiceChatRoom.range
            binding.itemVoiceMainPeopleCount.text = voiceChatRoom.peopleCount.toString()
            binding.itemVoiceMainSpeakerCount.text = voiceChatRoom.speakersCount.toString()
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