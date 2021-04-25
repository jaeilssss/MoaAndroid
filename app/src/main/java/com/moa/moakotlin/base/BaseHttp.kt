package com.moa.moakotlin.base

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

abstract class BaseHttp {

 protected var TAG = "HttpSender"

 protected var Url = "http://ec2-52-78-157-207.ap-northeast-2.compute.amazonaws.com:8080/"

 protected var header = "application/json; charset=utf-8";

 protected lateinit var apiName: String

 lateinit var requestBody: RequestBody

  var client = OkHttpClient()

 protected lateinit var request : Request

 var jsonObject = JSONObject()


}

