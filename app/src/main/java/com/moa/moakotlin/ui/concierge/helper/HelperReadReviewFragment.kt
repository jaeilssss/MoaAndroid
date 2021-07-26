package com.moa.moakotlin.ui.concierge.helper

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.moa.moakotlin.R
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.HelperReadReviewFragmentBinding
import com.moa.moakotlin.recyclerview.review.ReviewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HelperReadReviewFragment(var helper : Helper) : Fragment() {


    private lateinit var viewModel: HelperReadReviewViewModel

    private lateinit var binding : HelperReadReviewFragmentBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater , R.layout.helper_read_review_fragment,container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HelperReadReviewViewModel::class.java)
        binding.model =viewModel
        var adapter = ReviewAdapter()

        binding.helperReadReviewRcv.adapter = adapter

        binding.helperReadReviewRcv.layoutManager = LinearLayoutManager(context)

        viewModel.reviewList.observe(viewLifecycleOwner, Observer {
            println(it.size)
            adapter.submitList(it)

        })

        init()

    }

    fun init(){
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getReviewList(helper.uid)
        }

    }

}