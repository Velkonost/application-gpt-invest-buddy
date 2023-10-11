package com.investbuddy.common.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {

//    @Inject
//    lateinit var repo: FirebaseRepo

    private val notificationHelper: NotificationHelper by lazy {
        NotificationHelper(
            applicationContext
        )
    }

    override fun onCreate() {
        super.onCreate()
//        AndroidInjection.inject(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var pageTitle: String? = null
        var filter: String? = null
        var section: String? = null

        remoteMessage.data.let {
            pageTitle = it["pageTitle"]
            filter = it["filter"]
            section = it["section"]
        }

        remoteMessage.notification?.let {
            notificationHelper.createNotification(
                it.title,
                it.body,
                it.clickAction,
                pageTitle,
                filter,
                section
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}