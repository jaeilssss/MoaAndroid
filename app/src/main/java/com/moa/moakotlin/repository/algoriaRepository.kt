package com.moa.moakotlin.repository

import com.algolia.search.saas.Client
import com.algolia.search.saas.Index
import com.moa.moakotlin.R

class algoriaRepository {

    fun searchApt() : Index{

        var client = Client("Y5P2EINUZX","b17fcc022749dc2ae86e492504aa70f5")

         var index = client.getIndex("Aparts")

        return index
    }


}