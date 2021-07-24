package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moa.moakotlin.R
import com.moa.moakotlin.databinding.AlarmSettingFragmentBinding

class AlarmSettingFragment : Fragment() {

    companion object {
        fun newInstance() = AlarmSettingFragment()
    }

    private lateinit var viewModel: AlarmSettingViewModel

    private lateinit var binding : AlarmSettingFragmentBinding

    private lateinit var navController: NavController


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.alarm_setting_fragment,container,false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AlarmSettingViewModel::class.java)

        binding.model = viewModel

        navController = findNavController()


    }

    override fun onStop() {

        viewModel.updateAlarmSetting()
        super.onStop()
    }
}