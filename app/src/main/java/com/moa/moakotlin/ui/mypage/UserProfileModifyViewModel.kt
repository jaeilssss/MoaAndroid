package com.moa.moakotlin.ui.mypage

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Picture
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.mypage.MyPageRepository
import kotlinx.coroutines.*

class UserProfileModifyViewModel : ViewModel() {
 var introduction =MutableLiveData<String>(User.getInstance().nickName)
    var nickname =MutableLiveData<String>(User.getInstance().nickName)


}