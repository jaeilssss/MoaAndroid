package com.moa.moakotlin.ui.signup

import android.content.Context
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User

class AptInfoViewModel(navController: NavController,context: Context) : BaseViewModel(navController){
    lateinit var bundle: Bundle

    val aptName = ObservableField<String>("")
    fun next(){
                User.getInstance().aptCode = aptName.get() as String
            navController.navigate(R.id.action_aptInfoFragment_to_certificationNoticeFragment)
    }

}