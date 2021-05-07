package com.moa.moakotlin.ui.certification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentCertificationSkipBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CertificationSkipFragment : Fragment() {

    lateinit var binding: FragmentCertificationSkipBinding

    lateinit var model: CertificationSkipViewModel

    lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_certification_skip,container,false)

        navController = findNavController()

        model = context?.let {CertificationSkipViewModel(navController,it)}!!

        binding.model = model

        binding.next.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                var result = model.signup()
                if(result==true){
                    navController.navigate(R.id.action_certificationSkipFragment_to_signUpResultFragment)
                }else{
                    setToast("ERROR가 발생했습니다")
                }
            }
        }
        arguments?.let {
            model.bundle = arguments as Bundle
        }

        return binding.root
    }

    fun setToast(msg : String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

}