package com.moa.moakotlin.Http.signuphttp

import com.moa.moakotlin.base.BaseHttp
import com.moa.moakotlin.base.PostHttp
import com.moa.moakotlin.data.User
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class SignUpHttp(): BaseHttp(),PostHttp<User> {
    lateinit var responseBody: Response
    var result = "error"

    fun send(): String {


        requestBody = jsonObject.toString().toRequestBody(header.toMediaTypeOrNull())
        client.connectTimeoutMillis

        apiName = "api/user/signup3"

        request = Request.Builder().url(Url + apiName).post(requestBody).build()

            client.newCall(request).execute().use { response ->
                return if (response != null) {
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

    override fun setBody(data: User) {
        var jsonObject2 =JSONObject()
        jsonObject = JSONObject()
//        jsonObject2.put("name",data.name)
//        jsonObject2.put("gender",data.gender)
//        jsonObject2.put("birthday",data.birthDay)
        jsonObject2.put("phone_number",data.phoneNumber)
//        jsonObject2.put("nickName",data.nickname)
        jsonObject2.put("region_id",1)
        jsonObject.put("data",jsonObject2)
        println(jsonObject.toString())

    }

}