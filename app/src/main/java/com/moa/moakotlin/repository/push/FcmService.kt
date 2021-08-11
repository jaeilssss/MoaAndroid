package com.moa.moakotlin.repository.push

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.moa.moakotlin.LoadingActivity
import com.moa.moakotlin.MainActivity
import com.moa.moakotlin.R
import com.moa.moakotlin.data.CurrentChat
import com.moa.moakotlin.data.User


class FcmService() : FirebaseMessagingService() {



    override fun onMessageReceived(remotemessage: RemoteMessage) {
        println("메소드 올라옴.1.")
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
        if(remotemessage.notification!=null){
            println("포그라운드????...")
            if(remotemessage!!.notification?.title.equals("아파트 인증")){
                if( remotemessage!!.notification?.body!!.contains("앱을 재실행 해주세요")){
                    sendNotificationForGround(remotemessage.notification?.body!!,remotemessage.notification?.title!!)
                    User.getInstance().certificationStatus = "인증"
                }else if(remotemessage!!.notification?.body!!.contains("인증이 반려되었습니다")){
                    sendNotificationForGround(remotemessage.notification?.body!!,remotemessage.notification?.title!!)
                    User.getInstance().certificationStatus = "반려"
                }
                else{
                    sendNotificationForGround(remotemessage.notification?.body!!,remotemessage.notification?.title!!)
                }
            }
            else if(data?.getBoolean("isMarketingAlarm",false) ==true){
                sendNotificationForGround(remotemessage.notification?.body!!,remotemessage.notification?.title!!)
            }
        }else if(remotemessage.data.isNotEmpty()){
             if(data?.getBoolean("isEventAlarm",false) == true && remotemessage.data.get("title").equals("리뷰")){
                remotemessage.data.get("title")?.let { sendNotificationBackGround(remotemessage.data.get("body")!!, it)
                }
            }else if(data?.getBoolean("isChattingAlarm",false)==true && !CurrentChat.getInstance().boolean){
                remotemessage.data.get("title")?.let { sendNotificationBackGround(remotemessage.data.get("body")!!, it) }
            }else if(data?.getBoolean("isMarketingAlarm",false) ==true && remotemessage.data.get("title").equals("이벤트")){
                remotemessage.data.get("title")?.let { sendNotificationBackGround(remotemessage.data.get("body")!!, it) }
            }
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


    private fun sendNotificationBackGround(messageBody : String, messageTitle : String){
        println(">?>??")
        createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_HIGH, false,
                getString(R.string.app_name), "App notification channel")   // 1

        val channelId = "$packageName-${getString(R.string.app_name)} "
        val title = messageTitle
        val content =messageBody
//
//       if(messageBody.contains("앱을 재실행 해주세요")){
//           User.getInstance().certificationStatus = "인증"
//           println("ddd-> ${User.getInstance().certificationStatus}")
//       }



        val builder = NotificationCompat.Builder(this, channelId)

        builder.setSmallIcon(R.mipmap.ic_launcher)
        if(title!=null){
            builder.setContentTitle(title)
        }
        val intent = Intent(applicationContext,LoadingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        if(title.equals("리뷰") || title.equals("아파트 인증")){
            intent.putExtra("request","알림")
        }else {
            intent.putExtra("request","채팅")
        }
        val pendingIntent : PendingIntent = PendingIntent.getActivity(applicationContext,0, intent ,PendingIntent.FLAG_CANCEL_CURRENT)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentText(content)

        builder.priority = NotificationCompat.PRIORITY_HIGH // 3

        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE)
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1001, builder.build())
    }

    private fun sendNotificationForGround(messageBody : String, messageTitle : String){
        println(">?>??")
        createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_HIGH, false,
                getString(R.string.app_name), "App notification channel")   // 1

        val channelId = "$packageName-${getString(R.string.app_name)} "
        val title = messageTitle
        val content =messageBody
//
//       if(messageBody.contains("앱을 재실행 해주세요")){
//           User.getInstance().certificationStatus = "인증"
//           println("ddd-> ${User.getInstance().certificationStatus}")
//       }



        val builder = NotificationCompat.Builder(this, channelId)

        builder.setSmallIcon(R.mipmap.ic_launcher)
        if(title!=null){
            builder.setContentTitle(title)
        }
        val intent = Intent(applicationContext,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
        }


        if(title.equals("리뷰") || title.equals("아파트 인증")){
            intent.putExtra("request","알림")
        }else {
            intent.putExtra("request","채팅")
        }

        val pendingIntent : PendingIntent = PendingIntent.getActivity(baseContext,0, intent ,PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentText(content)

        builder.priority = NotificationCompat.PRIORITY_HIGH // 3
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE)
//        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1001, builder.build())
    }
}
