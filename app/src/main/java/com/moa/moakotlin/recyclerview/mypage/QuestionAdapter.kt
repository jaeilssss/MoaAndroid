package com.moa.moakotlin.recyclerview.mypage


import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.AppQuestion
import com.moa.moakotlin.data.Review
import com.moa.moakotlin.databinding.ItemQuestionBinding
import java.text.SimpleDateFormat

class QuestionAdapter : ListAdapter<AppQuestion,QuestionAdapter.AppQuestionViewHolder>(diffUtil) {

    private var mListener : OnItemClickListener?=null

    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppQuestionViewHolder {

        return AppQuestionViewHolder(ItemQuestionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: AppQuestionViewHolder, position: Int) {


        holder.binding(currentList[position])
    }
    inner class AppQuestionViewHolder(var binding : ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root){


        fun binding(appQuestion: AppQuestion){
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
            binding.itemQuestionText.text= appQuestion.title
            binding.itemQuestionDate.text = dateFormat.format(appQuestion.timeStamp.toDate())

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
        val diffUtil = object : DiffUtil.ItemCallback<AppQuestion>(){
            override fun areItemsTheSame(oldItem: AppQuestion, newItem: AppQuestion): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: AppQuestion, newItem: AppQuestion): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

        }
    }

}