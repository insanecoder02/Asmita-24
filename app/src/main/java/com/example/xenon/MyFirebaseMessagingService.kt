package com.example.xenon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.xenon.Activity.Main
import com.example.xenon.databinding.FragmentDeveloperBinding
import com.example.xenon.databinding.FragmentLiveScoreBinding
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val chaId = "notification_channel"
const val chaName = "com.example.xenon"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val pageId = message.data["pageId"] ?: "Main"
        if (message.notification != null) {
            generateNotification(
                message.notification!!.title!!,
                message.notification!!.body!!,
                pageId
            )
        }
    }

    fun getRemoteView(tit: String): RemoteViews {
        val remoteViews = RemoteViews(chaName, R.layout.notification_layout)

        remoteViews.setTextViewText(R.id.title, tit)
//        remoteViews.setTextViewText(R.id.description, msg)
        remoteViews.setImageViewResource(R.id.appLogo, R.drawable.group)

        return remoteViews
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("hello", "Refreshed token: $token")
    }

    fun generateNotification(tit: String, msg: String, pageId: String) {
        val intent = createIntentForPage(pageId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, chaId)
                .setSmallIcon(R.drawable.group)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(2000, 1000, 2000, 1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)

//        builder = builder.setContent(getRemoteView(tit, msg))
        builder = builder.setContent(getRemoteView(tit))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notiChannel = NotificationChannel(chaId, chaName, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notiChannel)

        notificationManager.notify(0, builder.build())
    }

    private fun createIntentForPage(pageId: String): Intent {
        return when (pageId) {
            "dev" -> Intent(this, FragmentDeveloperBinding::class.java)
            "score" -> Intent(this, FragmentLiveScoreBinding::class.java)

            else -> Intent(this, Main::class.java)
        }
    }
}