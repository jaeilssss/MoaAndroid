package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.User

class UserProfileViewModel : ViewModel() {


    var nickName = MutableLiveData<String>(User.getInstance().nickName)
    var aptName = MutableLiveData<String>(User.getInstance().aptName)
    var introduction = MutableLiveData<String>(User.getInstance().introduction)


}