package com.moa.moakotlin.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentAptInfoBinding

class AptInfoFragment : Fragment() {

    lateinit var binding:FragmentAptInfoBinding

    lateinit var model: AptInfoViewModel

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_apt_info,container,false)

        navController = findNavController()

        model = context?.let {AptInfoViewModel(navController,it)}!!

        arguments?.let {
            model.bundle = arguments as Bundle
        }
        binding.model = model

        return binding.root
    }

}