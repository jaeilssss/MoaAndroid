package com.moa.moakotlin.recyclerview.complaint


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.databinding.ItemClaimBinding
import com.moa.moakotlin.databinding.ItemConciergeBinding

class ComplaintAdapter : ListAdapter<Complaint, ComplaintAdapter.complaintViewHolder>(diffUtil){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintAdapter.complaintViewHolder {
        return complaintViewHolder(ItemClaimBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ComplaintAdapter.complaintViewHolder, position: Int) {
      holder.binding(currentList[position])
    }

    inner class complaintViewHolder(var binding : ItemClaimBinding ) : RecyclerView.ViewHolder(binding.root){

        fun binding(complaint: Complaint){
            binding.itemClaimTitle.text = complaint.title
            binding.itemClaimCategory.text = complaint.category
            binding.itemClaimNickName.text = complaint.uid
            if(complaint.images.size!=0){
                Glide.with(binding.root).load(complaint.images[0]).into(binding.itemClaimImage)
            }
        }
    }
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Complaint>(){
            override fun areItemsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

        }
    }


}