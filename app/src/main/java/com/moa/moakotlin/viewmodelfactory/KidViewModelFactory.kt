package com.moa.moakotlin.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.moa.moakotlin.ui.kid.*
import com.moa.moakotlin.ui.select.KidSitterViewSelectViewModel

class KidViewModelFactory(var navController: NavController) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(KidWritePageViewModel::class.java)){
           KidWritePageViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(KidReadViewModel::class.java)){
            KidReadViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(KidModifyViewModel::class.java)){
            KidModifyViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(KidHireSuccessViewModel::class.java)){
            KidHireSuccessViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(KidReviewWriteViewModel :: class.java)){
            KidReviewWriteViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(KidSitterViewSelectViewModel::class.java)){
            KidSitterViewSelectViewModel(navController) as T
        }
        else{
            throw IllegalArgumentException()
        }
    }

}