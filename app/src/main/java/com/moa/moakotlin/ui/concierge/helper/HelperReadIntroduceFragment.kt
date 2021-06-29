package com.moa.moakotlin.ui.concierge.helper

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R

class HelperReadIntroduceFragment : Fragment() {

    companion object {
        fun newInstance() = HelperReadIntroduceFragment()
    }

    private lateinit var viewModel: HelperReadIntroduceViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.concierge_read_introduce_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HelperReadIntroduceViewModel::class.java)
        // TODO: Use the ViewModel
    }

}