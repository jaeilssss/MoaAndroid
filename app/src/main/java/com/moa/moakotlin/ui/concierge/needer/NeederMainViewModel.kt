package com.moa.moakotlin.ui.concierge.needer

import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.concierge.NeederRepository
import com.moa.moakotlin.repository.user.UserRepository

class NeederMainViewModel : ViewModel() {


    suspend fun getData(mainCategory : String) : ArrayList<Needer>{
        var repository = NeederRepository()

        return repository.initSetList(mainCategory)
    }

    suspend fun getWriterData(uid :String) : User? {
        var repository = UserRepository()

        var writer = repository.getUserInfo(uid)

        return writer
    }
}