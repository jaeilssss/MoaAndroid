package com.moa.moakotlin.ui.concierge.helper

import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.repository.concierge.NeederRepository

class HelperMainViewModel() : ViewModel() {

    suspend fun getData(mainCategory : String) : ArrayList<Needer>{
            var repository = NeederRepository()

        return repository.initSetList(mainCategory)
    }
}