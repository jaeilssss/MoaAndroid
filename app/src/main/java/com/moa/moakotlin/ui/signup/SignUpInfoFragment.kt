package com.moa.moakotlin.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.base.BaseScrollFragment
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentSignUpBinding
import com.moa.moakotlin.databinding.FragmentSignUpInfoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignUpInfoFragment : BaseScrollFragment() {
    lateinit var binding: FragmentSignUpInfoBinding

    lateinit var model : SignUpInfoViewModel

    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up_info,container,false)
        navController = findNavController()
        model  = ViewModelProvider(this).get(SignUpInfoViewModel::class.java)

        binding.model = model

        if(model.nickCheck.get()==true){
            binding.next.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.next.isClickable =true
        }
        nextButtonChangeBackground()
        if(User.getInstance().aptName.equals("").not()){
            model.aptName.value = User.getInstance().aptName
        }

        binding.nicknameCheckBtn.setOnClickListener {
           nickCheck()
        }

       binding.userInfoEditName.setOnClickListener {
           closeKeyboardVisibility()
       }
        binding.apartCertificationButton.setOnClickListener {
            goToAptSearch()
        }

        binding.next.setOnClickListener{
            goToMyNeighborhood()
        }

        binding.userNickNameEdit.addTextChangedListener{
            if(!model.checkLatestNickname()){
                model.nickCheck.set(false)
                nextButtonChangeBackground()
            }
        }
        observeViewModel()
        return binding.root
    }

    private fun goToMyNeighborhood(){

        navController.navigate(R.id.myNeighborhoodFragment)
    }
    private fun goToAptCertificationNotice(){
        navController.navigate(R.id.certificationNoticeFragment)
    }
    fun nickCheck(){
        CoroutineScope(Dispatchers.Main).launch {
            var result = model.checkNickName()
            if(!result){
                context?.let { showToast(it,"이미 존재하는 닉네임입니다")
                    nextButtonChangeBackground()
                }
            }else{
                context?.let { showToast(it,"사용 가능한 닉네임 입니다") }
                model.setLatestNickname()
                nextButtonChangeBackground()
            }
        }
    }


    private fun observeViewModel(){
        model.name.observe(viewLifecycleOwner, Observer { nextButtonChangeBackground()})
        model.birthDay.observe(viewLifecycleOwner, Observer {nextButtonChangeBackground()})
        model.gender.observe(viewLifecycleOwner, Observer { nextButtonChangeBackground() })
        model.aptName.observe(viewLifecycleOwner, Observer { nextButtonChangeBackground() })
        model.dong.observe(viewLifecycleOwner, Observer { nextButtonChangeBackground() })
        model.hosoo.observe(viewLifecycleOwner, Observer { nextButtonChangeBackground() })
    }
    private fun goToAptSearch(){
        navController.navigate(R.id.aptSearchFragment)
    }

    private fun nextButtonChangeBackground(){
        if(model.checkInfo()){
            binding.next.setBackgroundResource(R.drawable.button_shape_main_color)
            binding.next.isClickable =true
        }else{
            binding.next.setBackgroundResource(R.drawable.shape_unable_radius_15)
            binding.next.isClickable =false
        }
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }
}