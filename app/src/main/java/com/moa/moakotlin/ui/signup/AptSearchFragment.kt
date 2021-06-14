package com.moa.moakotlin.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentAptSearchBinding


class AptSearchFragment : Fragment() {

lateinit var binding : FragmentAptSearchBinding

lateinit var navController: NavController

lateinit var model : AptSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_apt_search,container,false)

        navController = findNavController()

        model = ViewModelProvider(this).get(AptSearchViewModel::class.java)

        binding.model = model

        model.searchContent.observe(viewLifecycleOwner, Observer {
                model.updateSearchView()
        })

        return binding.root
    }

  
}