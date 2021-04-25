package com.moa.moakotlin.ui.select

import android.os.BadParcelableException
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
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.databinding.FragmentKidSitterViewSelectBinding
import com.moa.moakotlin.viewmodelfactory.KidViewModelFactory

class KidSitterViewSelectFragment : BaseFragment() {


    lateinit var binding: FragmentKidSitterViewSelectBinding

    lateinit var navController: NavController

    lateinit var model : KidSitterViewSelectViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_kid_sitter_view_select,container,false)

        navController = findNavController()

        model = ViewModelProvider(this,KidViewModelFactory(navController))
            .get(KidSitterViewSelectViewModel::class.java)


        binding.kidViewGoToKid.setOnClickListener {
            navController.navigate(R.id.action_kidViewSelectFragment_to_kidMainFragment)
        }
        binding.kidViewGoToSitter.setOnClickListener {
            navController.navigate(R.id.action_kidViewSelectFragment_to_sitterMainFragment)
        }
        binding.model =model
        return binding.root
    }

    override fun onBackPressed() {
        navController.navigate(R.id.HomeFragment)
    }

}