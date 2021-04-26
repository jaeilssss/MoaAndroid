package com.moa.moakotlin.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.moa.moakotlin.ui.concierge.needer.*
import com.moa.moakotlin.ui.concierge.ConciergeMainViewModel

class KidViewModelFactory(var navController: NavController) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(NeederWritePageViewModel::class.java)){
           NeederWritePageViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(NeederReadViewModel::class.java)){
            NeederReadViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(NeederModifyViewModel::class.java)){
            NeederModifyViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(NeederHireSuccessViewModel::class.java)){
            NeederHireSuccessViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(NeederReviewWriteViewModel :: class.java)){
            NeederReviewWriteViewModel(navController) as T
        }else if(modelClass.isAssignableFrom(ConciergeMainViewModel::class.java)){
            ConciergeMainViewModel(navController) as T
        }
        else{
            throw IllegalArgumentException()
        }
    }

}