package com.moa.moakotlin.recyclerview.imagepickrcv

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener
import com.moa.moakotlin.data.Picture

class ConciergeImagePickerAdapter(var context: Context, var list : ArrayList<String>, var selectedPicture : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var checkBox = -1
    var checkBoxList = ArrayList<Int>()

    var i=1

    private var mListener : OnItemClickListener ?=null

    fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       var view = View.inflate(parent.context ,R.layout.image_picker_item,null)
        return KidImagePickerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(context).load(list.get(position)).into((holder as KidImagePickerViewHolder).image)

        if(selectedPicture.contains(list.get(position))){
                (holder as KidImagePickerViewHolder).check.setBackgroundResource(R.drawable.image_check_image)
            holder.check.text = (selectedPicture.indexOf(list[position])+1).toString()
        }else{
                (holder as KidImagePickerViewHolder).check.setBackgroundResource(R.drawable.image_not_check_image)
            holder.check.text = ""
        }
//        if(checkBoxList.contains(position)){
//            (holder as KidImagePickerViewHolder).check.setBackgroundResource(R.drawable.image_check_image)
//            holder.check.text = (selectedPicture.indexOf(list[position])+1).toString()
//        }else{
//            (holder as KidImagePickerViewHolder).check.setBackgroundResource(R.drawable.image_not_check_image)
//            holder.check.text = ""
//        }
    }

    fun resetting(){
        notifyDataSetChanged()
    }
   inner class KidImagePickerViewHolder(view: View) : RecyclerView.ViewHolder(view){
        lateinit var image : ImageView
        lateinit var check : TextView
        lateinit var layout : RelativeLayout

        init{
            image = view.findViewById(R.id.image_picker_item)
            check = view.findViewById(R.id.image_check_box_button)
            layout  = view.findViewById(R.id.image_picker_layout)
            layout.setOnClickListener(ButtonClick())
        }
        inner class ButtonClick : View.OnClickListener{
            override fun onClick(v: View?) {
                when(v?.id){
                    R.id.image_picker_layout ->{
                        if(mListener!=null){
                            if(adapterPosition !=RecyclerView.NO_POSITION){
                                mListener!!.onItemClick(v,adapterPosition)
                            }
                        }

                    }
                }
            }
        }
    }
}