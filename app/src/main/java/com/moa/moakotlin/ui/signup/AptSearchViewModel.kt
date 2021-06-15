package com.moa.moakotlin.ui.signup

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moa.moakotlin.data.Apt
import com.moa.moakotlin.data.aptList
import com.moa.moakotlin.repository.algoriaRepository

class AptSearchViewModel : ViewModel() {

    var AptList = MutableLiveData<ArrayList<Apt>>()

    var searchContent = MutableLiveData<String>()

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