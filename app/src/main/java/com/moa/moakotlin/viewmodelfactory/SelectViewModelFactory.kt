package com.moa.moakotlin.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.moa.moakotlin.ui.select.KidSitterViewSelectViewModel


class SelectViewModelFactory(var navController: NavController) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(KidSitterViewSelectViewModel::class.java)){
            KidSitterViewSelectViewModel(navController) as T
        }
        else{
            throw IllegalArgumentException()
        }
    }
}