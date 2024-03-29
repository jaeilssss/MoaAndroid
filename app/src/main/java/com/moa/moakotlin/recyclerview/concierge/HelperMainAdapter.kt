package com.moa.moakotlin.recyclerview.concierge

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.databinding.ItemConciergeBinding
import com.moa.moakotlin.databinding.ItemEmptyConciergeBinding
import java.text.SimpleDateFormat

class HelperMainAdapter() :ListAdapter<Helper, RecyclerView.ViewHolder>(diffUtil) {

    private var mListener : OnItemClickListener ?=null

    var defaultUrl = "https://firebasestorage.googleapis.com/v0/b/moakr-8c0ab.appspot.com/o/CONCIERGE_DEFAULT_200x200.png?alt=media&token=8ba33ee5-1b36-4d39-b400-6af648439187"

    fun setOnItemClickListener(mListener : OnItemClickListener){
       this.mListener = mListener

    }

    override fun getItemViewType(position: Int): Int {
        if(currentList[position].documentID=="-1"){
            return -1
        }else{
            return position
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==-1){
            return EmptyConciergeViewHolder(ItemEmptyConciergeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }else{
            return ConciergeViewHolder(ItemConciergeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ConciergeViewHolder)  holder.binding(currentList[position])
        else (holder as EmptyConciergeViewHolder).binding()
    }

    inner class EmptyConciergeViewHolder(var binding : ItemEmptyConciergeBinding) : RecyclerView.ViewHolder(binding.root){
            fun binding(){
                binding.itemConciergeEmptyImg.setImageResource(R.drawable.img_empty_data)
            }


    }
    inner class ConciergeViewHolder(var binding: ItemConciergeBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(helper: Helper){
            binding.itemConciergeHireStatus.isVisible  = false
            if(helper.images?.size!!>0){
                Glide.with(binding.root).load(helper.images?.get(0)).apply(RequestOptions.bitmapTransform(RoundedCorners(15))).into(binding.itemConciergeImage)
            }else{
                Glide.with(binding.root).load(defaultUrl)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(15))).into(binding.itemConciergeImage)
            }
            if(helper.isNego.not()){
                binding.itemConciergeNego.isVisible = false
            }
            binding.itemConciergeHopeWage.text = "${helper.hopeWage}원"
            binding.itemConciergeTitle.text = helper.title
            binding.itemConciergeAptName.text = helper.aptName
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일")

            binding.itemConciergeDate.text =  dateFormat.format(helper.timeStamp.toDate())

            binding.itemConciergeLayout.setOnClickListener(ButtonClick())
        }

        inner class ButtonClick : View.OnClickListener{
            override fun onClick(v: View?) {
                when(v?.id){
                    R.id.itemConciergeLayout ->{
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