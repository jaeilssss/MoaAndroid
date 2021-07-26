package com.moa.moakotlin.ui.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.PolicyFragmentBinding

class PolicyFragment : BaseFragment() {

    private lateinit var binding: PolicyFragmentBinding

    private lateinit var viewModel: PolicyViewModel

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.policy_fragment,container,false)
        viewModel = ViewModelProvider(this).get(PolicyViewModel::class.java)
        binding.model = viewModel
        navController = findNavController()
        (context as MainActivity).backListener = this

        binding.nextBtn.setOnClickListener {

            if(viewModel.checkBox()){
                viewModel.setPolicy()
                navController.navigate(R.id.signUpInfoFragment)
            }
        }
        observeViewModel()
        return binding.root
    }

    override fun onBackPressed() {
         navController.popBackStack()
    }

    fun observeViewModel(){
        viewModel.checkBoxFirst.observe(viewLifecycleOwner, Observer {
            binding.policyAllAgreeCheck.isChecked = it
        })
        viewModel.checkBoxSecond.observe(viewLifecycleOwner, Observer {
            binding.servicePolicyCheck.isChecked = it
            nextBtnSetChangeBackground()
        })
        viewModel.checkBoxThird.observe(viewLifecycleOwner, Observer {
            binding.personalPolicyCheck.isChecked = it
            nextBtnSetChangeBackground()
        })
        viewModel.checkBoxFourth.observe(viewLifecycleOwner, Observer {
            binding.maketingPolicyCheck.isChecked = it
            User.getInstance().isAgreeMarketing = viewModel.checkBoxFourth.value==true
        })
    }

    fun nextBtnSetChangeBackground(){
        if(viewModel.checkBox()){
            binding.nextBtn.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.nextBtn.isClickable = true
        }else{
            binding.nextBtn.setBackgroundResource(R.drawable.shape_unable_radius_15)
            binding.nextBtn.isClickable = false
        }
    }


}