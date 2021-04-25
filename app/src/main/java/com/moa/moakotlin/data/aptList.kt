package com.moa.moakotlin.data

class aptList {
     var aroundApt = ArrayList<String>()
    companion object {
        // 자기변수 선언하기
        @Volatile
        private var instance: aptList? = null

        fun getInstance(): aptList = instance ?: synchronized(this) {
            instance ?: aptList().also {
                instance = it
            }
        }

        fun setInstance(newInstance : aptList){
            getInstance().aroundApt = newInstance.aroundApt

        }
    }
}