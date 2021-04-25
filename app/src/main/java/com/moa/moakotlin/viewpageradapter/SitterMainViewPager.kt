package com.moa.moakotlin.viewpageradapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Sitter

class SitterMainViewPager(var list: ArrayList<Sitter>, var context: Context,navController: NavController) : PagerAdapter() {


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun getCount(): Int {
       return 2
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var inflater : LayoutInflater = LayoutInflater.from(context)

                // 일단 kid_read 뷰페이저 그대로 쓰고 나중에 사이즈가 다를 경우 하나 더 만들 것!!
        var view = inflater.inflate(R.layout.kid_read_image_view_pager,container,false)

        var imageView  = view.findViewById<ImageView>(R.id.kid_view_pager_image)

        imageView.setBackgroundColor(Color.parseColor("#f033aa"))

//            Glide.with(context).load(list!!.get(position)).into(imageView)

        return view
    }
}