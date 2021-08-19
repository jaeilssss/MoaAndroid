package com.moa.moakotlin.ui.login


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.WHITE
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.moa.moakotlin.LoadingActivity
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.*
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.FragmentLoginBinding
import com.moa.moakotlin.firebase.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.log

class LoginFragment : BaseScrollFragment() {

   lateinit var binding: FragmentLoginBinding
    lateinit var navController: NavController
    lateinit var loginViewModel :LoginViewModel
     var countDownTimer: CountDownTimer ?=null
    lateinit var transfer: Transfer
    var sendPhoneNumber = ""
    var isSend = false
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
        (context as MainActivity).backListener = this
        binding.lifecycleOwner=this
        activity?.let { loginViewModel.init(it) }
        binding.sendMessageBtn.setOnClickListener {
            if(sendPhoneNumber.equals(loginViewModel.phoneNumber?.value)){
                showToast(activity?.applicationContext!!,"이미 인증메시지를 보냈습니다")
            } else{

                        countDownTimer?.cancel()
                        countDownTimer?.onFinish()
                    countDownTimer =  createCountDownTimer(1200*100L)
                    countDownTimer?.start()
                    loginViewModel.sendMessage()
                    sendPhoneNumber = loginViewModel.phoneNumber?.value!!
                    isSend  =true
                    binding.certificationCode.isVisible = true
                    binding.checkCertificationCodeBtn.isVisible = true


            }
        }

        binding.checkCertificationCodeBtn.setOnClickListener {

                // 이미 회원가입이 완료 된 유저 이면 메인 화면으로
                // 아니면 회원가입 페이지로 이동

            // 인증코드가 맞는지도 확인 할 것!!!
            CoroutineScope(Dispatchers.Main).launch {
                binding.loginLoadingProgressBar.show()
                activity?.getWindow()?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                if(loginViewModel.checkCertificationMessage()){
                    User.getInstance().phoneNumber = binding.phoneNumberEditText.text.toString()
                    User.getInstance().phoneUid = FirebaseAuth.getInstance().uid.toString()

                    if(FirebaseAuth.getInstance().currentUser?.let { it1 ->
                            loginViewModel.getUserInfo(
                                it1.uid)
                        } == true){
                            var intent = Intent(context,LoadingActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                        navController.navigate(R.id.HomeFragment)
                        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        binding.loginLoadingProgressBar.hide()
                    }else{
                        navController.navigate(R.id.policyFragment)
                        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        binding.loginLoadingProgressBar.hide()
                    }
                }else{
                    showToast(activity?.applicationContext!!,"정확하지 않은 인증번호 입니다")
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    binding.loginLoadingProgressBar.hide()
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
        binding.backButton.setOnClickListener {  navController.popBackStack() }

        return binding.root
    }

    override fun onBackPressed() {
        navController.popBackStack()
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
        binding.sendMessageBtn.text = "안증문자 다시 받기 ${minute}분 ${second}초"
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
    private val callbacks by lazy {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(phoneAuth: PhoneAuthCredential) {
                    println(phoneAuth.smsCode.toString())


            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }
        }
    }

    private fun initViewModelCallback() {


        activity?.let {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+82" + "01077381556", // Phone number to verify
                    60, // Timeout duration
                    TimeUnit.SECONDS, // Unit of timeout
                    it, // Activity (for callback binding)
                    callbacks
            )
        } // OnVerificationStateChangedCallbacks

    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = activity?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(90L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(it as MainActivity)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
        }
        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

    }

}


