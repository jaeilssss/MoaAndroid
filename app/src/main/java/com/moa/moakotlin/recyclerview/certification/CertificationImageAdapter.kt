package com.moa.moakotlin.recyclerview.certification


import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.ItemAptCertificationBinding
import com.moa.moakotlin.recyclerview.kid.KidAdapter

class CertificationImageAdapter() : ListAdapter<String, CertificationImageAdapter.CertificationViewModel>(diffUtil){

   var mListener : OnItemClickListener ?= null

   fun setOnItemCLickListener(listener: OnItemClickListener?){
       mListener = listener
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificationViewModel {
        return CertificationViewModel(ItemAptCertificationBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CertificationViewModel, position: Int) {
        holder.binding(currentList[position])
    }



    inner class CertificationViewModel(var binding : ItemAptCertificationBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.itemAptCertificationClose.setOnClickListener(ButtonClick())
        }
        fun binding(path : String){
            Glide.with(binding.root).load(path).into(binding.itemAptCertificationImage)
        }
        inner class ButtonClick : View.OnClickListener{
            override fun onClick(view: View?) {
                when(view?.id){
                    R.id.itemAptCertificationClose ->{
                        if(mListener !=null){
                            if(adapterPosition!=RecyclerView.NO_POSITION){
                                mListener?.onItemClick(view,adapterPosition)
                            }
                        }
                    }
                }
            }

        }
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }


}