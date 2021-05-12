package com.moa.moakotlin.ui.voice

interface AGEventHandler {
    fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int)

    fun onUserOffline(uid: Int, reason: Int)

    fun onExtraCallback(type: Int, vararg data: Any?)


}