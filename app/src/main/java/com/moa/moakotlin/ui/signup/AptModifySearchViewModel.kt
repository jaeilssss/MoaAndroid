package com.moa.moakotlin.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Apt
import com.moa.moakotlin.repository.algoriaRepository

class AptModifySearchViewModel : ViewModel() {
    var searchContent = MutableLiveData<String>("")

    var AptList = MutableLiveData<ArrayList<Apt>>()


    suspend fun updateSearchView(){
        var algoriaRepository = algoriaRepository()
        searchContent.let {
            if(searchContent.value?.isNotEmpty() == true){
                var list = algoriaRepository.searchApt(searchContent.value!!)
                AptList.value = list
            }
        }
    }

}