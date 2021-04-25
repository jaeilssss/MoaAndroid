package com.moa.moakotlin.ui.signup

import android.content.Context
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel

class SignUpPasswordViewModel(navController: NavController , context: Context) : BaseViewModel(navController){

    var password  = ObservableField<String>("")
    var repassword = ObservableField<String>("")
    lateinit var bundle: Bundle
    fun next(){
        if(password.get().equals(repassword.get())){
            bundle.putString("password",password.get())
//            navController.navigate(R.id.action_signUpPasswordFragment_to_aptInfoFragment,bundle)
        }
    }
}