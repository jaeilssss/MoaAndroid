package com.moa.moakotlin.recyclerview.apt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.databinding.ItemMyNeighborhoodTextBinding
import com.moa.moakotlin.recyclerview.certification.CertificationImageAdapter

class MyNeighborhoodListAdapter() : ListAdapter<String,MyNeighborhoodListAdapter.NeighborhoodViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNeighborhoodListAdapter.NeighborhoodViewHolder {
        return NeighborhoodViewHolder(ItemMyNeighborhoodTextBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyNeighborhoodListAdapter.NeighborhoodViewHolder, position: Int) {
       println("야")
        holder.binding(currentList[position])
    }

    inner class NeighborhoodViewHolder(var binding:ItemMyNeighborhoodTextBinding) : RecyclerView.ViewHolder(binding.root){

        fun binding(aptName : String){
            binding.itemMyNeighborhoodText.text = "3333"
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