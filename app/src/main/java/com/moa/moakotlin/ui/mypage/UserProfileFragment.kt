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
import com.bumptech.glide.Glide
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.UserProfileFragmentBinding

class UserProfileFragment : BaseFragment() {


    private lateinit var viewModel: UserProfileViewModel

    lateinit var binding : UserProfileFragmentBinding

    lateinit var navController: NavController

    lateinit var user : User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<UserProfileFragmentBinding>(inflater,R.layout.user_profile_fragment,container,false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        binding.model = viewModel

        navController = findNavController()

        arguments?.let {
            user = it.getParcelable<User>("user")!!
        }

        setViewData()

    }

    override fun onBackPressed() {
        navController.popBackStack()
    }

    private fun setViewData(){
        
        if(user.profileImage.isNotEmpty()){
            Glide.with(binding.root).load(user.profileImage).into(binding.UserProfileImage)
        }

        binding.UserProfileNickName.text = user.nickName

        binding.UserProfileUserAptInfo.text = user.aptName

        binding.UserprofileIntroduction.text = user.introduction


    }


}