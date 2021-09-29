package com.moa.moakotlin.ui.partner

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.PartnerNoticeMainFragmentBinding
import com.moa.moakotlin.viewpageradapter.PartnerNoticeViewPagerAdapter

class PartnerNoticeMainFragment : Fragment() {

    companion object {
        fun newInstance() = PartnerNoticeMainFragment()
    }

    private lateinit var viewModel: PartnerNoticeMainViewModel

    private lateinit var viewPagerAdapter: PartnerNoticeViewPagerAdapter

    private lateinit var binding : PartnerNoticeMainFragmentBinding

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.partner_notice_main_fragment , container , false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PartnerNoticeMainViewModel::class.java)
        binding.model = viewModel
        navController = findNavController()
        viewPagerAdapter = PartnerNoticeViewPagerAdapter(childFragmentManager , navController)
        binding.partnerNoticeViewPager.adapter = viewPagerAdapter
        binding.partnerNoticeTabLayout.setupWithViewPager(binding.partnerNoticeViewPager)
    }

}