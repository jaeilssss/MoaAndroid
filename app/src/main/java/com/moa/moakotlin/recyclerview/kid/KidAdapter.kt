package com.moa.moakotlin.recyclerview.kid

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Kid

class KidAdapter(var list : ArrayList<Kid>, var context : Context, var navController: NavController) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    var mListener : OnItemClickListener ? = null
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view : View

            view = View.inflate(parent.context , R.layout.needer_item , null)
            return kidViewHolder(view,this)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

             if(list.get(position).images ==null){
                holder as kidViewHolder
                holder.imageView.setImageResource(R.drawable.moa_kid_default)
                holder.title.text = list.get(position).title
                holder.wage.text = list.get(position).wage
                holder.aptName.text = list.get(position).aptName
                var date = list.get(position).hopeDate.split("년")
                 holder.hopeDate.text = date.get(1)+" 시작"
            }
           else if(list.get(position).images?.size!=0){

                Glide.with(context).load(list.get(position).images?.get(0)).into((holder as kidViewHolder).imageView)
                holder.title.text = list.get(position).title
                holder.wage.text = list.get(position).wage
                holder.aptName.text = list.get(position).aptName
                var date = list.get(position).hopeDate.split("년")
                holder.hopeDate.text = date.get(1)+" 시작"
            }



    }



    fun goToKidReadPage(position: Int){
        var bundle = Bundle()
            bundle.putParcelable("kid",list.get(position))
        navController.navigate(R.id.action_kidMainFragment_to_kidReadFragment,bundle)
    }
    class kidViewHolder(view : View,adapter: KidAdapter) : RecyclerView.ViewHolder(view) {
       var imageView :ImageView
        var layout : ConstraintLayout
        var title :TextView
        var wage : TextView
        var aptName :TextView
        var hopeDate : TextView
        init {
            hopeDate = view.findViewById(R.id.kid_item_hope_date)
            aptName = view.findViewById(R.id.kid_item_apt_name)
            wage = view.findViewById(R.id.kid_item_wage)
            title = view.findViewById(R.id.kid_item_title)
            imageView = view.findViewById(R.id.kid_item_image)
            layout = view.findViewById(R.id.kid_item_layout)
            layout.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    if (view != null) {
                        when(view.id){
                            R.id.kid_item_layout ->
                                adapter.goToKidReadPage(adapterPosition)
                        }
                    }
                }

            })
        }



    }

}



