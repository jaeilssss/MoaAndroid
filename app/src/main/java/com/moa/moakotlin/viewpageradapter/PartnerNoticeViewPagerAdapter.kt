package com.moa.moakotlin.viewpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.NavController
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.ui.concierge.helper.ConciergeReadIntroduceFragment
import com.moa.moakotlin.ui.concierge.helper.HelperReadReviewFragment
import com.moa.moakotlin.ui.partner.PartnerContractFragment
import com.moa.moakotlin.ui.partner.PartnerNoticeFragment

class PartnerNoticeViewPagerAdapter (var fm: FragmentManager,navController: NavController) : FragmentStatePagerAdapter(fm!!){
    var list = ArrayList<Fragment>()
    init {

        list.add(PartnerNoticeFragment(navController))
        list.add(PartnerContractFragment())
    }
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if(position==0){
            return "공지사항"
        }else{
            return "계약서"
        }
    }

}