package com.moa.moakotlin.ui.signup

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
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.FragmentPhoneCertificationBinding
import com.moa.moakotlin.firebase.phoneCertificationApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch


class PhoneCertification : Fragment() {

    lateinit var binding: FragmentPhoneCertificationBinding

    lateinit var model : PhoneCertificationViewModel

    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_phone_certification,container , false)
        navController = findNavController()
        model = ViewModelProvider(this).get(PhoneCertificationViewModel::class.java)
        binding.model = model
        arguments?.let {
            model.bundle = arguments as Bundle
        }
        activity?.let { model.init(it)}
        binding.success.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if(model.check()==true){
                    showToast("인증이 완료되었습니다")
                }else{
                    showToast("잘못된 인증코드입니다!")
                }
            }
            binding.next.setOnClickListener{
                CoroutineScope(Dispatchers.Main).launch {
                    if(model.next()==true){
                        navController.navigate(R.id.action_phoneCertification_to_HomeFragment)
                    }else{
                        navController.navigate(R.id.action_phoneCertification_to_signUpFragment)
                    }
                }
            }
        }
       return binding.root
    }

    fun showToast(msg : String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
}