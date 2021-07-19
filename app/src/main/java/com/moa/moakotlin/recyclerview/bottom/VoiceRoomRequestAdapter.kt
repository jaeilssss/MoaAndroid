package com.moa.moakotlin.recyclerview.bottom

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
import com.moa.moakotlin.data.RequestUser
import com.moa.moakotlin.databinding.ItemRequestUserBinding

class VoiceRoomRequestAdapter : ListAdapter<RequestUser,VoiceRoomRequestAdapter.RequestViewHolder>(diffUtil) {


    private lateinit var mListener : OnItemClickListener

    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
       return RequestViewHolder(ItemRequestUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {

        holder.binding(currentList[position])
    }
    inner class RequestViewHolder(var binding : ItemRequestUserBinding) : RecyclerView.ViewHolder(binding.root){


        fun binding(requestUser: RequestUser){
            if(requestUser.profileImage.isNotEmpty()){
                Glide.with(binding.root).load(requestUser.profileImage).into(binding.itemRequestUserProfile)
            }
            binding.itemRequestUserNickName.text = requestUser.nickName

            binding.itemRequestUserBtn.setOnClickListener(ButtonClick())
        }

        inner class ButtonClick : View.OnClickListener{
            override fun onClick(v: View?) {
                when(v?.id){
                    R.id.itemRequestUserBtn ->{
                        if(mListener!=null){
                            if(adapterPosition != RecyclerView.NO_POSITION){

                                mListener!!.onItemClick(v,adapterPosition)
                            }else{

                            }
                        }else{

                        }
                    }
                }
            }

        }
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<RequestUser>(){
            override fun areItemsTheSame(oldItem: RequestUser, newItem: RequestUser): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: RequestUser, newItem: RequestUser): Boolean {
                return oldItem.uid == newItem.uid
            }

        }
    }


}