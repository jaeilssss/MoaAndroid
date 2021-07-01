package com.moa.moakotlin.ui.concierge.category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.NeederSubCategoryFragmentBinding

class NeederSubCategoryFragment : Fragment() {

    companion object {
        fun newInstance() = NeederSubCategoryFragment()
    }

    private lateinit var viewModel: NeederSubCategoryViewModel

    private lateinit var binding : NeederSubCategoryFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.needer_sub_category_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NeederSubCategoryViewModel::class.java)


    }

}