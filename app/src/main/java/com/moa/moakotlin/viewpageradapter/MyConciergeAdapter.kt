package com.moa.moakotlin.viewpageradapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.databinding.MyConciergeViewPagerItemFragmentBinding

class MyConciergeAdapter<T>(var list : ArrayList<T>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyConciergeViewHolder(var binding: MyConciergeViewPagerItemFragmentBinding) :RecyclerView.ViewHolder(binding.root){

    }
}