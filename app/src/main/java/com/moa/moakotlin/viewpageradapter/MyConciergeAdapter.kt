package com.moa.moakotlin.viewpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView

import com.moa.moakotlin.ui.mypage.MyHelperFragment
import com.moa.moakotlin.ui.mypage.MyNeederFragment

class MyConciergeAdapter(var fm: FragmentManager) : FragmentStatePagerAdapter(fm!!){
   var list = ArrayList<Fragment>()
    init {
        println("init")
        list.add(MyHelperFragment())
        list.add(MyNeederFragment())
    }
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        println("getItem..")
       return list.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if(position==0){
            return "재능공유"
        }else{
            return "도움요청"
        }
    }
}