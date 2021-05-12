package com.moa.moakotlin.vmodel

object ConstantApp {
    const val APP_BUILD_DATE = "today"
    const val BASE_VALUE_PERMISSION = 0X0001
    const val PERMISSION_REQ_ID_RECORD_AUDIO = BASE_VALUE_PERMISSION + 1
    const val PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = BASE_VALUE_PERMISSION + 3
    const val ACTION_KEY_CROLE = "C_Role"
    const val ACTION_KEY_ROOM_NAME = "ecHANEL"

    object PrefManager {
        const val PREF_PROPERTY_UID = "pOCXx_uid"
    }

    object AppError {
        const val NO_NETWORK_CONNECTION = 3
    }
}