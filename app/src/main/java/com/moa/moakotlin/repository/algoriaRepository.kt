package com.moa.moakotlin.repository

import android.R.string
import com.algolia.search.saas.Client
import com.algolia.search.saas.Query
import com.google.common.base.Splitter
import com.google.common.collect.Lists
import com.moa.moakotlin.data.Apt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.*
import kotlin.collections.ArrayList


class algoriaRepository {


  suspend  fun searchApt(str: String) : ArrayList<Apt>{
        var list  = ArrayList<Apt>()
        var client = Client("Y5P2EINUZX", "b17fcc022749dc2ae86e492504aa70f5")

         var index = client.getIndex("Aparts")
        CoroutineScope(Dispatchers.Default).async {
            var jsonArray = index.searchSync(Query(str)).getJSONArray("hits")
            if(jsonArray!=null){
                for(i in 0 until jsonArray.length()) {
                    var json = jsonArray?.getJSONObject(i)
                    var aroundApt = json.getString("aroundApt")
                   aroundApt= aroundApt.replace("[", "")
                   aroundApt= aroundApt.replace("]", "")
                    aroundApt = aroundApt.replace("\"","")
                    val aroundAptList: ArrayList<String> = Lists.newArrayList(Splitter.on(",").split(aroundApt))
                    var apt = Apt(json.getString("address"), json.getString("aptCode"),
                            json.getString("aptName"), aroundAptList,
                            json.getString("doroJuso"), json.getDouble("lat"),
                            json.getDouble("lon")
                    )
                    list.add(apt)
                }
            }
        }.await()


        return list
    }




}