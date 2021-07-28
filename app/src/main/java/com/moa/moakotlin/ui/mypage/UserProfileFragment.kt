package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.UserProfileFragmentBinding
import com.moa.moakotlin.recyclerview.review.ReviewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch

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

        (context as MainActivity).backListener = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        binding.model = viewModel

        navController = findNavController()

        arguments?.let {
            if(it.getParcelable<User>("user")==null){
                user = User.getInstance()
            }else{
                user = it.getParcelable<User>("user")!!
            }

        }
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getReviewList(user.uid)
        }

        setProfileImage()
        setIntroduce()


        var adapter = ReviewAdapter()

        binding.UserProfileRcv.adapter = adapter

        binding.UserProfileRcv.layoutManager = LinearLayoutManager(context)

        viewModel.reviewList.observe(viewLifecycleOwner, Observer {
            println(it.size)
            adapter.submitList(it)

        })


        binding.UserprofileModifyBtn.setOnClickListener { navController.navigate(R.id.action_userProfileFragment_to_userProfileModifyFragment) }
    }
    fun setProfileImage(){
        if(user.profileImage.isNotEmpty()){
            Glide.with(binding.root).load(user.profileImage).into(binding.UserProfileImage)
        }
    }
    override fun onBackPressed() {
        navController.popBackStack(R.id.MyPageFragment,false)

    }

    fun setIntroduce(){
        if(user.introduction.isEmpty()){
            viewModel.introduction.value="아직 자기 소개를 작성하지 않으셨습니다!"
        }else{
            viewModel.introduction.value = user.introduction
        }
    }
}