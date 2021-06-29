package com.moa.moakotlin.viewpageradapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R

class ConciergeReadViewpagerAdapter(var context : Context , var list : ArrayList<String>) :  RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_concierge_read_viewpager,parent,false)
        return ConciergeReadViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        Glide.with(context).load(list.get(position)).into((holder as ConciergeReadViewHolder).image)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ConciergeReadViewHolder(var view : View) : RecyclerView.ViewHolder(view){
        var image : ImageView = view.findViewById(R.id.itemConciergeReadImage)
    }
}