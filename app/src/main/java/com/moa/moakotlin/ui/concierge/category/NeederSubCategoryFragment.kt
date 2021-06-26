package com.moa.moakotlin.ui.concierge.category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R

class NeederSubCategoryFragment : Fragment() {

    companion object {
        fun newInstance() = NeederSubCategoryFragment()
    }

    private lateinit var viewModel: NeederSubCategoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.needer_sub_category_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NeederSubCategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}