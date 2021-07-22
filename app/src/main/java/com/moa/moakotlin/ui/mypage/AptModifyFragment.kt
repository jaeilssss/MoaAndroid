package com.moa.moakotlin.ui.mypage

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
import com.moa.moakotlin.databinding.AptModifyFragmentBinding

class AptModifyFragment : Fragment() {

    private lateinit var viewModel: AptModifyViewModel


    private lateinit var binding : AptModifyFragmentBinding

    private lateinit var navController: NavController


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.apt_modify_fragment , container , false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AptModifyViewModel::class.java)
        navController = findNavController()

        binding.AptModifyAptEdit.setOnClickListener { goToSearchView()  }

    }

    fun goToSearchView(){

    
    }

}