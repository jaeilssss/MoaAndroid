package com.moa.moakotlin.recyclerview.concierge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.ItemCategoryMainBinding
import java.text.SimpleDateFormat

class CategoryNeederMainAdapter() : ListAdapter<Needer, CategoryNeederMainAdapter.CategoryViewHolder>(diffUtil) {

    private lateinit var mListener : OnItemClickListener
    var defaultUrl = "https://firebasestorage.googleapis.com/v0/b/moakr-8c0ab.appspot.com/o/CONCIERGE_DEFAULT.png?alt=media&token=8623aaa7-4f88-44fb-a05e-64d2a02cb683"
    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryNeederMainAdapter.CategoryViewHolder {
        return CategoryViewHolder(ItemCategoryMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
            return position
    }

    inner class CategoryViewHolder(var binding : ItemCategoryMainBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(needer : Needer){
            if(needer.images?.size!=0){
                Glide.with(binding.root).load(needer.images?.get(0)).apply(
                        RequestOptions.bitmapTransform(
                                RoundedCorners(16)
                        ))
                        .into(binding.itemCategoryImage)


            }else{
                Glide.with(binding.root).load(defaultUrl).apply(
                        RequestOptions.bitmapTransform(
                                RoundedCorners(16)
                        ))
                        .into(binding.itemCategoryImage)
            }
            binding.itemCategoryAptName.text = needer.aptName
            binding.itemCategoryContent.text = needer.content
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm")
            binding.itemCategoryHopeDate.text = dateFormat.format(needer.timeStamp.toDate())
            binding.itemCategoryPrice.text = "${needer.hopeWage}원"
            if(needer.isNego.not()){
                binding.CategoryMainIsNego.isVisible = false
            }
            binding.CategoryHelperLayout.setOnClickListener(ButtonClick())

            binding.ItemCategoryStatus.text = needer.hireStatus

        }
        inner class ButtonClick : View.OnClickListener{
            override fun onClick(view: View?) {
                when(view?.id){
                    R.id.CategoryHelperLayout->{
                        if(mListener!=null){
                            if(adapterPosition != RecyclerView.NO_POSITION){
                                mListener!!.onItemClick(view,adapterPosition)
                            }
                        }
                    }
                }

            }

        }
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Needer>(){
            override fun areItemsTheSame(oldItem: Needer, newItem: Needer): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Needer, newItem: Needer): Boolean {
                return oldItem.documentID == newItem.documentID
            }

        }
    }


}