package ca.mudar.rotationquicksetting.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by mudar on 17/05/17.
 */

public class PermissionUtils {

    public static Intent getPermissionIntent(Context context) {
        return new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                Uri.parse("package:" + context.getPackageName()));
    }
}
