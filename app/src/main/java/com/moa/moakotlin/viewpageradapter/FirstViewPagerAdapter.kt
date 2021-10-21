package com.moa.moakotlin.viewpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.moa.moakotlin.ui.firstview.FirstViewPagerFragment
import com.moa.moakotlin.ui.firstview.SecondViewPagerFragment
import com.moa.moakotlin.ui.firstview.thirdViewPagerFragment
import com.moa.moakotlin.ui.mypage.MyHelperFragment
import com.moa.moakotlin.ui.mypage.MyNeederFragment

class FirstViewPagerAdapter(fm :FragmentManager)  : FragmentStatePagerAdapter(fm!!){
    var list = ArrayList<Fragment>()
    init {
        list.add(FirstViewPagerFragment())
        list.add(SecondViewPagerFragment())
        list.add(thirdViewPagerFragment())
    }
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {

        return list.get(position)
    }


}