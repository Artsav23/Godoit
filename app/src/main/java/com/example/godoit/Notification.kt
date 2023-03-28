package com.example.godoit

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

const val  notificationID = 111
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification : BroadcastReceiver() {

    override fun onReceive(applicationContext: Context, intent: Intent) {
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelID,)
            .setSmallIcon(R.drawable.ic_time)
            .setContentText(intent.getStringExtra(messageExtra))
            .setContentTitle(intent.getStringExtra(titleExtra))
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationID, notificationBuilder.build())
    }
}