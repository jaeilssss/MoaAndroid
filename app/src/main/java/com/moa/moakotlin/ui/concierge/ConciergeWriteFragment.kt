package com.moa.moakotlin.ui.concierge

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
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.databinding.FragmentConciergeWriteBinding

class ConciergeWriteFragment : Fragment() {

        lateinit var binding : FragmentConciergeWriteBinding

        lateinit var model  : ConciergeWriteViewModel

        lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_concierge_write,container,false)

        navController = findNavController()

        model = ViewModelProvider(this).get(ConciergeWriteViewModel::class.java)

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