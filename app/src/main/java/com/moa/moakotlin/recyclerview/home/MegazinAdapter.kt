package com.moa.moakotlin.recyclerview.home

import android.app.ActionBar
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.data.Megazin
import com.moa.moakotlin.databinding.ItemMoaMegazinBinding

class MegazinAdapter() : ListAdapter<Megazin, MegazinAdapter.MegazinViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MegazinViewHolder {
        return MegazinViewHolder(ItemMoaMegazinBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MegazinViewHolder, position: Int) {
        holder.binding(currentList[position],position)
    }

    inner class MegazinViewHolder(var binding : ItemMoaMegazinBinding) : RecyclerView.ViewHolder(binding.root){

        // item 따로 만드는 걸로 ,..
        fun binding(megazin:Megazin, position: Int){

            if(position%2==0){
                // 오른 쪽에 마진 을 줄것 !


            var wlayoutParams = ConstraintLayout.LayoutParams(100,100)

                binding.itemMegazinImage.layoutParams = wlayoutParams

                binding.itemMegazinImage.setBackgroundColor(Color.parseColor("#bdbdbd"))
            }else{
                var wlayoutParams = binding.itemMegazinImage.layoutParams
                binding.itemMegazinImage.layoutParams = wlayoutParams

            }
        }

    }
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Megazin>(){
            override fun areItemsTheSame(oldItem: Megazin, newItem: Megazin): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Megazin, newItem: Megazin): Boolean {

                // 나중에 수정할 것 !


                return oldItem .equals(newItem)
//                return oldItem.id == newItem.id
            }

        }
    }
}