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

package ca.mudar.rotationquicksetting.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Icon
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.support.v4.app.NotificationCompat
import android.view.WindowManager
import ca.mudar.rotationquicksetting.Const
import ca.mudar.rotationquicksetting.R
import ca.mudar.rotationquicksetting.data.UserPrefs
import ca.mudar.rotationquicksetting.utils.NotificationUtils
import ca.mudar.rotationquicksetting.utils.OrientationUtils
import ca.mudar.rotationquicksetting.utils.PermissionUtils

class QuickSettingsService : TileService() {
    private val TAG = "QuickSettingsService"

    override fun onCreate() {
        super.onCreate()

        UserPrefs(applicationContext).setHelpCompleted()
    }

    override fun onStartListening() {
        super.onStartListening()

        try {
            updateQuickSettingsTile(getCurrentOrientation(
                    UserPrefs(applicationContext).hasAutoRotate()),
                    Settings.System.canWrite(applicationContext))
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onClick() {
        if (Settings.System.canWrite(applicationContext)) {
            try {
                val newOrientation = toggleOrientation()
                updateQuickSettingsTile(newOrientation, true)
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
            }
        } else {
            showPermissionNotification()
        }
    }

    private fun updateQuickSettingsTile(orientation: Int, canWriteSettings: Boolean) {
        qsTile.state = when (canWriteSettings) {
            true -> Tile.STATE_ACTIVE
            else -> Tile.STATE_INACTIVE
        }

        when {
            OrientationUtils.isLandscape(orientation) -> {
                qsTile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_screen_landscape)
                qsTile.label = getString(R.string.tile_label_land)
            }
            OrientationUtils.isPortrait(orientation) -> {
                qsTile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_screen_portrait)
                qsTile.label = getString(R.string.tile_label_port)
            }
            else -> {
                qsTile.icon = Icon.createWithResource(applicationContext, R.drawable.ic_screen_rotation)
                qsTile.label = getString(R.string.tile_label_auto_rotate)
            }
        }

        qsTile.updateTile()
    }

    /**
     * Toggles current location, and returns the new value.
     * Requires a prior check of Settings.System.canWrite()
     *
     * @return new rotation
     */
    private fun toggleOrientation(): Int {
        val userPrefs = UserPrefs(applicationContext)

        return if (userPrefs.hasAutoRotate() &&
                Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION) == 0) {
            // Enable auto-rotation
            Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 1)
            OrientationUtils.ROTATION_AUTO
        } else {
            val oldOrientation = getCurrentOrientation(false)
            val newOrientation = OrientationUtils.getOppositeOrientation(oldOrientation, userPrefs)

            Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0)
            Settings.System.putInt(contentResolver, Settings.System.USER_ROTATION, newOrientation)

            newOrientation
        }
    }

    private fun getCurrentOrientation(includeAutoRotate: Boolean): Int {
        val hasAccelerometer = Settings.System
                .getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION) == 1

        return if (hasAccelerometer && includeAutoRotate) {
            OrientationUtils.ROTATION_AUTO
        } else if (hasAccelerometer) {
            getAccelerometerOrientation()
        } else {
            getUserOrientation()
        }
    }

    private fun getUserOrientation(): Int {
        return Settings.System.getInt(contentResolver, Settings.System.USER_ROTATION)
    }

    private fun getAccelerometerOrientation(): Int {
        return (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     */
    private fun showPermissionNotification() {
        NotificationUtils.createNotifyChannelIfNecessary(ContextWrapper(applicationContext))

        val pendingIntent = PendingIntent.getActivity(applicationContext,
                0,
                PermissionUtils.getPermissionIntent(applicationContext),
                PendingIntent.FLAG_ONE_SHOT)

        val contentTitle = resources.getString(R.string.notify_permissions_title)
        val contentText = resources.getString(R.string.notify_permissions_text)

        val builder = NotificationCompat.Builder(applicationContext, Const.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_screen_lock_rotation)
                .setColor(getColor(R.color.app_notification))
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setOngoing(true)
                .setShowWhen(false)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .notify(0, builder.build())
    }
}
