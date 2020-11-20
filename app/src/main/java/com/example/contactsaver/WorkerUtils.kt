package com.example.contactsaver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Message
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object WorkerUtils {
    const val CHANNEL_ID="VERBOSE_NOTIFICATION"
    const val CHANNEL_NAME="VERBOSE_NOTIFICATION_ON_CONTACT_OPERATION"
    fun makeNotification(context: Context, message: String){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
           val importance=NotificationManager.IMPORTANCE_HIGH
            val channel=NotificationChannel(CHANNEL_ID, CHANNEL_NAME,importance).apply {
                description="contact based operation"
            }
            val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
              notificationManager.createNotificationChannel(channel)
        }

        val builder=NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Contact")
            .setContentText(message)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setVibrate(LongArray(3))



        with(NotificationManagerCompat.from(context)){
            notify(1,builder.build())
        }


    }
}