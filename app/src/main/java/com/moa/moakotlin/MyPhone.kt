package com.moa.moakotlin

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager


class MyPhone {
    fun getBatteryRemain(context: Context): Int {
        val intentBattery: Intent? = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = intentBattery?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intentBattery?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = level?.div(scale?.toFloat()!!)
        if (batteryPct != null) {
            return (batteryPct * 100).toInt()
        }

        return  -1
    }

}