package com.moa.moakotlin.ui.certification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentCertificationNoticeBinding

class CertificationNoticeFragment : Fragment() {

    lateinit var binding: FragmentCertificationNoticeBinding

    lateinit var navController: NavController

    lateinit var model: CertificationNoticeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_certification_notice,container,false)
        navController = findNavController()
        model = ViewModelProvider(this).get(CertificationNoticeViewModel::class.java)

        binding.model = model

        arguments?.let {
            model.bundle = arguments as Bundle
        }
        return binding.root
    }


}