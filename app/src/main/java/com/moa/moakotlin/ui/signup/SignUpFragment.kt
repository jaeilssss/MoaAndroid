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
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding

    lateinit var model: SignUpViewModel

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_sign_up, container , false)
        navController = findNavController()
        model = ViewModelProvider(this).get(SignUpViewModel::class.java)
        binding.model=model
        binding.next.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_signUpInfoFragment)
        }
        return binding.root
    }


}