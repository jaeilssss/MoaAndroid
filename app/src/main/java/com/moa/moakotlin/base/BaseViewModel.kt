package com.moa.moakotlin.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

abstract class BaseViewModel(var navController: NavController) : ViewModel() {


    open fun prev(){
        navController.popBackStack()
    }


}