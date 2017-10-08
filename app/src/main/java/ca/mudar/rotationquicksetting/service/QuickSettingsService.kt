package ca.mudar.rotationquicksetting.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.view.WindowManager

import ca.mudar.rotationquicksetting.R
import ca.mudar.rotationquicksetting.data.UserPrefs
import ca.mudar.rotationquicksetting.utils.OrientationUtils
import ca.mudar.rotationquicksetting.utils.PermissionUtils

/**
 * Created by mudar on 07/05/17.
 */

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
        val pendingIntent = PendingIntent.getActivity(applicationContext,
                0,
                PermissionUtils.getPermissionIntent(applicationContext),
                PendingIntent.FLAG_ONE_SHOT)

        val contentTitle = resources.getString(R.string.notify_permissions_title)
        val contentText = resources.getString(R.string.notify_permissions_text)

        val drawable = ContextCompat.getDrawable(applicationContext, R.mipmap.ic_launcher)
        val bitmap = (drawable as BitmapDrawable).bitmap

        // TODO refactor to handle channels
        val builder = NotificationCompat.Builder(applicationContext)
                .setSmallIcon(R.drawable.ic_screen_lock_rotation)
                .setLargeIcon(bitmap)
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
