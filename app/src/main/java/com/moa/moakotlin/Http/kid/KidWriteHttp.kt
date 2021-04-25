package com.moa.moakotlin.Http.kid

import com.moa.moakotlin.base.BaseHttp
import com.moa.moakotlin.base.PostHttp
import com.moa.moakotlin.data.Chat

class KidWriteHttp() : BaseHttp(),PostHttp<Chat>{
    override fun setBody(data: Chat) {
        TODO("Not yet implemented")
    }

}