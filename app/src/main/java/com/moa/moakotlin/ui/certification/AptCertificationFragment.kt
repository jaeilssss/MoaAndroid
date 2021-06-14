package com.moa.moakotlin.ui.certification

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moa.moakotlin.R

class AptCertificationFragment : Fragment() {

    companion object {
        fun newInstance() = AptCertificationFragment()
    }

    private lateinit var viewModel: AptCertificationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.apt_certification_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AptCertificationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}