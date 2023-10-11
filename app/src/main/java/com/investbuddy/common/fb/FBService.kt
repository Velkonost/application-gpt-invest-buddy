package com.investbuddy.common.fb

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.investbuddy.R
import com.investbuddy.features.activity.InitialActivity

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FBService : FirebaseMessagingService() {


    @SuppressLint("InlinedApi", "NotificationPermission")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        /**Creates an explicit intent for an Activity in your app */

        var resultIntent = Intent()
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        resultIntent = Intent(this, InitialActivity::class.java)

        val flags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            else -> PendingIntent.FLAG_UPDATE_CURRENT
        }

        val resultPendingIntent = PendingIntent.getActivity(
            this,
            0 /* Request code */, resultIntent,
            flags
        )
        val mBuilder = NotificationCompat.Builder(this, "Default channel")
        mBuilder.color = ContextCompat.getColor(this, android.R.color.transparent)
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
        mBuilder.setLargeIcon(
            BitmapFactory.decodeResource(
                this.resources,
                R.mipmap.ic_launcher
            )
        )

        mBuilder.setContentTitle(remoteMessage.notification?.title)
        mBuilder.setContentText(remoteMessage.notification?.body)
        mBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.notification?.body))
        mBuilder.setAutoCancel(true)
        mBuilder.setContentIntent(resultPendingIntent)

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel =
                NotificationChannel("Default channel", "(Notification)", importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = resources.getColor(android.R.color.transparent)
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(
                100,
                200,
                300,
                400,
                500,
                400,
                300,
                200,
                400
            )
            mBuilder.setChannelId("Default channel")
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = mBuilder.build();

        mNotificationManager.notify(0 /* Request Code */, notification)

        val notifications: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(applicationContext, notifications)
        r.play()

    }



}