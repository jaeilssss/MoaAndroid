package com.moa.moakotlin.ui.mypage

import android.content.Intent
import android.graphics.Color
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
import com.moa.moakotlin.LoadingActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.costumdialog.AptCertificationImageAlertDialog
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.MyAccountSettingFragmentBinding

class MyAccountSettingFragment : Fragment() {

    companion object {
        fun newInstance() = MyAccountSettingFragment()
    }

    private lateinit var viewModel: MyAccountSettingViewModel

    private lateinit var binding : MyAccountSettingFragmentBinding

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.my_account_setting_fragment,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyAccountSettingViewModel::class.java)

        navController = findNavController()

        binding.model = viewModel

        binding.MyAccountPhoneNumberNewCheck.setOnClickListener { goToChangePhoneNumber()}

        binding.MyAccountLogout.setOnClickListener {
            context?.let {
                AptCertificationImageAlertDialog(it)
                    .setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("예"){
                        viewModel.logout()
                        var intent = Intent(context, LoadingActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }.show()
            }
        }
    }


    fun goToChangePhoneNumber(){

        context?.let {
            AptCertificationImageAlertDialog(it)
                .setMessage("전화번호 변경 시 채팅 내용이 다 지워집니다!" +
                        "변경하시겠습니까? ")
                .setPositiveButton("예"){
                    navController.navigate(R.id.action_myAccountSettingFragment_to_changeMyPhoneNumberFragment)
                }
                .setNegativeButton {

                }
                .show()
        }
    }


}