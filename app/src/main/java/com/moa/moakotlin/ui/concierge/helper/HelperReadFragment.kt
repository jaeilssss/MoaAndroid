package com.moa.moakotlin.ui.concierge.helper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Kid
import com.moa.moakotlin.databinding.FragmentNeederReadBinding


class HelperReadFragment : Fragment() {

    lateinit var binding : FragmentNeederReadBinding

    lateinit var navController: NavController

    lateinit var model: HelperReadViewModel

    lateinit var kid : Kid
    lateinit var  bundle : Bundle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_needer_read, container, false)

        navController = findNavController()

        model = ViewModelProvider(this).get(HelperReadViewModel::class.java)
        
        binding.model = model

        setUpFragment(HelperReadIntroduceFragment())

        binding.HelperMainIntroduce.setOnClickListener {
            setUpFragment(HelperReadIntroduceFragment())
        }
        binding.HelperMainReview.setOnClickListener {
            setUpFragment(HelperReadReviewFragment())
        }
        return binding.root
    }

    fun setUpFragment(fragment : Fragment){
        val fragmentManager: FragmentManager = activity?.supportFragmentManager!!
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.HelperMainFragmentView, fragment).commit()
    }
}