package com.moa.moakotlin.recyclerview.concierge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.ItemConciergeBinding
import java.text.SimpleDateFormat

class NeederMainAdapter() : ListAdapter<Needer, NeederMainAdapter.ConciergeViewHolder>(diffUtil) {

    private var mListener : OnItemClickListener?=null


    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NeederMainAdapter.ConciergeViewHolder {
        return ConciergeViewHolder(ItemConciergeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NeederMainAdapter.ConciergeViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    inner class ConciergeViewHolder(var binding: ItemConciergeBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(helper: Needer){
            if(helper.images?.size!!>0){
                Glide.with(binding.root).load(helper.images?.get(0)).apply(
                    RequestOptions.bitmapTransform(
                        RoundedCorners(10)
                    )).into(binding.itemConciergeImage)
            }
            if(helper.isNego.not()){
                binding.itemConciergeNego.isVisible = false
            }
            binding.itemConciergeHopeWage.text = helper.hopeWage
            binding.itemConciergeContent.text = helper.content
            binding.itemConciergeAptName.text = helper.aptName
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm")

            binding.itemConciergeDate.text =  dateFormat.format(helper.timeStamp.toDate())

            binding.itemConciergeLayout.setOnClickListener(ButtonClick())
        }

        inner class ButtonClick : View.OnClickListener{
            override fun onClick(v: View?) {
                when(v?.id){
                    R.id.itemConciergeLayout ->{
                        if(mListener!=null){
                            if(adapterPosition != RecyclerView.NO_POSITION){
                                mListener!!.onItemClick(v,adapterPosition)
                            }
                        }
                    }
                }
            }

        }

    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Needer>(){
            override fun areItemsTheSame(oldItem: Needer, newItem: Needer): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Needer, newItem: Needer): Boolean {
                return oldItem.documentID == newItem.documentID
            }

        }
    }
}