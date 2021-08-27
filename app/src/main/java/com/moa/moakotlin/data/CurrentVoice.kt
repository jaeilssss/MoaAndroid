package com.moa.moakotlin.data

class CurrentVoice {
    var boolean = false
    companion object {
        // 자기변수 선언하기
        @Volatile
        private var instance: CurrentVoice? = null

        fun getInstance(): CurrentVoice = instance ?: synchronized(this) {
            instance ?: CurrentVoice().also {
                instance = it
            }
        }
    }
}