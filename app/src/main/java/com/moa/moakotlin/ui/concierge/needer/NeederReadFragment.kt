package com.moa.moakotlin.ui.concierge.needer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Kid
import com.moa.moakotlin.databinding.FragmentNeederReadBinding


class NeederReadFragment : Fragment() {

    lateinit var binding : FragmentNeederReadBinding

    lateinit var navController: NavController

    lateinit var model: NeederReadViewModel

    lateinit var kid : Kid
    lateinit var  bundle : Bundle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_needer_read, container, false)



        binding.NeederMainIntroduce.setOnClickListener {
            println("눌림~")


            val fragmentManager: FragmentManager = activity?.supportFragmentManager!!
            val fragmentTransaction: FragmentTransaction =            fragmentManager.beginTransaction()
            var fragment = NeederReadIntroduceFragment()
            fragmentTransaction.replace(R.id.NeederMainFragmentView, fragment).commit()
        }

        binding.NeederMainReview.setOnClickListener {
            println("눌림222~")
//            childFragmentManager
            val fragmentManager: FragmentManager = activity?.supportFragmentManager!!
            val fragmentTransaction: FragmentTransaction =  fragmentManager.beginTransaction()
            var fragment = NeederReadReviewFragment()
            fragmentTransaction.replace(R.id.NeederMainFragmentView, fragment).commit()


        }
        return binding.root
    }
}