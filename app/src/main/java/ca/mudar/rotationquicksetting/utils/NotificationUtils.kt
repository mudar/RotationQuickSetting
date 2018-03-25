package ca.mudar.rotationquicksetting.utils

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.media.AudioAttributes
import android.os.Build
import ca.mudar.rotationquicksetting.Const
import ca.mudar.rotationquicksetting.R

/**
 * Created by mudar on 25/03/18.
 */
object NotificationUtils {

    @TargetApi(Build.VERSION_CODES.O)
    fun createNotifyChannelIfNecessary(context: ContextWrapper) {
        if (Const.SUPPORTS_OREO) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val audioAttrs = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()

            val channel = NotificationChannel(Const.NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.notify_channel_name),
                    NotificationManager.IMPORTANCE_HIGH)
            channel.setBypassDnd(false)
            channel.enableVibration(true)
            channel.setSound(null, audioAttrs)
            channel.description = context.getString(R.string.notify_channel_description)

            notificationManager.createNotificationChannel(channel)
        }
    }
}