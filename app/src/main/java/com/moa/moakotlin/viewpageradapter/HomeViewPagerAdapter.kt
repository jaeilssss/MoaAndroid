package com.moa.moakotlin.viewpageradapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Banner

class HomeViewPagerAdapter(var list : ArrayList<Int>) :  RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_viewpager,parent,false)
        return HomeViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


//        (holder as HomeViewViewHolder).binding(list[position].image)
        (holder as HomeViewViewHolder).image.setImageResource(list.get(position))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class HomeViewViewHolder(var view : View) : RecyclerView.ViewHolder(view){
        var image : ImageView = view.findViewById(R.id.homeViewPagerImage)

        fun binding(path : String){
            Glide.with(view).load(path).into(image)
        }
    }
}