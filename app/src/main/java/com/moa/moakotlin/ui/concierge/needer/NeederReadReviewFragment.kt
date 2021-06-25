package com.moa.moakotlin.ui.concierge.needer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R

class NeederReadReviewFragment : Fragment() {

    companion object {
        fun newInstance() = NeederReadReviewFragment()
    }

    private lateinit var viewModel: NeederReadReviewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.needer_read_review_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NeederReadReviewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}