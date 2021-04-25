package com.moa.moakotlin.recyclerview.imagepickrcv

import android.content.Context
import android.media.Image
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.data.Picture
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.moa.moakotlin.R

class ImagePickerAdapter(var navController: NavController,var context: Context , var list : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var checkBox = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = View.inflate(context, R.layout.image_picker_item , null)
        var viewHolder = ImagePickerViewHolder(view,this)
        return viewHolder
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       var picture = list.get(position)
        Glide.with(context).load(picture).into((holder as ImagePickerViewHolder).image)

        if(checkBox==position){
            (holder as ImagePickerViewHolder).check.setImageResource(R.drawable.image_check_image)
        }else{
            (holder as ImagePickerViewHolder).check.setImageResource(R.drawable.image_not_check_image)
        }

    }
        fun resetting(position: Int){
            notifyDataSetChanged()
        }
    class ImagePickerViewHolder(view : View,var adapter: ImagePickerAdapter) : RecyclerView.ViewHolder(view){
        lateinit var image : ImageView
        lateinit var check : ImageView
        lateinit var layout : RelativeLayout

        init{
            image = view.findViewById(R.id.image_picker_item)
            check = view.findViewById(R.id.image_check_box_button)
            layout  = view.findViewById(R.id.image_picker_layout)
            layout.setOnClickListener(ButtonClick())
//            check.setOnClickListener(ButtonClick())
        }
    inner class ButtonClick : View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id){
                R.id.image_picker_layout ->{
                    if(adapter.checkBox==-1){
                        adapter.checkBox = adapterPosition
                        adapter.resetting(adapterPosition)
                    }else{
                        var temp = adapter.checkBox
                        adapter.checkBox = adapterPosition
                        adapter.resetting(adapterPosition)
                    }
                }
            }
        }
    }



    }
}