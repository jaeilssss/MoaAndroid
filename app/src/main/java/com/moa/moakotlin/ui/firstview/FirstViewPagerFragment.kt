package com.moa.moakotlin.ui.firstview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R

class FirstViewPagerFragment : Fragment() {

    companion object {
        fun newInstance() = FirstViewPagerFragment()
    }

    private lateinit var viewModel: FirstViewPagerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.first_view_pager_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FirstViewPagerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}