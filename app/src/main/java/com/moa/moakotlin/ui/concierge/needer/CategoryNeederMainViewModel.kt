package com.moa.moakotlin.ui.concierge.needer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.concierge.NeederRepository
import com.moa.moakotlin.repository.user.UserRepository

class CategoryNeederMainViewModel : ViewModel() {

     var neederList =  MutableLiveData<ArrayList<Needer>>()

    var list = ArrayList<Needer>()
    // LiveData 적용해서 할것 getList 메소드 수정  해야
    suspend fun getList(mainCategory : String){
        var repository = NeederRepository()
        var data = repository.getList(mainCategory)
        list = data
        neederList.value = data
    }

    suspend fun getWriterData(uid :String) : User? {
        var repository = UserRepository()

        var writer = repository.getUserInfo(uid)

        return writer
    }


    suspend fun Scrolling(mainCategory: String, timeStamp : Timestamp) {

        var repository = NeederRepository()

        var data = repository.getNextData(mainCategory,timeStamp)

        list.addAll(data)

        neederList.value = list!!
    }
}