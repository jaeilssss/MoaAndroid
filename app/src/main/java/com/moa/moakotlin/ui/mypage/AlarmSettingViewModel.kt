package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.user.UserRepository

class AlarmSettingViewModel : ViewModel() {
    var isAgreeChattingAlarm = MutableLiveData<Boolean>(User.getInstance().isAgreeChattingAlarm)
    var isAgreeEventAlarm = MutableLiveData<Boolean>(User.getInstance().isAgreeEventAlarm)
    var isAgreeMarketingAlarm = MutableLiveData<Boolean>(User.getInstance().isAgreeMarketing)




    fun updateAlarmSetting(){
        User.getInstance().isAgreeMarketing = isAgreeChattingAlarm.value!!
        User.getInstance().isAgreeEventAlarm = isAgreeEventAlarm.value!!
        User.getInstance().isAgreeMarketing = isAgreeMarketingAlarm.value!!

        var repository = UserRepository()

        repository.updateAlarmSetting()
    }

}