package com.moa.moakotlin.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.moa.moakotlin.base.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.logging.Handler

class HomeViewModel() : ViewModel() {

    private lateinit var functions: FirebaseFunctions

    fun init(){

    }
    fun goToKid() {

    }

    fun goToShare() {

    }
}