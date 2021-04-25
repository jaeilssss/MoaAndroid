package com.moa.moakotlin.ui.select

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Picture

import com.moa.moakotlin.databinding.FragmentKidSitterWriteBinding

class KidSitterWriteFragment : Fragment() {

        lateinit var binding : FragmentKidSitterWriteBinding

        lateinit var model  : KidSitterWriteViewModel

        lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_kid_sitter_write,container,false)

        navController = findNavController()

        model = context?.let {

            KidSitterWriteViewModel(
                navController,
                it
            )
        }!!

        binding.goToKidWrite.setOnClickListener{
            Picture.deleteInstance()
            navController.navigate(R.id.action_kidSelectFragment_to_kidWritePageFragment)
        }

        binding.goToSitterWrite.setOnClickListener {
            navController.navigate(R.id.action_KidSitterWriteFragment_to_sitterWriteFragment)
        }
        return binding.root
    }


}