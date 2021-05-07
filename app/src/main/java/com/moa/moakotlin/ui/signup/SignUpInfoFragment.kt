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
import com.moa.moakotlin.databinding.FragmentSignUpInfoBinding


class SignUpInfoFragment : Fragment() {
    lateinit var binding: FragmentSignUpInfoBinding

    lateinit var model : SignUpInfoViewModel

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up_info,container,false)
        navController = findNavController()
        model  = ViewModelProvider(this).get(SignUpInfoViewModel::class.java)
        binding.nicknameCheck.setOnClickListener{
                model.nickNameCheck()
        }
        binding.model = model
        return binding.root
    }
}