package com.moa.moakotlin.ui.signup

import android.content.Context
import android.text.BoringLayout
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel

class SignUpViewModel (navController: NavController,context: Context) :BaseViewModel(navController){
    private var observableField = arrayOfNulls<ObservableField<String>>(20)

    fun next(){
            navController.navigate(R.id.action_signUpFragment_to_signUpInfoFragment)
    }
}