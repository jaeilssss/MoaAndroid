package com.moa.moakotlin.repository

import com.algolia.search.saas.Client
import com.algolia.search.saas.CompletionHandler
import com.algolia.search.saas.Index
import com.algolia.search.saas.Query
import com.moa.moakotlin.R
import com.moa.moakotlin.data.Apt

class algoriaRepository {


    fun searchApt(str : String) : ArrayList<Apt>{
        var list  = ArrayList<Apt>()
        var client = Client("Y5P2EINUZX","b17fcc022749dc2ae86e492504aa70f5")

         var index = client.getIndex("Aparts")

        var jsonArray = index.searchSync(Query(str)).getJSONArray("hits")
        if(jsonArray!=null){
            for(i in 0 until jsonArray.length()) {
                var json = jsonArray?.getJSONObject(i)
                var apt = Apt(json.getString("address"),json.getString("aptCode"),
                json.getString("aptName"),json.getString("aroundApt") as ArrayList<String>,
                        json.getString("doroJuso"), json.getString("lat"),
                        json.getString("lon")
                )
                list.add(apt)
            }
        }

        return list
    }


}