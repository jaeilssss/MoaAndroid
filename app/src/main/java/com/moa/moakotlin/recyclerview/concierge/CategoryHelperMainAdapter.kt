package com.moa.moakotlin.recyclerview.concierge

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
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
import com.moa.moakotlin.databinding.ItemCategoryMainBinding
import kotlinx.android.synthetic.main.item_category_main.view.*
import java.text.SimpleDateFormat

class CategoryHelperMainAdapter(): ListAdapter<Helper, CategoryHelperMainAdapter.CategoryViewHolder>(diffUtil) {


    private var mListener : OnItemClickListener ?=null

    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHelperMainAdapter.CategoryViewHolder {
        return CategoryViewHolder(ItemCategoryMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CategoryHelperMainAdapter.CategoryViewHolder, position: Int) {
            holder.binding(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CategoryViewHolder(var binding : ItemCategoryMainBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(helper : Helper){
            if(helper.images?.size!=0){
                Glide.with(binding.root).load(helper.images?.get(0)).apply(
                    RequestOptions.bitmapTransform(
                        RoundedCorners(16)
                    ))
                    .into(binding.itemCategoryImage)
            }
            binding.itemCategoryAptName.text = helper.aptName
            binding.itemCategoryContent.text = helper.content
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm")
            binding.itemCategoryHopeDate.text = dateFormat.format(helper.timeStamp.toDate())
            if(helper.isNego.not()){
                binding.CategoryMainIsNego.isVisible = false
            }
            binding.itemCategoryPrice.text = "${helper.hopeWage}원"
            binding.CategoryHelperLayout.setOnClickListener(ButtonClick())

            binding.ItemCategoryStatus.isVisible =false

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
        val diffUtil = object : DiffUtil.ItemCallback<Helper>(){
            override fun areItemsTheSame(oldItem: Helper, newItem: Helper): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Helper, newItem: Helper): Boolean {
                return oldItem.documentID == newItem.documentID
            }

        }
    }
}