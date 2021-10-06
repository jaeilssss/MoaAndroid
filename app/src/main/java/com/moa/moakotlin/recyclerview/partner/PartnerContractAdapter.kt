package com.moa.moakotlin.recyclerview.partner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Contract
import android.view.View
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.ItemPartnerContractBinding
import java.text.SimpleDateFormat

class PartnerContractAdapter  : ListAdapter<Contract,PartnerContractAdapter.ContractViewHolder>(diffUtil){

    private var mListener : OnItemClickListener?=null

    fun setOnItemClickListener(mListener: OnItemClickListener){
        this.mListener = mListener

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerContractAdapter.ContractViewHolder {
        return ContractViewHolder(ItemPartnerContractBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PartnerContractAdapter.ContractViewHolder, position: Int) {
        holder.binding(currentList[position])
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ContractViewHolder(var binding : ItemPartnerContractBinding): RecyclerView.ViewHolder(binding.root){


        fun binding(contract: Contract){

            binding.itemPartnerContractTitle.text = contract.title
            binding.itemPartnerContractStatus.text = contract.status
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
            binding.itemPartnerContractDate.text = "${contract.companyName} ${dateFormat.format(contract.contractStartDate.toDate())}~${dateFormat.format(contract.contractEndDate.toDate())}"
            if(contract.images.isNotEmpty()){
                Glide.with(binding.root).load(contract.images[0]).into(binding.itemPartnerContractImg)
            }
            binding.itemPartnerContractLayout.setOnClickListener(ButtonClick())
        }
        inner class ButtonClick : View.OnClickListener{
            override fun onClick(view: View?) {
                when(view!!.id){
                    R.id.itemPartnerContractLayout ->{
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
        val diffUtil = object : DiffUtil.ItemCallback<Contract>(){
            override fun areItemsTheSame(oldItem: Contract, newItem: Contract): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Contract, newItem: Contract): Boolean {
                return oldItem.contractStartDate == newItem.contractStartDate
            }

        }
    }
}