package com.moa.moakotlin.recyclerview.voice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import android.view.View
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.VoiceUser
import com.moa.moakotlin.databinding.ItemVoiceUserBinding

class NewVoiceRoomAdapter() : RecyclerView.Adapter<NewVoiceRoomAdapter.NewRoomVoiceHolder>() {

    var list = ArrayList<String>()
    var talking = ArrayList<String>()
    var map = HashMap<String,VoiceUser>()

    lateinit var mListener: OnItemClickListener

    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewVoiceRoomAdapter.NewRoomVoiceHolder {
        return NewRoomVoiceHolder(ItemVoiceUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: NewVoiceRoomAdapter.NewRoomVoiceHolder, position: Int) {
        holder.binding(list.get(position))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class NewRoomVoiceHolder(var binding : ItemVoiceUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun binding(phoneNumber: String) {

            var voiceUser = map.get(phoneNumber)

            binding.ItemVoiceUserImage.setOnClickListener(ButtonClick())
            binding.ItemVoiceUserNickName.text = voiceUser?.nickName

            if (talking.contains("${voiceUser?.phoneNumber}").not()) {
                binding.ItemVoiceUserBorder.setBackgroundResource(R.drawable.shape_oval_white)
            } else {
                binding.ItemVoiceUserBorder.setBackgroundResource(R.drawable.shape_oval_white_border_main_color)
            }

            if (voiceUser?.profileImage?.isNotEmpty() == true) {
                println("여기 이미지 널아님 ")
                Glide.with(binding.root).load(voiceUser?.profileImage).into(binding.ItemVoiceUserImage)
            }
            if (voiceUser?.role.equals("owner").not()) {
                binding.ItemVoiceUserRoomMakerImg.isVisible = false
            }
        }


        inner class ButtonClick : View.OnClickListener {
            override fun onClick(v: View?) {
                if (mListener != null) {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        if (v != null) {
                            mListener!!.onItemClick(v, adapterPosition)
                        }
                    }
                }

            }
        }
    }
        }