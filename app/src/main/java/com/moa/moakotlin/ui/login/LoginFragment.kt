package com.moa.moakotlin.ui.login


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Color.WHITE
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.moa.moakotlin.R
import com.moa.moakotlin.base.*
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

class LoginFragment : BaseScrollFragment() {

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
                showToast(activity?.applicationContext!!,"이미 인증메시지를 보냈습니다")
            }else{
                countDownTimer =  createCountDownTimer(1200*100L)
                countDownTimer?.start()
                loginViewModel.sendMessage()
                binding.certificationCode.isVisible = true
                binding.checkCertificationCodeBtn.isVisible = true
            }

        }

        binding.checkCertificationCodeBtn.setOnClickListener {

                // 이미 회원가입이 완료 된 유저 이면 메인 화면으로
                // 아니면 회원가입 페이지로 이동

            // 인증코드가 맞는지도 확인 할 것!!!
            CoroutineScope(Dispatchers.Main).launch {
                if(loginViewModel.checkCertificationMessage()){
                    User.getInstance().phoneNumber = binding.phoneNumberEditText.text.toString()
                    User.getInstance().uid = FirebaseAuth.getInstance().uid.toString()

                    if(loginViewModel.getUserInfo(FirebaseAuth.getInstance().currentUser.uid)){
                        navController.navigate(R.id.HomeFragment)
                    }else{
                        navController.navigate(R.id.policyFragment)
                    }
                }else{
                    showToast(activity?.applicationContext!!,"정확하지 않은 인증번호 입니다")
                }

            }

        }
        activity?.window?.let { keyboardVisibility(it,binding.scv) }
        transfer.bottomGone()

        loginViewModel.code.observe(viewLifecycleOwner, Observer {
            if(it.length==6){
                binding.checkCertificationCodeBtn.setBackgroundResource(R.drawable.button_shape_main_color)
                binding.checkCertificationCodeBtn.setTextColor(Color.BLACK)
                binding.checkCertificationCodeBtn.isClickable=true
            }else{
                binding.checkCertificationCodeBtn.setBackgroundResource(R.drawable.shape_unable_radius_15)
                binding.checkCertificationCodeBtn.setTextColor(Color.WHITE)
                binding.checkCertificationCodeBtn.isClickable=false
            }
        })

        setChangeButton()
        loginViewModel.phoneNumber.observe(viewLifecycleOwner, Observer {
            setChangeButton()
        })
        return binding.root
    }

    override fun onBackPressed() {
        navController.popBackStack(R.id.FirstFragment,false)
    }

    private fun createCountDownTimer(initialMililis : Long) =
        object : CountDownTimer(initialMililis,1000L){
            override fun onFinish() {
                countDownTimer = null
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
        binding.sendMessageBtn.text = "${minute}분 ${second}초"
    }

    private fun setChangeButton(){
        if(loginViewModel.checkPhoneNumber()){
            binding.sendMessageBtn.setBackgroundResource(R.drawable.shape_moa_black_radius_10)
            binding.sendMessageBtn.isClickable = true
            binding.sendMessageBtn.setTextColor(Color.WHITE)
        }else{
            binding.sendMessageBtn.setBackgroundResource(R.drawable.shape_unable_radius_15)
            binding.sendMessageBtn.isClickable = false
            binding.sendMessageBtn.setTextColor(Color.WHITE)
        }
    }

}


