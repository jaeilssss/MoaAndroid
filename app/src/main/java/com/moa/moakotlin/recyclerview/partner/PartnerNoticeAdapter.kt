package com.moa.moakotlin.recyclerview.partner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Complaint
import com.moa.moakotlin.data.PartnerNotice
import com.moa.moakotlin.databinding.ItemPartnerNoticeBinding

class PartnerNoticeAdapter : ListAdapter<PartnerNotice,PartnerNoticeAdapter.PartnerNoticeViewHolder>(diffUtil) {

    private var mListener : OnItemClickListener?=null

    var defaultUrl = "https://firebasestorage.googleapis.com/v0/b/moakr-8c0ab.appspot.com/o/CONCIERGE_DEFAULT_200x200.png?alt=media&token=8ba33ee5-1b36-4d39-b400-6af648439187"

    fun setOnItemClickListener(mListener: OnItemClickListener){
        this.mListener = mListener

    }

    override fun getItemCount(): Int {
//        return super.getItemCount()
        return 20
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerNoticeViewHolder {
        return PartnerNoticeViewHolder(ItemPartnerNoticeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PartnerNoticeViewHolder, position: Int) {
        holder.binding(PartnerNotice())
    }

    inner class PartnerNoticeViewHolder(var binding : ItemPartnerNoticeBinding) : RecyclerView.ViewHolder(binding.root){

        fun binding(partnerNotice : PartnerNotice){
            binding.itemPartnerNoticeLayout.setOnClickListener(ButtonClick())
        }
        inner class ButtonClick : View.OnClickListener{
            override fun onClick(v: View?) {
                when(v?.id){
                    R.id.itemPartnerNoticeLayout ->{
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
        val diffUtil = object : DiffUtil.ItemCallback<PartnerNotice>(){
            override fun areItemsTheSame(oldItem: PartnerNotice, newItem: PartnerNotice): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: PartnerNotice, newItem: PartnerNotice): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

        }
    }

}