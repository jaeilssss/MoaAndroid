package com.moa.moakotlin.ui.home

import android.content.Context
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
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.base.onBackPressedListener
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment(){
    var lastTimeBackPressed : Long = 0
lateinit var transfer: Transfer

    lateinit var binding: FragmentHomeBinding

    lateinit var navController: NavController

    lateinit var model: HomeViewModel
    var i = 0
    override fun onAttach(context: Context) {
        super.onAttach(context)
        println(i++)
        if(activity != null  ){
            transfer = activity as Transfer
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)

        navController = findNavController()

        model = ViewModelProvider(this).get(HomeViewModel::class.java)

        model.init()

        binding.model = model

        transfer.bottomVisible()

        binding.concierge.setOnClickListener{
            navController.navigate(R.id.action_HomeFragment_to_ConciergeMainFragment)
        }


        binding.voice.setOnClickListener {
            navController.navigate(R.id.voiceMainFragment)
        }
        return binding.root

    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            activity?.finish()
            return
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(context,"종료하려면 한번 더 누르세요",Toast.LENGTH_SHORT).show()
    }
}