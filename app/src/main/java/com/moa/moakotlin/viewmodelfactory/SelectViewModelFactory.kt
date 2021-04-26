package com.moa.moakotlin.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.moa.moakotlin.ui.concierge.ConciergeMainViewModel


class SelectViewModelFactory(var navController: NavController) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(ConciergeMainViewModel::class.java)){
            ConciergeMainViewModel(navController) as T
        }
        else{
            throw IllegalArgumentException()
        }
    }
}