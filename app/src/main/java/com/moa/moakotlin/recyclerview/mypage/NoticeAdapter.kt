package com.moa.moakotlin.recyclerview.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.AppQuestion
import com.moa.moakotlin.data.Notice
import com.moa.moakotlin.databinding.ItemQuestionBinding
import java.text.SimpleDateFormat

class NoticeAdapter: ListAdapter<Notice, NoticeAdapter.NoticeViewHolder>(diffUtil) {

    private var mListener : OnItemClickListener?=null

    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {

        return NoticeViewHolder(ItemQuestionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {


        holder.binding(currentList[position])
    }
    inner class NoticeViewHolder(var binding : ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root){


        fun binding(notice: Notice){
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
            binding.itemQuestionText.text = notice.title
            binding.itemQuestionDate.text = dateFormat.format(notice.timeStamp.toDate())

            binding.itemQuestionLayout.setOnClickListener(ButtonClick())
        }

        inner class ButtonClick() : View.OnClickListener{
            override fun onClick(v: View?) {
                if(mListener!=null){
                    if(v!=null){
                        mListener!!.onItemClick(v,adapterPosition)
                    }
                }
            }
        }
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Notice>(){
            override fun areItemsTheSame(oldItem: Notice, newItem: Notice): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Notice, newItem: Notice): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

        }
    }

}