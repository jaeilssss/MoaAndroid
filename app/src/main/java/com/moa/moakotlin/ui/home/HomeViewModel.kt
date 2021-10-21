package com.moa.moakotlin.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.*
import com.moa.moakotlin.repository.banner.BannerRepository
import com.moa.moakotlin.repository.partnerapart.PartnerApartRepository
import com.moa.moakotlin.repository.push.FcmRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.logging.Handler

class HomeViewModel() : ViewModel() {

    private lateinit var functions: FirebaseFunctions

      var magazineList = MutableLiveData<ArrayList<Magazine>>()

    var homeBannerList = MutableLiveData<ArrayList<Banner>>()


   suspend fun getMoaMagazine(){
        var repository = BannerRepository()

       var list= repository.getMoaMagazine()

       magazineList.value = list

    }

    suspend fun getHomeBanner(){
        var repository = BannerRepository()

        var list = repository.getHomeBanner()

        homeBannerList.value = list
    }

    suspend fun findPartnerApart() : PartnerApart?{


        var repository = PartnerApartRepository()

        return repository.findPartnerApart()


    }

}