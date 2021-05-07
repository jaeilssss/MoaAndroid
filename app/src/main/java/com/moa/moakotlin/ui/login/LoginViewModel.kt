package com.moa.moakotlin.ui .login

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.moa.moakotlin.R
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel(){
    var phoneNumber = ObservableField<String>("")


    fun login():Bundle{
            var user = User.getInstance()
            user.phoneNumber=phoneNumber.get().toString()
            var bundle = Bundle()
            bundle.putString("phoneNumber",phoneNumber.get())
//            navController.navigate(R.id.action_loginFragment_to_phoneCertification,bundle)
        return bundle
        }




    fun goToSignUp(){
    }

}