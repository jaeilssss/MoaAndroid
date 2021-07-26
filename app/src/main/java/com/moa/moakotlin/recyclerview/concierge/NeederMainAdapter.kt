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
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.databinding.ItemConciergeBinding
import com.moa.moakotlin.databinding.ItemEmptyConciergeBinding
import java.text.SimpleDateFormat

class NeederMainAdapter() : ListAdapter<Needer, RecyclerView.ViewHolder>(diffUtil) {

    private var mListener : OnItemClickListener?=null


    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener

    }

    override fun getItemViewType(position: Int): Int {

        if(currentList[position].documentID.equals("-1")){
            return -1
        }else{
            return position
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==-1){
            return EmptyConciergeViewHolder(ItemEmptyConciergeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else{
            return ConciergeViewHolder(ItemConciergeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ConciergeViewHolder) holder.binding(currentList[position])
        else (holder as NeederMainAdapter.EmptyConciergeViewHolder).binding()
    }

    inner class EmptyConciergeViewHolder(var binding: ItemEmptyConciergeBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(){
            binding.itemConciergeEmptyImg.setImageResource(R.drawable.img_empty_data)
        }
    }

    inner class ConciergeViewHolder(var binding: ItemConciergeBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(helper: Needer){
            if(helper.images?.size!!>0){
                Glide.with(binding.root).load(helper.images?.get(0)).apply(
                    RequestOptions.bitmapTransform(
                        RoundedCorners(10)
                    )).into(binding.itemConciergeImage)
            }
            if(helper.isNego.not()){
                binding.itemConciergeNego.isVisible = false
            }
            binding.itemConciergeHopeWage.text = "${helper.hopeWage}원"
            binding.itemConciergeContent.text = helper.content
            binding.itemConciergeAptName.text = helper.aptName
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm")

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