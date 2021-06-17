package com.moa.moakotlin.ui.signup

import androidx.lifecycle.ViewModel
import com.moa.moakotlin.repository.apt.AptRepository

class MyNeighborhoodViewModel : ViewModel() {

    suspend fun getMyAroundNeighborhood(list : ArrayList<String>) : ArrayList<String>{
        var repository =AptRepository()
        var aptNameList = ArrayList<String>()
        for(i in 0 until list.size){
            var aptName = repository.getMyAroundNeighborhood(list.get(i))
            aptNameList.add(aptName)
        }
        return aptNameList
    }
}