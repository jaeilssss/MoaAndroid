package com.moa.moakotlin.recyclerview.imagepickrcv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.databinding.ImagePickerItemBinding
import com.moa.moakotlin.databinding.ImagePickerViewFragmentBinding
import com.moa.moakotlin.databinding.ItemGoToBinding

class ImagePickerViewAdapter(var context: Context,var list : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding : ImagePickerItemBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if(viewType==0){
//            context = parent.context
//            return goToCameraViewHolder(ItemGoToBinding.inflate(LayoutInflater.from(parent.context),parent,false))
//        }else{
            var view = View.inflate(parent.context, R.layout.image_picker_item,null)
            return ImageViewHolder(view)
//        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if(position!=0){
            holder as ImageViewHolder
            Glide.with(context).load(list.get(position)).into(holder.image)
//        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
//    override fun getItemViewType(position: Int): Int {
//        if(position==0){
//            return 0
//        }else{
//            return 1
//        }
//    }
    inner class ImageViewHolder(var view : View) : RecyclerView.ViewHolder(view){

        var image : ImageView = view.findViewById(R.id.image_picker_item)

        inner class ButtonClick  :View.OnClickListener{
            override fun onClick(view: View?) {

            }

        }
    }

    inner class goToCameraViewHolder(var binding : ItemGoToBinding) : RecyclerView.ViewHolder(binding.root){

    }




}