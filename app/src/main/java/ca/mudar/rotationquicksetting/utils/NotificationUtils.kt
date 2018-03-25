/*
 * Rotation Quick Setting
 * Add a Quick Settings tile to select portrait or landscape screen orientation.
 *
 * Copyright (C) 2017 Mudar Noufal <mn@mudar.ca>
 *
 * This file is part of RotationQS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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