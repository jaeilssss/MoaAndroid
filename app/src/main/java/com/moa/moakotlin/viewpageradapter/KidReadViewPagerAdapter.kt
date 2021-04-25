package com.moa.moakotlin.viewpageradapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.moa.moakotlin.R

class KidReadViewPagerAdapter(var list: ArrayList<String>?, var context: Context) : PagerAdapter(){
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view :View ?= null;
        var inflater : LayoutInflater = LayoutInflater.from(context)

        view = inflater.inflate(R.layout.kid_read_image_view_pager,container,false)

        var imageView  = view.findViewById<ImageView>(R.id.kid_view_pager_image)

        if(list==null){
            imageView.setImageResource(R.drawable.moa_kid_default)
        }
        else if(list!!.size!=0){
            Glide.with(context).load(list!!.get(position)).into(imageView)
        }



        container.addView(view);
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        if(list ==null){
            return 1
        }else{
           return list!!.size
        }
    }


}