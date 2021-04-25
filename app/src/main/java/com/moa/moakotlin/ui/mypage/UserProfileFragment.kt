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
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.UserProfileFragmentBinding

class UserProfileFragment : Fragment() {


    private lateinit var viewModel: UserProfileViewModel

    lateinit var binding : UserProfileFragmentBinding

    lateinit var navController: NavController

    lateinit var user : User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<UserProfileFragmentBinding>(inflater,R.layout.user_profile_fragment,container,false)

         user = User()
        arguments?.let {
            user = (arguments as Bundle).getParcelable<User>("user")!!
        }

        navController = findNavController()
        initView()

        binding.userProfileModify.setOnClickListener {
            navController.navigate(R.id.action_userProfileFragment_to_userProfileModifyFragment)
        }

        return binding.root
    }

    fun initView(){
        if(user.profileImage!=null){
            context?.let { Glide.with(it).load(user.profileImage).into(binding.userProfileImage)}
        }
        binding.userProfileNick.text = user.nickName

        binding.userProfileMySelf.text = user.introduction

        binding.userProfileAptName.text = user.aptName
    }

}