package com.moa.moakotlin.recyclerview.imagepickrcv

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Picture

class KidImagePickerAdapter(var navController: NavController,var context: Context,var list : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var checkBox = -1
    var checkBoxList = ArrayList<Int>()
    var selectedPicture = Picture.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       var view = View.inflate(parent.context ,R.layout.image_picker_item,null)
        return KidImagePickerViewHolder(view,this)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(context).load(list.get(position)).into((holder as KidImagePickerViewHolder).image)
        if(checkBoxList.contains(position)){
            (holder as KidImagePickerViewHolder).check.setImageResource(R.drawable.image_check_image)
        }else{
            (holder as KidImagePickerViewHolder).check.setImageResource(R.drawable.image_not_check_image)
        }
    }

    fun resetting(){
        notifyDataSetChanged()
    }
    class KidImagePickerViewHolder(view: View,var adapter: KidImagePickerAdapter) : RecyclerView.ViewHolder(view){
        lateinit var image : ImageView
        lateinit var check : ImageView
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
                        if(adapter.checkBoxList.contains(adapterPosition)){
                            println("삭제포지션 : ${adapterPosition}")
                            var index = adapter.checkBoxList.indexOf(adapterPosition)
                            println("index : ${index}")
                            adapter.checkBoxList.removeAt(index)
                            adapter.selectedPicture.removeAt(index)
                            println("리스트  !! ${adapter.checkBoxList}")

                                adapter.resetting()
                        }else{
                            adapter.selectedPicture.add(adapter.list.get(adapterPosition))
                            adapter.checkBoxList.add(adapterPosition)
                            println("선택리스트 : ${adapter.checkBoxList}")
                            adapter.resetting()
                        }
                    }
                }
            }
        }
    }
}