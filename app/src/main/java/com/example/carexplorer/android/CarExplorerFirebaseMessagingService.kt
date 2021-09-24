package com.example.carexplorer.android

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class CarExplorerFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Timber.d("$token")
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val titleMessage = message.notification?.title
        val textMessage = message.notification?.body
        super.onMessageReceived(message)
    }
}