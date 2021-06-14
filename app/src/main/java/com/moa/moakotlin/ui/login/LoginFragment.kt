package com.moa.moakotlin.ui.login


import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.Transfer
import com.moa.moakotlin.databinding.FragmentLoginBinding
import kotlin.math.log

class LoginFragment : Fragment() {

   lateinit var binding: FragmentLoginBinding
    lateinit var navController: NavController
    lateinit var loginViewModel :LoginViewModel
     var countDownTimer: CountDownTimer ?=null
    lateinit var transfer: Transfer


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(activity != null){
            transfer = activity as Transfer
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
            navController = findNavController()

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.model = loginViewModel

        binding.lifecycleOwner=this
        activity?.let { loginViewModel.init(it) }
        binding.sendMessageBtn.setOnClickListener {
            if(countDownTimer !=null){
                countDownTimer?.cancel()
            }
           countDownTimer =  createCountDownTimer(1200*100L)
            countDownTimer?.start()
//            loginViewModel.sendMessage()
            binding.certificationCode.isVisible = true
            binding.checkCertificationCodeBtn.isVisible = true
        }

        binding.checkCertificationCodeBtn.setOnClickListener {
                // 이미 회원가입이 완료 된 유저 이면 메인 화면으로


                // 아니면 회원가입 페이지로 이동

            navController.navigate(R.id.policyFragment)


        }
        transfer.bottomGone()

        return binding.root
    }
    private fun createCountDownTimer(initialMililis : Long) =
        object : CountDownTimer(initialMililis,1000L){
            override fun onFinish() {

            }

            override fun onTick(remainTime: Long) {
             updateRemainTime(remainTime)
            }

        }
    @SuppressLint("SetTextI18n")
    private fun updateRemainTime(remainMillis : Long){
        val remainSeconds = remainMillis/1000L

        val minute = "%02d".format(remainSeconds/60)
        val second = "%02d".format(remainSeconds%60)
        binding.sendMessageBtn.text = "인증문자 다시 받기 (${minute}분 ${second}초)"
    }
}


