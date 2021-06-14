package com.moa.moakotlin.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.databinding.FragmentSignUpBinding
import com.moa.moakotlin.databinding.FragmentSignUpInfoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignUpInfoFragment : BaseFragment() {
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

        binding.model = model

        binding.nicknameCheckBtn.setOnClickListener {
           nickCheck()
        }

        binding.apartCertificationEdit.setOnClickListener {
            goToAptSearch()
        }
        return binding.root
    }



    fun nickCheck(){
        CoroutineScope(Dispatchers.Main).launch {
            var result = model.checkNickName()
            if(!result){
                context?.let { ShowToast(it,"이미 존재하는 닉네임입니다") }
            }else{
                context?.let { ShowToast(it,"사용 가능한 닉네임 입니다") }
            }
        }
    }

    fun goToAptSearch(){
        navController.navigate(R.id.aptSearchFragment)
    }


    override fun onBackPressed() {
        navController.popBackStack()
    }
}