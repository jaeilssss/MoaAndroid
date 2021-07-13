package com.moa.moakotlin.recyclerview.imagepickrcv

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
class SinglePickerAdapter(var list : ArrayList<String>) :RecyclerView.Adapter<SinglePickerAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SinglePickerAdapter.ImageViewHolder {
        TODO("Not yet implemented")
    }


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }


}