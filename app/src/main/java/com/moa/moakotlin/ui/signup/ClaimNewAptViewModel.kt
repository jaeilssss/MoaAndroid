package com.moa.moakotlin.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.moa.moakotlin.data.RequestApart
import com.moa.moakotlin.repository.apt.AptRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClaimNewAptViewModel : ViewModel() {

    var aptName = MutableLiveData<String>("")

    var aptAddress = MutableLiveData<String>("")

    var contact = MutableLiveData<String>("")

   suspend fun ClaimNewApt() : Boolean{
        var result = false
        var request = RequestApart(aptAddress.value!!,contact.value!!,aptName.value!!, Timestamp.now())

        val repository = AptRepository()

      if(repository.claimNewApt(request)){
          result = true
      }

        return result
    }


    fun check() : Boolean{
        if(aptName.value?.length!! >0 && aptAddress.value?.length!! > 0 && contact.value?.length!! > 0){
            return true
        }
        return false
    }
}