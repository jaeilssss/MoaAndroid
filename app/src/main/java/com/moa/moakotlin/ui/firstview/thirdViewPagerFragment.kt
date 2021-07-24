package com.moa.moakotlin.ui.firstview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment

class thirdViewPagerFragment : BaseFragment() {

    companion object {
        fun newInstance() = thirdViewPagerFragment()
    }

    private lateinit var viewModel: ThirdViewPagerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.third_view_pager_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (context as MainActivity).backListener = this
        viewModel = ViewModelProvider(this).get(ThirdViewPagerViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onBackPressed() {

    }

}