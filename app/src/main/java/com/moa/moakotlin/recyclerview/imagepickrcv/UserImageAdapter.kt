package com.moa.moakotlin.recyclerview.imagepickrcv

import android.content.Context
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.base.OnItemClickListener

class UserImageAdapter(var list : ArrayList<String>,var context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var checkBox = -1

    private var mListener : OnItemClickListener?=null

    public fun setOnItemClickListener(mListener : OnItemClickListener){
        this.mListener = mListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = View.inflate(parent.context,R.layout.image_picker_item,null)
        return UserImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun resetting(){
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var picture = list.get(position)
        Glide.with(context).load(picture).into((holder as UserImageViewHolder).image)

        if(checkBox==position){
            (holder as UserImageViewHolder).check.setImageResource(R.drawable.image_check_image)
        }else{
            (holder as UserImageViewHolder).check.setImageResource(R.drawable.image_not_check_image)
        }
    }

    inner class UserImageViewHolder( view : View) : RecyclerView.ViewHolder(view){
        lateinit var image : ImageView
        lateinit var check : ImageView
        lateinit var layout : RelativeLayout
        init{
            image = view.findViewById(R.id.image_picker_item)
            check = view.findViewById(R.id.image_check_box_button)
            layout  = view.findViewById(R.id.image_picker_layout)
            layout.setOnClickListener(ButtonClick())
//            check.setOnClickListener(ButtonClick())
        }
        inner class ButtonClick : View.OnClickListener{
            override fun onClick(v: View?) {
                when(v?.id){
                    R.id.image_picker_layout ->{
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
}