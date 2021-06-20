package com.moa.moakotlin.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentSignUpInfoBinding
import com.moa.moakotlin.databinding.FragmentSignUpResultBinding

class SignUpResultFragment : Fragment() {

    lateinit var binding: FragmentSignUpResultBinding

    lateinit var model: SignUpResultViewModel

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up_result,container,false)

        navController = findNavController()

        model = ViewModelProvider(this).get(SignUpResultViewModel::class.java)

        binding.model = model
        var bundle = arguments as Bundle
        var check = bundle.getBoolean("isCertified")
        if(check.not()){
            binding.SignUpResultMessage.text = resources.getString(R.string.aptUncertifiedDetail)
        }


        binding.signUpResultNext.setOnClickListener {
            navController.navigate(R.id.HomeFragment)
        }
        return binding.root
    }


}