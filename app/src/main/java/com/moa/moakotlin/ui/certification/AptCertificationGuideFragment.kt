package com.moa.moakotlin.ui.certification

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.AptCertificationFragmentBinding
import com.moa.moakotlin.databinding.AptCertificationGuideFragmentBinding

class AptCertificationGuideFragment : Fragment() {


    lateinit var binding : AptCertificationGuideFragmentBinding

    lateinit var navController: NavController

    private lateinit var viewModel: AptCertificationGuideViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater,R.layout.apt_certification_guide_fragment,container,false)

        navController = findNavController()

        viewModel = ViewModelProvider(this).get(AptCertificationGuideViewModel::class.java)

        binding.guildNextBtn.setOnClickListener {
            navController.navigate(R.id.aptCertificationFragment)
        }
        return binding.root

    }



}