package com.moa.moakotlin.viewpageradapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener

class ConciergeReadViewpagerAdapter(var context : Context , var list : ArrayList<String>) :  RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var mListener : OnItemClickListener?=null


    fun setOnItemClickListener(mListener: OnItemClickListener){
        this.mListener = mListener

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_concierge_read_viewpager,parent,false)
        return ConciergeReadViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as ConciergeReadViewHolder

        holder.binding(list[position])

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ConciergeReadViewHolder(var view : View) : RecyclerView.ViewHolder(view){
        var image : ImageView = view.findViewById(R.id.itemConciergeReadImage)

        fun binding(url : String){
            Glide.with(context).load(url)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                .into(image)
            image.setOnClickListener(ButtonClick())
        }

        inner class ButtonClick : View.OnClickListener{
            override fun onClick(v: View?) {
                if(mListener!=null){
                    when(v?.id){
                        R.id.itemConciergeReadImage ->{
                            if(mListener!=null){
                                if(adapterPosition != RecyclerView.NO_POSITION){
                                    mListener!!.onItemClick(v,adapterPosition)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}