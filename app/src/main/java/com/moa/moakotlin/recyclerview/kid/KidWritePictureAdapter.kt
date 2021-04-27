package com.moa.moakotlin.recyclerview.kid

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.moa.moakotlin.R

class KidWritePictureAdapter(var list: ArrayList<String>, var context: Context, var resource: Resources) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = View.inflate(parent.context,R.layout.picture_item,null)
            return KidWritePictureViewHolder(view,this)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun deletePicture(position: Int){
            list.removeAt(position)
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(context).load(list.get(position)).into((holder as KidWritePictureViewHolder).imageview)
    }
    fun updateList(newList : ArrayList<String>){
        this.list = newList
    }
     class KidWritePictureViewHolder(view:View,adapter: KidWritePictureAdapter) :RecyclerView.ViewHolder(view){
        lateinit var imageview : ImageView
         init{
             imageview = view.findViewById(R.id.imageviewItem)
             imageview.setOnClickListener {
                     val builder: AlertDialog.Builder = AlertDialog.Builder(adapter.context)

                     builder.setPositiveButton("네",object :DialogInterface.OnClickListener{
                         override fun onClick(p0: DialogInterface?, p1: Int) {
                            adapter.deletePicture(adapterPosition)
                         }

                     })
                     builder.setNegativeButton("아니요",object :DialogInterface.OnClickListener{
                         override fun onClick(p0: DialogInterface?, p1: Int) {

                         }

                     })

                 val alertDialog: AlertDialog = builder.create()
                 alertDialog.setTitle("사진을 지우시겠습니까??")
                     alertDialog.show()


             }
         }
     }
}