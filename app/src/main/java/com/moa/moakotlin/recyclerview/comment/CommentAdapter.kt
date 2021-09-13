package com.moa.moakotlin.recyclerview.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.data.Comment
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.databinding.ItemClaimCommentBinding

class CommentAdapter :ListAdapter<Comment ,CommentAdapter.CommentViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CommentViewHolder {

        return CommentViewHolder(ItemClaimCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {

    }

    inner class CommentViewHolder(var binding: ItemClaimCommentBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun getItemCount(): Int {
//        return super.getItemCount()
        return 100
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Comment>(){
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

        }
    }

}