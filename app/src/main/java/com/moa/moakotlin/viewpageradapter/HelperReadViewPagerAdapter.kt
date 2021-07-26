package com.moa.moakotlin.viewpageradapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.ui.concierge.helper.ConciergeReadIntroduceFragment
import com.moa.moakotlin.ui.concierge.helper.HelperReadReviewFragment

import com.moa.moakotlin.ui.mypage.MyHelperFragment
import com.moa.moakotlin.ui.mypage.MyNeederFragment

class HelperReadViewPagerAdapter(var fm: FragmentManager,var helper : Helper ) : FragmentStatePagerAdapter(fm!!){
    var list = ArrayList<Fragment>()
    init {
        println("init")
        list.add(ConciergeReadIntroduceFragment(helper,null))
        list.add(HelperReadReviewFragment(helper))
    }
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if(position==0){
            return "프로필"
        }else{
            return "받은후기"
        }
    }
}