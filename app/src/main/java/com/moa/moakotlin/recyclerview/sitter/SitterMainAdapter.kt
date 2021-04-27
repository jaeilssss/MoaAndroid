package com.moa.moakotlin.recyclerview.sitter

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Sitter

class SitterMainAdapter(var list : ArrayList<Sitter>,var context: Context,var navController: NavController) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListener : OnItemClickListener?=null

    public fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

    var view = View.inflate(parent.context,R.layout.helper_item,null)
        return SitterMainViewHolder(view,this)
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(list.get(position).images?.size==0){
            holder as SitterMainViewHolder
            holder.title.text = list.get(position).title
            holder.wage.text = list.get(position).wage
            holder.aptName.text = list.get(position).aptName
        }else{
            Glide.with(context).load(list.get(position).images?.get(0)).into((holder as SitterMainViewHolder).imageView)
            holder.title.text = list.get(position).title
            holder.wage.text = list.get(position).wage
            holder.aptName.text = list.get(position).aptName
        }

    }

    fun goToSitterRead(position: Int){
        var bundle = Bundle()

        bundle.putParcelable("sitter",list.get(position))
        navController.navigate(R.id.action_sitterMainFragment_to_sitterReadFragment,bundle)
    }
   inner class SitterMainViewHolder(view :View,adapter: SitterMainAdapter) : RecyclerView.ViewHolder(view){
        var imageView : ImageView
        var layout : ConstraintLayout
        var title : TextView
        var wage : TextView
        var aptName : TextView
        init {
            aptName = view.findViewById(R.id.sitter_item_apt_name)
            wage = view.findViewById(R.id.sitter_item_wage)
            title = view.findViewById(R.id.sitter_item_title)
            imageView = view.findViewById(R.id.sitter_item_image)
            layout = view.findViewById(R.id.sitter_item_layout)
            layout.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    if (view != null) {

                                if(adapterPosition!=RecyclerView.NO_POSITION){
//                                    adapter.goToSitterRead(adapterPosition)
                                    mListener?.onItemClick(view,adapterPosition)
                                    if(mListener==null){
                                    }
                        }
                    }
                }

            })
        }



    }

}