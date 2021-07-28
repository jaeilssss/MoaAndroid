package com.moa.moakotlin.repository.push

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.data.User


class FcmService() : FirebaseMessagingService() {

    override fun onMessageReceived(remotemessage: RemoteMessage) {
        val pm =
            getSystemService(Context.POWER_SERVICE) as PowerManager
        @SuppressLint("InvalidWakeLockTag") val wakeLock =
            pm.newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK
                        or PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG"
            )
        wakeLock.acquire(3000)
        wakeLock.release()
        val data = getSharedPreferences("AlarmSetting",Context.MODE_PRIVATE)

        if(data?.getBoolean("isEventAlarm",false) == true){
            remotemessage.data.get("title")?.let { sendNotification(remotemessage.data.get("body")!!, it) }
        }
//            remotemessage.data.get("title")?.let { sendNotification(remotemessage.data.get("body")!!, it) }

    }

    private fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean,
                                          name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name "
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

        }
    }

    private fun sendNotification(messageBody : String, messageTitle : String){
        createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_HIGH, false,
                getString(R.string.app_name), "App notification channel")   // 1

        val channelId = "$packageName-${getString(R.string.app_name)} "
        val title = messageTitle
        val content =messageBody

        if(title.equals("성공")){
            User.getInstance().certificationStatus = "인증"
        }
        val intent = Intent(baseContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK


        val builder = NotificationCompat.Builder(this, channelId)

        builder.setSmallIcon(R.drawable.moa_logo_vector)
        builder.setContentTitle(title)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentText(content)
        builder.priority = NotificationCompat.PRIORITY_HIGH // 3

        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE)
        builder.setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1001, builder.build())
    }
}
