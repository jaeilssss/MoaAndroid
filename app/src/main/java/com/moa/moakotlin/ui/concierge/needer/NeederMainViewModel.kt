package com.moa.moakotlin.ui.concierge.needer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.repository.concierge.NeederRepository

class NeederMainViewModel() : ViewModel() {

    suspend fun getData(mainCategory : String) : ArrayList<Needer>{
            var repository = NeederRepository()

        return repository.initSetList(mainCategory)
    }
}