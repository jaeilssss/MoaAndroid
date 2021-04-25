package com.moa.moakotlin

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class testHttp(){


    fun send() :String{

        var client = OkHttpClient.Builder().build()

        var req = Request.Builder().url("https://www.naver.com").build()

        client.newCall(req).execute().use {
            response ->
            return if(response.body != null){
                    "성공 통신"
        }else{
                "실패"
            }
        }

    }
}