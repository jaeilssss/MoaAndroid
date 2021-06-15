package com.moa.moakotlin.recyclerview.algoria

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.data.Apt
import com.moa.moakotlin.databinding.ItemAptBinding

class SearchAptAdapter() : ListAdapter<Apt,SearchAptAdapter.SearchAptViewModel>(diffUtil){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAptViewModel {
        return SearchAptViewModel(ItemAptBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SearchAptViewModel, position: Int) {
        holder.binding(currentList[position])
    }
    inner class SearchAptViewModel(private val binding : ItemAptBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(apt : Apt){
            binding.itemAptName.text = apt.aptName
            binding.aptJuso.text = apt.address
        }
    }
    companion object{
        val diffUtil = object  : DiffUtil.ItemCallback<Apt>(){
            override fun areItemsTheSame(oldItem: Apt, newItem: Apt): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Apt, newItem: Apt): Boolean {
                return oldItem.aptCode == newItem.aptCode
            }

        }
    }


}