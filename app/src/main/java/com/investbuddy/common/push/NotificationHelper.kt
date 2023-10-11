package com.investbuddy.common.push

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.investbuddy.R
import com.investbuddy.features.activity.InitialActivity

class NotificationHelper(private val mContext: Context) {
    private var mNotificationManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null

    /**
     * Create and push the notification
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    fun createNotification(
        title: String?,
        message: String?,
        link: String?,
        pageTitle: String?,
        filter: String?,
        section: String?
    ) {

        /**Creates an explicit intent for an Activity in your app */
        val resultIntent = Intent(mContext, InitialActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        resultIntent.putExtra("link", link)
        resultIntent.putExtra("title", title)
        resultIntent.putExtra("filter", filter)
        resultIntent.putExtra("section", section)

        val resultPendingIntent = PendingIntent.getActivity(
            mContext,
            2 /* Request code */, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
        )

        mBuilder = NotificationCompat.Builder(mContext)
//            .setSmallIcon(R.drawable.ic_affirmation_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(resultPendingIntent)

        mNotificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mBuilder?.setChannelId(mContext.getString(R.string.app_name))
        }
//        if (App.preferences.userId != 0)
//        mNotificationManager?.notify(2 /* Request Code */, mBuilder?.build())
    }
}