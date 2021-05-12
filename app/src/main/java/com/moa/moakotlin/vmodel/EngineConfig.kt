package com.moa.moakotlin.vmodel

class EngineConfig(){
    var mClientRole = 0
    var mUid = 0
    var mChannel: String? = null
    fun reset() {
        mChannel = null
    }
}