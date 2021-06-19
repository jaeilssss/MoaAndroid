package com.moa.moakotlin.recyclerview.certification


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.databinding.ItemAptCertificationBinding

class CertificationImageAdapter() : ListAdapter<String, CertificationImageAdapter.CertificationViewModel>(diffUtil){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificationViewModel {
        return CertificationViewModel(ItemAptCertificationBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CertificationViewModel, position: Int) {
        holder.binding(currentList[position])
    }



    inner class CertificationViewModel(var binding : ItemAptCertificationBinding) : RecyclerView.ViewHolder(binding.root){

        fun binding(path : String){
            Glide.with(binding.root).load(path).into(binding.itemAptCertificationImage)
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