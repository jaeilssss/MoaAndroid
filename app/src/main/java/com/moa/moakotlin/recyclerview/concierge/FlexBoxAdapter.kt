package com.moa.moakotlin.recyclerview.concierge

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.databinding.ItemConciergeBinding
import com.moa.moakotlin.databinding.ItemSubCategoryBinding

class FlexBoxAdapter() : ListAdapter<String, FlexBoxAdapter.FlexViewHolder>(diffUtil) {

    private var mListener : OnItemClickListener ?= null

    var checked = -1

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlexViewHolder {
        return FlexViewHolder(ItemSubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))    }

    override fun onBindViewHolder(holder: FlexViewHolder, position: Int) {
        holder.binding(currentList[position])
    }
    inner class FlexViewHolder(var binding : ItemSubCategoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(subCategory : String){
            if(checked ==adapterPosition) {
                binding.itemSubCategory.setBackgroundResource(R.drawable.shape_main_color_radius_25)
            }else{
                binding.itemSubCategory.setBackgroundResource(R.drawable.shape_f7f7f7_radius_25)
            }
            binding.itemSubCategory.text = subCategory
            binding.itemSubCategory.setOnClickListener(ButtonClick())
        }

        inner class ButtonClick() : View.OnClickListener{
            override fun onClick(v: View?) {
                when(v?.id){
                    R.id.itemSubCategory ->{
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
        val diffUtil = object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }


}