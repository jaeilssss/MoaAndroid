package com.moa.moakotlin.recyclerview.concierge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.databinding.ItemConciergeBinding
import java.text.SimpleDateFormat

class HelperMainAdapter() :ListAdapter<Helper, HelperMainAdapter.ConciergeViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelperMainAdapter.ConciergeViewHolder {
        return ConciergeViewHolder(ItemConciergeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HelperMainAdapter.ConciergeViewHolder, position: Int) {
            holder.binding(currentList[position])
    }

    inner class ConciergeViewHolder(var binding: ItemConciergeBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(helper: Helper){
            if(helper.images?.size!!>0){
                Glide.with(binding.root).load(helper.images?.get(0)).apply(RequestOptions.bitmapTransform(RoundedCorners(10))).into(binding.itemConciergeImage)
            }
            if(helper.isNego.not()){
                binding.itemConciergeNego.isVisible = false
            }
            binding.itemConciergeHopeWage.text = helper.hopeWage
            binding.itemConciergeContent.text = helper.content
            binding.itemConciergeAptName.text = helper.aptName
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")

            binding.itemConciergeDate.text =  dateFormat.format(helper.timeStamp.toDate())

        }
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Helper>(){
            override fun areItemsTheSame(oldItem: Helper, newItem: Helper): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Helper, newItem: Helper): Boolean {
                return oldItem.documentID == newItem.documentID
            }

        }
    }
}