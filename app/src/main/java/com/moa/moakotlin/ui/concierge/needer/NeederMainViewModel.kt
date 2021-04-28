package com.moa.moakotlin.ui.concierge.needer

import android.content.Context
import androidx.navigation.NavController
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.Needer
import com.moa.moakotlin.main
import com.moa.moakotlin.repository.concierge.NeederRepository

class NeederMainViewModel(navController: NavController, context: Context) : BaseViewModel(navController) {

    suspend fun getData(mainCategory : String) : ArrayList<Needer>{
            var repository = NeederRepository()

        return repository.initSetList(mainCategory)
    }
}