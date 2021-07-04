package com.moa.moakotlin.ui.concierge

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.rpc.Help
import com.moa.moakotlin.base.BaseViewModel
import com.moa.moakotlin.data.Helper
import com.moa.moakotlin.repository.concierge.HelperRepository

class ConciergeMainViewModel(): ViewModel(){

    companion object{
        val mainHelperCategoryList = arrayListOf<String>("육아","기타")
    }
    suspend fun getHelperDataList() : HashMap<String,ArrayList<Helper>>{

        var repository = HelperRepository()

        var map = HashMap<String,ArrayList<Helper>>()

        for(str in mainHelperCategoryList){
            var list = repository.initSetList(str)

            map.put(str,list)
        }
        return map


    }
}