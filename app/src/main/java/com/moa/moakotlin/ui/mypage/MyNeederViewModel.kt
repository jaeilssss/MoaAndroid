package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.concierge.NeederRepository
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyNeederViewModel : ViewModel() {
    var myNeederDataList = MutableLiveData<ArrayList<Needer>>()

    fun initData(){
        var repository = NeederRepository()

        CoroutineScope(Dispatchers.Main).launch {
            var list = repository.getMyNeederData()

            myNeederDataList.value = list
        }
    }

    suspend fun getUserInfo(uid : String) : User? {
        var repository = UserRepository()

        return repository.getUserInfo(uid)


    }
}