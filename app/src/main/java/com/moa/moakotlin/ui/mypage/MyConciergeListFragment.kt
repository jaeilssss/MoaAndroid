package com.moa.moakotlin.ui.mypage

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.databinding.MyConciergeListFragmentBinding
import com.moa.moakotlin.viewpageradapter.MyConciergeAdapter

class MyConciergeListFragment : BaseFragment() {

    private lateinit var viewModel: MyConciergeListViewModel

    private lateinit var binding : MyConciergeListFragmentBinding

    private lateinit var navController: NavController

     lateinit var viewPagerAdapter : MyConciergeAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)

        myActivity = activity as MainActivity

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater ,R.layout.my_concierge_list_fragment,container,false)
        viewPagerAdapter = MyConciergeAdapter(childFragmentManager)
        binding.MyConciergeViewPager.adapter = viewPagerAdapter
        binding.MyConciergeTabLayout.setupWithViewPager(binding.MyConciergeViewPager)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MyConciergeListViewModel::class.java)

        binding.model = viewModel

        navController = findNavController()





    }


    override fun onDestroyView() {

        super.onDestroyView()
    }

    override fun onBackPressed() {
        navController.popBackStack(R.id.MyPageFragment,false)
    }
}