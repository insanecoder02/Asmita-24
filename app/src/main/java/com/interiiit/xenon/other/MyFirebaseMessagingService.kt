package com.interiiit.xenon.other

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.interiiit.xenon.Activity.Main
import com.interiiit.xenon.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val chaId = "notification_channel"
const val chaName = "com.interiiit.xenon"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
//            val title = message.notification!!.title?:""
            val body = message.notification!!.body!!
            generateNotification(body)
        }
    }

    fun getRemoteView(msg: String): RemoteViews {
        val remoteViews = RemoteViews(packageName, if (isDarkMode()) R.layout.custom_noti_lay else R.layout.custom_noti_dark)

        remoteViews.setTextViewText(R.id.description, msg)
        return remoteViews
    }

    fun generateNotification(msg: String) {
        val intent = Intent(this, Main::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(intent)

        val pendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, chaId)
                .setSmallIcon(R.drawable.group)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(2000, 1000, 2000, 1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setContentText(msg)
                .setStyle(NotificationCompat.BigTextStyle().bigText(msg))

        builder = builder.setContent(getRemoteView(msg))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notiChannel = NotificationChannel(chaId, chaName, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notiChannel)

        if (isAppInForeground()) {
            builder.setContent(getRemoteView(msg))
        }

        notificationManager.notify(0, builder.build())
    }
    private fun isDarkMode(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun isAppInForeground(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val packageName = applicationContext.packageName
        val appProcesses = activityManager.runningAppProcesses ?: return false

        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                && appProcess.processName == packageName
            ) {
                return true
            }
        }
        return false
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("hello", "Refreshed token: $token")
    }


//    private fun createIntentForPage(pageId: String): Intent {
//        return when (pageId) {
//            "dev" -> {
//                load("dev")
//            }
//
//            "gallery" -> {
//                load("gal")
//            }
//
//            "team" -> {
//                load("team")
//            }
//
//            "about" -> {
//                load("abt")
//            }
//
//            else -> Intent(this, Main::class.java)
//        }
//    }

//    private fun load(value: String): Intent {
//        val intent = Intent(this, Main::class.java)
//        intent.putExtra("open", value)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        return intent
//    }
}
