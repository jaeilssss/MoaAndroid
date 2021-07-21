package com.moa.moakotlin.ui.concierge

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User

class ConciergeWriteViewModel() : ViewModel(){
    var nickName = "${User.getInstance().nickName}님!"
}