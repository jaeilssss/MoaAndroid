package com.moa.moakotlin.ui.concierge

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.rpc.Help
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.concierge.HelperRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class ConciergeMainViewModel(): ViewModel(){

    var HelperData = MutableLiveData<HashMap<String,ArrayList<Helper>>>()


    var nickname = "${User.getInstance().nickName}님"


    companion object{
        val mainHelperCategoryList = arrayListOf<String>("육아","기타","인테리어","반려동물케어","교육")
    }
//    suspend fun getHelperDataList() {
//
//        var repository = HelperRepository()
//
//        var map = HashMap<String,ArrayList<Helper>>()
//
//
//
//             repository.initSetList(map){
//
//                 HelperData.value = map
//             }
//
//        }




}