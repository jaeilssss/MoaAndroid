package com.moa.moakotlin.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.moa.moakotlin.ui.declaration.DeclarationViewModel

class DeclarationViewFactory(var navController: NavController) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(DeclarationViewModel::class.java)){
            DeclarationViewModel(navController) as T
        }else{
            throw IllegalArgumentException()
        }
    }
}