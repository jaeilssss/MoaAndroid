package com.moa.moakotlin.ui.concierge.category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.NeederMainCategoryFragmentBinding

class NeederMainCategoryFragment : Fragment() {

    companion object {
        fun newInstance() = NeederMainCategoryFragment()
    }

    private lateinit var viewModel: NeederMainCategoryViewModel

    private lateinit var binding : NeederMainCategoryFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.needer_main_category_fragment,container,false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NeederMainCategoryViewModel::class.java)



    }

}