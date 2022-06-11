package com.example.onigiri_restaurant_recommendation.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.onigiri_restaurant_recommendation.R
import com.example.onigiri_restaurant_recommendation.data.remote.network.Firebase
import com.example.onigiri_restaurant_recommendation.model.TokenFcm
import com.example.onigiri_restaurant_recommendation.ui.main.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService: FirebaseMessagingService() {

    companion object {
        private val TAG = MessagingService::class.java.simpleName
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_CHANNEL_ID = "Firebase Channel"
        private const val NOTIFICATION_CHANNEL_NAME = "Firebase Notification"
    }

    private var token = ""

    fun getToken(): String {
        Firebase.firebaseMessaging().token.addOnCompleteListener(OnCompleteListener {
            if(!it.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", it.exception)
                return@OnCompleteListener
            }
            val token = it.result
            this.token = token
        })
        return this.token
    }

    override fun onNewToken(token: String) {
        this.token = token

        Firebase.firestoreInstance()
            .collection("token_fcm")
            .whereEqualTo("user_id", Firebase.currentUser().uid)
            .get()
            .addOnSuccessListener { documents ->
                Firebase.firestoreInstance()
                    .collection("token_fcm")
                    .document(Firebase.currentUser().uid)
                    .update("token", token)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            }
            .addOnFailureListener { exception ->
                Firebase.firestoreInstance()
                    .collection("token_fcm")
                    .document(Firebase.currentUser().uid)
                    .set(TokenFcm(token, Firebase.currentUser().uid))
                    .addOnSuccessListener {
                        Log.d(TAG, "Saved token $token")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error", e)
                    }
            }

        Log.d(TAG, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "Message data payload: " + remoteMessage.data)
        Log.d(TAG, "Message Notification Body: ${remoteMessage.notification?.body}")
        sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val contentIntent = Intent(applicationContext, MainActivity::class.java)

        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(applicationContext,
            NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}