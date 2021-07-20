package com.moa.moakotlin.ui.home

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.moa.moakotlin.data.PushDTO
import com.squareup.okhttp.*



import okhttp3.Response
import java.io.IOException


class FcmPush() {
    val JSON = MediaType.parse("application/json; charset=utf-8")//Post전송 JSON Type
    val url = "https://fcm.googleapis.com/fcm/send" //FCM HTTP를 호출하는 URL
    val serverKey =
        "AAAAVJhLpJ4:APA91bEHqK395lihIJpI9RV40Oqsx5ZPx5l9oi__hwX_Ydvq2DfiDOjgWYmPQOegcNZYXR_Jnjg8GcT0-Lt1undl5T3XlwD1edqOeKHkYcBI2I5JVcZ92NijnQWXss_eOaDuAGsxcge6"

    //Firebase에서 복사한 서버키
    var okHttpClient: OkHttpClient
    var gson: Gson

    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
    }

    fun sendMessage(destinationUid: String, title: String, message: String) {
        FirebaseFirestore.getInstance().collection("pushtokens").document(destinationUid)
            .get()//destinationUid의 값으로 푸시를 보낼 토큰값을 가져오는 코드
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var token = task.result!!["pushtoken"].toString()
                    Log.i("토큰정보", token)
                    var pushDTO = PushDTO()
                    pushDTO.to = token                   //푸시토큰 세팅
                    pushDTO.notification?.title = title  //푸시 타이틀 세팅
                    pushDTO.notification?.body = message //푸시 메시지 세팅

                    var body = RequestBody.create(JSON, gson?.toJson(pushDTO)!!)
                    var request = Request
                        .Builder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "key=" + serverKey)
                        .url(url) //푸시 URL 세팅
                        .post(body) //pushDTO가 담긴 body 세팅
                        .build()
                    okHttpClient?.newCall(request)?.enqueue(object : Callback {//푸시 전송

                        override fun onFailure(request: Request?, e: IOException?) {
                            TODO("Not yet implemented")
                        }

                        override fun onResponse(response: com.squareup.okhttp.Response?) {
                            println(response?.body().toString())
                        }
                    })
                }
            }
    }
}