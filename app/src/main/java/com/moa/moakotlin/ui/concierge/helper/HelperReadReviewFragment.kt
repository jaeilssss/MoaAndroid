package com.moa.moakotlin.ui.concierge.helper

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R

class HelperReadReviewFragment : Fragment() {

    companion object {
        fun newInstance() = HelperReadReviewFragment()
    }

    private lateinit var viewModel: HelperReadReviewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.helper_read_review_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HelperReadReviewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}