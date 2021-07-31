package com.moa.moakotlin.ui.concierge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.databinding.FragmentConciergeMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        (context as MainActivity).backListener = this

        navController = findNavController()

        model = ViewModelProvider(this).get(ConciergeMainViewModel::class.java)

        binding.ConciergeMainTalentSharingLayout.setOnClickListener {
            navController.navigate(R.id.HelperMainFragment)
        }

        binding.ConciergeMainHelpLayout.setOnClickListener {
            navController.navigate(R.id.action_ConciergeMainFragment_to_neederMainFragment)
        }

        binding.back.setOnClickListener { navController.popBackStack() }
        binding.model =model
        return binding.root
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

}