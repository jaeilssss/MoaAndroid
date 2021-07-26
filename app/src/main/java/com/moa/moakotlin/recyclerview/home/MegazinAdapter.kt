package com.moa.moakotlin.recyclerview.home

import android.app.ActionBar
import android.graphics.Color
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.moa.moakotlin.custom.OutlineTextView
import com.moa.moakotlin.data.Magazine
import com.moa.moakotlin.data.Megazin
import com.moa.moakotlin.databinding.ItemMoaMegazinBinding

class MegazinAdapter() : ListAdapter<Magazine, MegazinAdapter.MegazinViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MegazinViewHolder {
        return MegazinViewHolder(ItemMoaMegazinBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MegazinViewHolder, position: Int) {
        holder.binding(currentList[position],position)
    }

    inner class MegazinViewHolder(var binding : ItemMoaMegazinBinding) : RecyclerView.ViewHolder(binding.root){

        // item 따로 만드는 걸로 ,..
        fun binding(magazine:Magazine, position: Int){

               Glide.with(binding.root).load(magazine.thumbnail)
                       .apply(RequestOptions.bitmapTransform(RoundedCorners(25)))
                               .into(binding.itemMegazinImage)
            binding.itemMoaMegazinText.text=magazine.title

        }

    }
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Magazine>(){
            override fun areItemsTheSame(oldItem: Magazine, newItem: Magazine): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Magazine, newItem: Magazine): Boolean {

                // 나중에 수정할 것 !


                return oldItem .equals(newItem)
//                return oldItem.id == newItem.id
            }

        }
    }
}