package com.moa.moakotlin.ui.concierge.needer

import androidx.lifecycle.ViewModel
import com.moa.moakotlin.repository.concierge.NeederRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NeederReadViewModel : ViewModel() {


    suspend fun delete(mainCategory : String , documentId : String) : Boolean{

        var repository = NeederRepository()

        return repository.delete(mainCategory ,documentId)
    }
}