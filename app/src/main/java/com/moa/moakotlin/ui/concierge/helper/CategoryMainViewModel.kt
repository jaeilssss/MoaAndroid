package com.moa.moakotlin.ui.concierge.helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.moa.moakotlin.data.Banner
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.banner.BannerRepository
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.concierge.NeederRepository
import com.moa.moakotlin.repository.user.UserRepository

class CategoryMainViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var neederList =  MutableLiveData<ArrayList<Helper>>()

    var list = ArrayList<Helper>()
    var newDatasize =0
    var lastPosition = 0
    suspend fun getList(mainCategory : String) :ArrayList<Helper>{
        list.clear()
        var repository = HelperRepository()

        return repository.getList(mainCategory)
    }

    suspend fun getWriterData(uid :String) : User? {
        var repository = UserRepository()

        var writer = repository.getUserInfo(uid)

        return writer
    }

    suspend fun Scrolling(mainCategory: String, timeStamp : Timestamp) {

        var repository = HelperRepository()

        var data = repository.getNextData(mainCategory,timeStamp)
        newDatasize = data.size
        list.addAll(data)

        println("lise.size - >${list.size}")
        neederList.value = list!!
    }


}