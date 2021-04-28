package com.moa.moakotlin.recyclerview.concierge

import android.content.Context
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Needer
import kotlinx.android.synthetic.main.concierge_item.view.*

class NeederMainAdapter(var list : ArrayList<Needer>,var context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       var view = View.inflate(parent.context , R.layout.concierge_item,null)
        return NeederViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var data = list.get(position)
        println("-------여기 실행되나요???")
        if(data.images!=null){
            Glide.with(context).load(data.images!!.get(0)).into((holder as NeederViewHolder).image)
            holder.title.text = data.title
        }
    }

    inner class NeederViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var image : ImageView
        var title : TextView

        init {
            image = view.findViewById(R.id.image)
            title = view.findViewById(R.id.title)
        }
    }
 }