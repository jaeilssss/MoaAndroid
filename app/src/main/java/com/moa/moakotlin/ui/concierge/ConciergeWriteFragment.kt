package com.moa.moakotlin.ui.concierge

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
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentConciergeWriteBinding

class ConciergeWriteFragment : BaseFragment() {

        lateinit var binding : FragmentConciergeWriteBinding

        lateinit var model  : ConciergeWriteViewModel

        lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_concierge_write,container,false)

        navController = findNavController()

        model = ViewModelProvider(this).get(ConciergeWriteViewModel::class.java)

        binding.model = model

        binding.ConciergeWriteTalentSharingLayout.setOnClickListener {

            if(User.getInstance().certificationStatus.equals("인증").not()){
                showToast(activity?.applicationContext!!,"아파트 인증 한 유저만 이용가능합니다!")
            }else{
                navController.navigate(R.id.neederWritePageFragment)
            }
        }

        return binding.root
    }

    override fun onBackPressed() {
        TODO("Not yet implemented")
    }


}