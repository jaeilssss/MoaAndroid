package com.moa.moakotlin.ui.mypage

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.LoadingActivity
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.custom.AptCertificationImageAlertDialog
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.MyAccountSettingFragmentBinding

class MyAccountSettingFragment : BaseFragment() {

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
        (context as MainActivity).backListener = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyAccountSettingViewModel::class.java)

        navController = findNavController()
        initView()
        binding.model = viewModel
        myActivity.bottomNavigationGone()

        binding.MyAccountBack.setOnClickListener { navController.popBackStack() }

        binding.MyAccountPhoneNumberNewCheck.setOnClickListener { goToChangePhoneNumber()}

        binding.MyAccountMoaPolicy.setOnClickListener {
            var bundle = Bundle()
            var url = resources.getString(R.string.MoaOperationUrl)
            bundle.putString("url",url)
            bundle.putString("title","모두의 아파트 운영정책")
            navController.navigate(R.id.action_myAccountSettingFragment_to_moaOperationPolicy,bundle)
        }

        binding.MyAccountPrivatePolicy.setOnClickListener {
            var bundle = Bundle()
            var url = resources.getString(R.string.MoaPrivatePolicyUrl)

            bundle.putString("url",url)
            bundle.putString("title","개인정보 처리방침")
            navController.navigate(R.id.action_myAccountSettingFragment_to_moaOperationPolicy,bundle)
        }

        binding.MyAccountLogout.setOnClickListener {
            context?.let {
                AptCertificationImageAlertDialog(it)
                    .setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("예"){
                        viewModel.logout()
                        var intent = Intent(context, LoadingActivity::class.java)
                        startActivity(intent)
                        User.deleteUser()
                        activity?.finish()
                    }.setNegativeButton {  }
                        .show()
            }
        }

        binding.MyAccountSignOut.setOnClickListener { navController.navigate(R.id.action_myAccountSettingFragment_to_dropOutQuestionFragment) }
    }

    fun initView(){
        viewModel.phoneNumber.value = User.getInstance().phoneNumber

    }
    override fun onBackPressed() {
        navController.popBackStack()
    }


    fun goToChangePhoneNumber(){
        navController.navigate(R.id.action_myAccountSettingFragment_to_changeMyPhoneNumberFragment)
    }


}