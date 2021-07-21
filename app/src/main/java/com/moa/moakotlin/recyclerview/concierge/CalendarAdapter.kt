package com.moa.moakotlin.recyclerview.concierge


import android.R
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.databinding.ItemCalendarBinding


import java.time.LocalDateTime


class CalendarAdapter(var list : ArrayList<String>,var currentYear : String , var currentMonth : String , var currentDay : String, var selectDate : LocalDateTime) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var mListener : OnItemClickListener ?= null

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener
    }
    var selectPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.getContext())

        var binding  = ItemCalendarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val view: View = binding.root
        val layoutParams: ViewGroup.LayoutParams = view.getLayoutParams()
        layoutParams.height = (parent.getHeight() * 0.126666666).toInt()
        return CalendarViewHolder(binding)

    }


    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.binding(list[position])
        if(position==selectPosition){
            holder.setChangeBackGround()
        }else{
            holder.setChangeBackGroundDefault()
        }

    }
    inner class CalendarViewHolder(var binding: ItemCalendarBinding) : RecyclerView.ViewHolder(binding.root){
        fun binding(day : String){


            if(day.isEmpty()){
                binding.cellDayText.text = day
            } else if(currentYear.equals("${selectDate.year.toString()}년")&&
                    currentMonth.equals("${selectDate.monthValue.toString()}월") &&
                    currentDay.toInt()>day.toInt()){
                    binding.cellDayText.text = day
                    binding.cellDayText.setTextColor(Color.GRAY)


            }else{
                binding.cellDayText.text = day
                binding.cellDayText.setOnClickListener(ButtonClick())
            }


        }
        fun setChangeBackGround(){
            binding.cellDayText.setBackgroundResource(com.moa.moakotlin.R.drawable.shpae_oval_calendar_active)
        }
        fun setChangeBackGroundDefault(){
        binding.cellDayText.setBackgroundResource(com.moa.moakotlin.R.drawable.shape_oval_calendar_default)
        }
        inner class ButtonClick : View.OnClickListener{
            override fun onClick(v: View?) {
                if(mListener!=null){
                    if(adapterPosition != RecyclerView.NO_POSITION){
                        if (v != null) {
                            mListener!!.onItemClick(v,adapterPosition)
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

    override fun getItemCount(): Int {
        return list.size
    }


}