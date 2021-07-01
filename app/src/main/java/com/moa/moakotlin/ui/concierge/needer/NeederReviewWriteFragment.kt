package com.moa.moakotlin.ui.concierge.needer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R

class NeederReviewWriteFragment : Fragment() {

    companion object {
        fun newInstance() = NeederReviewWriteFragment()
    }

    private lateinit var viewModel: NeederReviewWriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.needer_review_write_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NeederReviewWriteViewModel::class.java)
        // TODO: Use the ViewModel
    }

}