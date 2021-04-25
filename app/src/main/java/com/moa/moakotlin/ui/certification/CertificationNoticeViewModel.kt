package com.moa.moakotlin.ui.certification

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel

class CertificationNoticeViewModel(navController: NavController,context: Context) : BaseViewModel(navController){
    lateinit var bundle: Bundle
    fun skip(){

            navController.navigate(R.id.action_certificationNoticeFragment_to_certificationSkipFragment)
    }
}



