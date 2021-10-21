package com.moa.moakotlin.ui.certification

import android.os.Bundle
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentCertificationNoticeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CertificationNoticeFragment : BaseFragment() {

    lateinit var binding: FragmentCertificationNoticeBinding

    lateinit var navController: NavController

    lateinit var model: CertificationNoticeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_certification_notice,container,false)

        navController = findNavController()
        (context as MainActivity).backListener = this
        model = ViewModelProvider(this).get(CertificationNoticeViewModel::class.java)

        binding.model = model

        binding.skipCertificationBtn.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                if(model.signUp()){
                    var bundle = Bundle()
                    bundle.putBoolean("isCertification",false)
                    navController.navigate(R.id.signUpResultFragment,bundle)
                }
            }
        }

        binding.back.setOnClickListener { navController.popBackStack() }
        binding.CertificationBtn.setOnClickListener { navController.navigate(R.id.aptCertificationGuideFragment) }

        return binding.root
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }


}