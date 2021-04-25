package com.moa.moakotlin.Http.login

import com.moa.moakotlin.base.BaseHttp
import com.moa.moakotlin.base.PostHttp
import com.moa.moakotlin.data.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class loginHttp():BaseHttp(),PostHttp<User>{
    override fun setBody(data: User) {
        var jsonobject2 = JSONObject()

        jsonobject2.put("phone_number",data.phoneNumber)
        jsonObject.put("data",jsonobject2)
    }

    fun send() : String{
            requestBody = jsonObject.toString().toRequestBody(header.toMediaTypeOrNull())
        apiName = "api/user/login2"
        client.connectTimeoutMillis
        request = Request.Builder().url(Url+apiName).post(requestBody).build()
        client.newCall(request).execute().use {
            response->return if(response!=null){
            var str = response.body?.string()
            var json = JSONObject(str)
            if(json.get("result_code").equals("OK")){
                "ok"
            }else{
                "error"
            }
        } else {
            "error"
        }
        }

        }





}