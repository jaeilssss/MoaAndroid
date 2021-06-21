package com.moa.moakotlin.recyclerview.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.data.Megazin
import com.moa.moakotlin.databinding.ItemMoaMegazinBinding

class MegazinAdapter() : ListAdapter<Megazin, MegazinAdapter.MegazinViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MegazinViewHolder {
        return MegazinViewHolder(ItemMoaMegazinBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MegazinViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    inner class MegazinViewHolder(binding : ItemMoaMegazinBinding) : RecyclerView.ViewHolder(binding.root){

        fun binding(megazin:Megazin){


        }

    }
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Megazin>(){
            override fun areItemsTheSame(oldItem: Megazin, newItem: Megazin): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Megazin, newItem: Megazin): Boolean {

                // 나중에 수정할 것 !


                return oldItem .equals(newItem)
//                return oldItem.id == newItem.id
            }

        }
    }
}