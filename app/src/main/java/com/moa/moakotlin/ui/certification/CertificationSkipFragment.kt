package com.moa.moakotlin.ui.certification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentCertificationSkipBinding

class CertificationSkipFragment : Fragment() {

    lateinit var binding: FragmentCertificationSkipBinding

    lateinit var model: CertificationSkipViewModel

    lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_certification_skip,container,false)

        navController = findNavController()

        model = context?.let {CertificationSkipViewModel(navController,it)}!!

        binding.model = model

        arguments?.let {
            model.bundle = arguments as Bundle
        }


        return binding.root
    }


}