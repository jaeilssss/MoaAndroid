package com.moa.moakotlin.ui.mypage

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseFragment
import com.moa.moakotlin.data.User
import com.moa.moakotlin.databinding.AlarmSettingFragmentBinding

class AlarmSettingFragment : BaseFragment() {

    companion object {
        fun newInstance() = AlarmSettingFragment()
    }

    private lateinit var viewModel: AlarmSettingViewModel

    private lateinit var binding : AlarmSettingFragmentBinding

    private lateinit var navController: NavController


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.alarm_setting_fragment,container,false)

        (context as MainActivity).backListener = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AlarmSettingViewModel::class.java)

        binding.model = viewModel

        binding.AlarmSettingBack.setOnClickListener { navController.popBackStack() }
        navController = findNavController()

    }

    override fun onStop() {

        viewModel.updateAlarmSetting()

        activity?.getSharedPreferences("AlarmSetting",Context.MODE_PRIVATE)!!
            .edit {
            putBoolean("isChattingAlarm",User.getInstance().isAgreeChattingAlarm)
                putBoolean("isEventAlarm",User.getInstance().isAgreeEventAlarm)
                putBoolean("isMarketingAlarm",User.getInstance().isAgreeMarketing)
                commit()
        }


        super.onStop()
    }

    override fun onBackPressed() {
        navController.popBackStack()
    }
}