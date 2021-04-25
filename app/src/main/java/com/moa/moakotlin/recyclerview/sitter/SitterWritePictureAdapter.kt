package com.moa.moakotlin.recyclerview.sitter

import android.content.Context
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.recyclerview.kid.KidWritePictureAdapter

class SitterWritePictureAdapter(navController: NavController,var list : ArrayList<String>,var context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = View.inflate(parent.context, R.layout.picture_item,null)
        return SitterWriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(context).load(list.get(position)).into((holder as SitterWriteViewHolder).imageview)
    }

    class SitterWriteViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var imageview : ImageView

        init {
            imageview = view.findViewById(R.id.imageviewItem)
        }

    }
}