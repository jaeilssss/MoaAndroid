package com.moa.moakotlin.Http.kid

import com.moa.moakotlin.base.BaseHttp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class KidGetHttp() : BaseHttp() {

    fun send() : String{
        requestBody = jsonObject.toString().toRequestBody(header.toMediaTypeOrNull())
        apiName = "api/childBoard/1"
        println(Url+apiName)
        client.connectTimeoutMillis
        request = Request.Builder().url(Url+apiName).get().build()
        client.newCall(request).execute().use {
            response->return if(response!=null){
            var str = response.body?.string()
            var json = JSONObject(str)
            println(json.toString())


            // internal  server error 가 뜰 경우 밑에 처럼 하면 앱이 팅김

//            if(json.get("result_code").equals("OK")){
//                "ok"
//            }else{
//                "error"
//            }
//        } else {
//            "error"
            "ok"
        }else{
            "error"
        }
        }

    }

    }
