package ca.mudar.rotationquicksetting.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager;

import ca.mudar.rotationquicksetting.R;
import ca.mudar.rotationquicksetting.data.UserPrefs;
import ca.mudar.rotationquicksetting.utils.OrientationUtils;

/**
 * Created by mudar on 07/05/17.
 */

public class QuickSettingsService extends TileService {
    private final static String TAG = "QuickSettingsService";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.i(TAG, "onStartListening: ");

        try {
            updateQuickSettingsTile(getCurrentOrientation(UserPrefs.getInstance(getApplicationContext()).hasAutoRotate()),
                    Settings.System.canWrite(getApplicationContext()));
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Log.i(TAG, "onStopListening: ");
    }

    @Override
    public void onClick() {
        Log.i(TAG, "onClick: ");

        if (Settings.System.canWrite(getApplicationContext())) {
            try {
                final int newOrientation = toggleOrientation();
                updateQuickSettingsTile(newOrientation, true);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            showPermissionNotification();
        }
    }

    private void updateQuickSettingsTile(int orientation, boolean canWriteSettings) throws Settings.SettingNotFoundException {
        Log.d(TAG, "updateQuickSettingsTile() called with: orientation = [" + orientation + "]");
        final Tile tile = getQsTile();
        tile.setState(canWriteSettings ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);

        if (OrientationUtils.isLandscape(orientation)) {
            tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_screen_landscape));
            tile.setLabel(getString(R.string.tile_label_land));
        } else if (OrientationUtils.isPortrait(orientation)) {
            tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_screen_portrait));
            tile.setLabel(getString(R.string.tile_label_port));
        } else {
            tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_screen_rotation));
            tile.setLabel(getString(R.string.tile_label_auto_rotate));
        }

        tile.updateTile();
    }

    /**
     * Toggles current location, and returns the new value.
     * Requires a prior check of Settings.System.canWrite()
     *
     * @return new rotation
     */
    private int toggleOrientation() throws Settings.SettingNotFoundException {
        Log.i(TAG, "toggleOrientation: ");
        final ContentResolver contentResolver = getContentResolver();
        final UserPrefs userPrefs = UserPrefs.getInstance(getApplicationContext());

        if (userPrefs.hasAutoRotate() &&
                Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION) == 0) {
            // Enable auto-rotation
            Log.e(TAG, "Enable auto-rotation");
            Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 1);
            return OrientationUtils.ROTATION_AUTO;
        }

        final int oldOrientation = getCurrentOrientation(false);
        final int newOrientation = OrientationUtils.getOppositeOrientation(oldOrientation, userPrefs);

        Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0);
        Settings.System.putInt(contentResolver, Settings.System.USER_ROTATION, newOrientation);

        return newOrientation;
    }

    private int getCurrentOrientation(boolean includeAutoRotate) throws Settings.SettingNotFoundException {
        Log.i(TAG, "getCurrentOrientation: ");
        final ContentResolver contentResolver = getContentResolver();
        final boolean hasAccelerometer = (Settings.System
                .getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION) == 1);

        if (hasAccelerometer && includeAutoRotate) {
            return OrientationUtils.ROTATION_AUTO;
        }

        return hasAccelerometer ? getAccelerometerOrientation() : getUserOrientation();
    }

    private int getUserOrientation() throws Settings.SettingNotFoundException {
        Log.i(TAG, "getUserOrientation: ");
        return Settings.System.getInt(getContentResolver(), Settings.System.USER_ROTATION);
    }

    private int getAccelerometerOrientation() throws Settings.SettingNotFoundException {
        Log.i(TAG, "getAccelerometerOrientation: ");
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getRotation();
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     */
    private void showPermissionNotification() {
        final Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                Uri.parse("package:" + getPackageName()));

        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        final Resources res = getApplicationContext().getResources();
        final String contentTitle = res.getString(R.string.notify_permissions_title);
        final String contentText = res.getString(R.string.notify_permissions_text);

        final Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_launcher);
        final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
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
                .setContentIntent(pendingIntent);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(0, builder.build());
    }

}
