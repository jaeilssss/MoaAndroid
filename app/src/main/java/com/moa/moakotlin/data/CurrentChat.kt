package com.moa.moakotlin.data

class CurrentChat {

    var boolean = false
    companion object {
        // 자기변수 선언하기
        @Volatile
        private var instance: CurrentChat? = null

        fun getInstance(): CurrentChat = instance ?: synchronized(this) {
            instance ?: CurrentChat().also {
                instance = it
            }
        }
    }
}