package com.moa.moakotlin.data

class aptList {
     var aroundApt = ArrayList<String>()
    var address = ""
    var city =""
    var gu = ""
    var lat = 0.0
    var lon =0.0
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
            getInstance().address = newInstance.address
            getInstance().city = newInstance.city
            getInstance().gu = newInstance.gu
            getInstance().lat = newInstance.lat
            getInstance().lon = newInstance.lon
        }
    }
}