package com.moa.moakotlin.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.rpc.Help
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyHelperViewModel : ViewModel() {
    var myHelperDataList = MutableLiveData<ArrayList<Helper>>()

    fun initData(){
        var repository = HelperRepository()

        CoroutineScope(Dispatchers.Main).launch {
            var list = repository.getMyHelperData()

            myHelperDataList.value = list
        }
    }

    suspend fun getUserInfo(uid : String) : User? {
        var repository = UserRepository()

        return repository.getUserInfo(uid)


    }
}