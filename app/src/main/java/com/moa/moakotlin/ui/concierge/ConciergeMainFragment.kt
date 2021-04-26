package com.moa.moakotlin.ui.concierge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.databinding.FragmentConciergeMainBinding
import com.moa.moakotlin.viewmodelfactory.KidViewModelFactory

class ConciergeMainFragment : BaseFragment() {


    lateinit var binding: FragmentConciergeMainBinding

    lateinit var navController: NavController

    lateinit var model : ConciergeMainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_concierge_main,container,false)

        navController = findNavController()

        model = ViewModelProvider(this,KidViewModelFactory(navController))
            .get(ConciergeMainViewModel::class.java)

        binding.kidViewGoToKid.setOnClickListener {
            navController.navigate(R.id.action_ConciergeMainFragment_to_kidMainFragment)
        }
        binding.kidViewGoToSitter.setOnClickListener {
            navController.navigate(R.id.action_ConciergeMainFragment_to_sitterMainFragment)
        }
        binding.model =model
        return binding.root
    }

    override fun onBackPressed() {
        navController.navigate(R.id.HomeFragment)
    }

}