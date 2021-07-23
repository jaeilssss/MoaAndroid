package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.User

class MyAccountSettingViewModel : ViewModel() {
    var phoneNumber = MutableLiveData<String>(User.getInstance().phoneNumber)

}