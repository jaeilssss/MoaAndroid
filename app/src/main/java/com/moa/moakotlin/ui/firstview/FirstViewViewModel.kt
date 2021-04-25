package com.moa.moakotlin.ui.firstview

import android.content.Context
import androidx.navigation.NavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel

class FirstViewViewModel(navController: NavController,context: Context) : BaseViewModel(navController){
    fun login(){
        navController.navigate(R.id.action_firstViewFragment_to_loginFragment)
    }
}