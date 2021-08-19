package com.moa.moakotlin.ui.mypage

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.ChangeMyPhoneNumberFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangeMyPhoneNumberFragment : BaseFragment() {

    companion object {
        fun newInstance() = ChangeMyPhoneNumberFragment()
    }

    private lateinit var viewModel: ChangeMyPhoneNumberViewModel

    private lateinit var binding : ChangeMyPhoneNumberFragmentBinding

    private lateinit var navController: NavController

    var isSend = false

    var countDownTimer: CountDownTimer ?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding  = DataBindingUtil.inflate(inflater,R.layout.change_my_phone_number_fragment,container,false)
        (context as MainActivity).backListener = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChangeMyPhoneNumberViewModel::class.java)
        navController = findNavController()
        binding.model = viewModel
        myActivity.bottomNavigationGone()
        binding.ChangeMyPhoneDetailText.text= "현재 등록된 전화번호는 ${User.getInstance().phoneNumber.subSequence(0,3)}-${User.getInstance().phoneNumber.subSequence(3,7)}-${User.getInstance().phoneNumber.subSequence(7,11)}"
        activity?.let { viewModel.init(it) }
        binding.ChangeMyPhoneNumberback.setOnClickListener { onBackPressed() }
        binding.ChangeNewPhoneSubmit.setOnClickListener {
            if(isSend){
                CoroutineScope(Dispatchers.Main).launch {
                    activity?.getWindow()?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    binding.ChangeMyPhoneNumberLoading.show()

                    if(viewModel.checkCertificationMessage()){
                        User.getInstance().phoneNumber = viewModel.phoneNumber.value.toString()
                        User.getInstance().uid = FirebaseAuth.getInstance().uid.toString()
                        Toast.makeText(context , "핸드폰 번호 변경이 완료 되었습니다!",Toast.LENGTH_SHORT).show()
                        binding.ChangeMyPhoneNumberLoading.hide()
                        activity?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        onBackPressed()
                    }else{
                        Toast.makeText(context , "잘못된 인증 번호 입니다!",Toast.LENGTH_SHORT).show()
                        binding.ChangeMyPhoneNumberLoading.hide()
                        activity?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }
                }
            }else {
                CoroutineScope(Dispatchers.Main).launch {
                    if (viewModel.checkAlreadyPhone()) {
                        viewModel.sendMessage()
                        countDownTimer = createCountDownTimer(1200 * 100L)
                        countDownTimer?.start()
                        isSend = true
                        binding.ChangeNewPhoneEdit.isEnabled = false
                        binding.ChangeCertificationCodeEdit.isVisible = true
                    } else {
                        Toast.makeText(context, "이미 가입한 핸드폰 번호 입니다!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        viewModel.phoneNumber.observe(viewLifecycleOwner, Observer {
            setBackGroundChange()
        })
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
        binding.ChangeNewPhoneSubmit.text = "인증 확인 ${minute}분 ${second}초"
    }

    fun setBackGroundChange(){
        if(isSend){

        }else{
            if(viewModel.checkPhoneNumber()){

                binding.ChangeNewPhoneSubmit.setBackgroundResource(R.drawable.button_shape_main_color)
                binding.ChangeNewPhoneSubmit.setTextColor(Color.BLACK)
                binding.ChangeNewPhoneSubmit.isClickable = true
            }else{
                binding.ChangeNewPhoneSubmit.setBackgroundResource(R.drawable.shape_unable_radius_15)
                binding.ChangeNewPhoneSubmit.setTextColor(Color.WHITE)
                binding.ChangeNewPhoneSubmit.isClickable = false
            }
        }

    }

}