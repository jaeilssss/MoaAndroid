package com.moa.moakotlin.ui.concierge.helper

import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.data.User
import com.moa.moakotlin.repository.concierge.HelperRepository
import com.moa.moakotlin.repository.user.UserRepository

class CategoryMainViewModel : ViewModel() {
    // TODO: Implement the ViewModel


    suspend fun getList(mainCategory : String) :ArrayList<Helper>{
        var repository = HelperRepository()

        return repository.getList(mainCategory)
    }

    suspend fun getWriterData(uid :String) : User? {
        var repository = UserRepository()

        var writer = repository.getUserInfo(uid)

        return writer
    }
}