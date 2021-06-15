package com.moa.moakotlin.recyclerview.algoria

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Apt
import com.moa.moakotlin.databinding.ItemAptBinding
import kotlinx.android.synthetic.main.item_apt.view.*

class SearchAptAdapter() : ListAdapter<Apt,SearchAptAdapter.SearchAptViewModel>(diffUtil){

    private var mListener : OnItemClickListener?=null
    public fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAptViewModel {
        return SearchAptViewModel(ItemAptBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SearchAptViewModel, position: Int) {
        holder.binding(currentList[position])
    }
    inner class SearchAptViewModel(private val binding : ItemAptBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.itemMyAptCheck.setOnClickListener(ButtonClick())
        }
        fun binding(apt : Apt){
            binding.itemAptName.text = apt.aptName
            binding.aptJuso.text = apt.address
        }
        inner class ButtonClick : View.OnClickListener{
            override fun onClick(v: View?) {
                when(v?.id){
                    R.id.itemMyAptCheck ->{
                        if(mListener!=null){
                            if(adapterPosition!=RecyclerView.NO_POSITION){
                                mListener?.onItemClick(v,adapterPosition)
                            }
                        }
                    }
                }
            }

        }
    }
    companion object{
        val diffUtil = object  : DiffUtil.ItemCallback<Apt>(){
            override fun areItemsTheSame(oldItem: Apt, newItem: Apt): Boolean {
                return oldItem ==newItem
            }

            override fun areContentsTheSame(oldItem: Apt, newItem: Apt): Boolean {
                return oldItem.aptCode == newItem.aptCode
            }

        }
    }


}