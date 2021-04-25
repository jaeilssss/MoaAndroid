package com.moa.moakotlin.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

   lateinit var binding: FragmentLoginBinding
    lateinit var navController: NavController
    lateinit var loginViewModel :LoginViewModel

    lateinit var transfer: Transfer

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(activity != null  ){
            transfer = activity as Transfer
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
            navController = findNavController()

        loginViewModel = LoginViewModel(navController)
        binding.model = loginViewModel

        binding.lifecycleOwner=this

        transfer.bottomGone()

        binding.loginButton.setOnClickListener{
            loginViewModel.phoneNumber.get()?.let {
                if(it.length==11){
                   var bundle = loginViewModel.login()
                    navController.navigate(R.id.action_loginFragment_to_phoneCertification,bundle)
                }else{
                    Toast.makeText(context,"핸드폰 번호를 재대로 입력해주세요",Toast.LENGTH_SHORT).show()
                }
            }
        }


        return binding.root
    }


}


