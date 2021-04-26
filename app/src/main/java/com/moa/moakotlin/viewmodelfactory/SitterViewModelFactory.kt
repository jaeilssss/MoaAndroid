package com.moa.moakotlin.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.moa.moakotlin.ui.concierge.helper.*

class SitterViewModelFactory(var navController: NavController) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HelperWriteViewModel::class.java)){
            HelperWriteViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(HelperMainViewModel::class.java)){
            HelperMainViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(HelperReadViewModel::class.java)){
            HelperReadViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(HelperModifyViewModel::class.java)){
            HelperModifyViewModel(navController) as T
        }
        else{
            throw IllegalArgumentException()
        }
    }
}