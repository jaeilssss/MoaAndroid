package com.moa.moakotlin.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.moa.moakotlin.ui.sitter.*

class SitterViewModelFactory(var navController: NavController) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(SitterWriteViewModel::class.java)){
            SitterWriteViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(SitterMainViewModel::class.java)){
            SitterMainViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(SitterReadViewModel::class.java)){
            SitterReadViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(SitterModifyViewModel::class.java)){
            SitterModifyViewModel(navController) as T
        }
        else{
            throw IllegalArgumentException()
        }
    }
}