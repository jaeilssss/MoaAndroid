package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.MyConciergeListFragmentBinding
import com.moa.moakotlin.viewpageradapter.MyConciergeAdapter

class MyConciergeListFragment : Fragment() {

    private lateinit var viewModel: MyConciergeListViewModel

    private lateinit var binding : MyConciergeListFragmentBinding

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater ,R.layout.my_concierge_list_fragment,container,false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MyConciergeListViewModel::class.java)

        binding.model = viewModel

        navController = findNavController()
        var viewPagerAdapter : MyConciergeAdapter = MyConciergeAdapter(parentFragmentManager)

        binding.MyConciergeViewPager.adapter = viewPagerAdapter
        binding.MyConciergeTabLayout.setupWithViewPager(binding.MyConciergeViewPager)

    }

}