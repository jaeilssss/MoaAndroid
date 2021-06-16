package com.moa.moakotlin.recyclerview.certification


import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.databinding.ItemAptCertificationBinding

class CertificationImageAdapter() : ListAdapter<String, CertificationImageAdapter.CertificationViewModel>(diffUtil){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificationViewModel {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: CertificationViewModel, position: Int) {
        TODO("Not yet implemented")
    }

    inner class CertificationViewModel(binding : ItemAptCertificationBinding) : RecyclerView.ViewHolder(binding.root){

    }
    companion object{
        val diffUtil = object  : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }


}