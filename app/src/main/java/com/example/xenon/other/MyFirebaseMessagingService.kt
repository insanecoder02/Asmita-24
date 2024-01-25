package com.example.xenon.other

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.xenon.Activity.Main
import com.example.xenon.R
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val chaId = "notification_channel"
const val chaName = "com.example.xenon"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val pageId = message.data["pageId"]!!
        if (message.notification != null) {
            val title = message.notification!!.title!!
            val body = message.notification!!.body!!

            FirebaseApp.initializeApp(this)

            saveFCMMessageToFirestore(title, body)
            // Generate and display the notification
            generateNotification(title, body, pageId)
        }
    }


    fun getRemoteView(tit: String, msg: String): RemoteViews {
        val remoteViews = RemoteViews(chaName, R.layout.layout_notification)

        remoteViews.setTextViewText(R.id.title, tit)
        remoteViews.setTextViewText(R.id.description, msg)
        remoteViews.setImageViewResource(R.id.appLogo, R.drawable.group)

        return remoteViews
    }

    private fun saveFCMMessageToFirestore(title: String, body: String) {
        val firestore = FirebaseFirestore.getInstance()
        val notificationsCollection = firestore.collection("Notification")

        val notificationData = hashMapOf(
            "title" to title,
            "body" to body,
            "time" to System.currentTimeMillis() // Optional: Include a timestamp
        )

        notificationsCollection.add(notificationData)
            .addOnSuccessListener {
                Log.d("Firestore", "FCM message added to Firestore")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding FCM message to Firestore", e)
            }
    }

    fun generateNotification(tit: String, msg: String, pageId: String) {
        val intent = createIntentForPage(pageId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(intent)

        val pendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//        )

        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, chaId)
                .setSmallIcon(R.drawable.group)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(2000, 1000, 2000, 1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(tit)
                .setContentText(msg)

        builder = builder.setContent(getRemoteView(tit, msg))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notiChannel = NotificationChannel(chaId, chaName, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notiChannel)

        notificationManager.notify(0, builder.build())
    }

    private fun createIntentForPage(pageId: String): Intent {
        return when (pageId) {
            "dev" -> {
                load("dev")
            }

            "gallery" -> {
                load("gal")
            }

            "team" -> {
                load("team")
            }

            "about" -> {
                load("abt")
            }

            else -> Intent(this, Main::class.java)
        }
    }

    private fun load(value: String): Intent {
        val intent = Intent(this, Main::class.java)
        intent.putExtra("open", value)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return intent
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("hello", "Refreshed token: $token")
    }
}
