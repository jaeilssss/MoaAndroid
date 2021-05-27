package com.moa.moakotlin.repository.push

import android.app.NotificationChannel
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.moa.moakotlin.MainActivity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.moa.moakotlin.R


class FcmService() : FirebaseMessagingService() {

    override fun onMessageReceived(remotemessage: RemoteMessage) {

        if(remotemessage.notification !=null){  // 포그라운드늗
            println("포그라우ㅡㄴㄷ")
            remotemessage!!.notification!!.title?.let { remotemessage.notification!!.body?.let { it1 -> sendNotification(it1, it) } }

        }else if(remotemessage.data.size>0){ // 백그라운드
                println("백그라우")
            remotemessage.data.get("title")?.let { sendNotification(remotemessage.data.get("body")!!+"dd", "백그라오ㅜㄴ") }
        }
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

        val intent = Intent(baseContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val fullScreenPendingIntent = PendingIntent.getActivity(baseContext, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)    // 2

        val builder = NotificationCompat.Builder(this, channelId)

        builder.setSmallIcon(R.drawable.profile_human)
        builder.setContentTitle(title)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentText(content)
        builder.priority = NotificationCompat.PRIORITY_HIGH // 3

        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE)
        builder.setAutoCancel(true)
//            builder.setTimeoutAfter(100L)
//            builder.setFullScreenIntent(fullScreenPendingIntent, true) // 4
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1001, builder.build())
    }
}