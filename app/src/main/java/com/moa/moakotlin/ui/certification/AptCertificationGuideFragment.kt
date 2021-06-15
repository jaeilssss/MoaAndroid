package com.moa.moakotlin.ui.certification

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R

class AptCertificationGuideFragment : Fragment() {

    companion object {
        fun newInstance() = AptCertificationGuideFragment()
    }

    private lateinit var viewModel: AptCertificationGuideViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.apt_certification_guide_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AptCertificationGuideViewModel::class.java)
        // TODO: Use the ViewModel
    }

}