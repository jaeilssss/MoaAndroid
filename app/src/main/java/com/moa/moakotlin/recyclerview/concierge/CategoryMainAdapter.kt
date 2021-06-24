package com.moa.moakotlin.recyclerview.concierge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.ItemCategoryMainBinding
import com.moa.moakotlin.databinding.ItemConciergeBinding

class CategoryMainAdapter(): ListAdapter<Needer, CategoryMainAdapter.CategoryViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMainAdapter.CategoryViewHolder {
        return CategoryViewHolder(ItemCategoryMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CategoryMainAdapter.CategoryViewHolder, position: Int) {

    }

    inner class CategoryViewHolder(binding : ItemCategoryMainBinding) : RecyclerView.ViewHolder(binding.root)

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