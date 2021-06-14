package com.moa.moakotlin.ui.signup

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
import com.moa.moakotlin.databinding.PolicyFragmentBinding

class PolicyFragment : Fragment() {

    private lateinit var binding: PolicyFragmentBinding

    private lateinit var viewModel: PolicyViewModel

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.policy_fragment,container,false)
        viewModel = ViewModelProvider(this).get(PolicyViewModel::class.java)
        binding.model = viewModel
        navController = findNavController()


        binding.nextBtn.setOnClickListener {

            if(viewModel.checkBox()){
                navController.navigate(R.id.signUpInfoFragment)
            }
        }

        return binding.root
    }



}