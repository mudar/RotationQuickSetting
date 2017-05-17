package ca.mudar.rotationquicksetting.utils;

import android.support.annotation.NonNull;
import android.view.Surface;

import ca.mudar.rotationquicksetting.data.UserPrefs;

/**
 * Created by mudar on 10/05/17.
 */

public class OrientationUtils {
    public final static int ROTATION_PORT = Surface.ROTATION_0;
    public final static int ROTATION_PORT_REVERSE = Surface.ROTATION_180;
    public final static int ROTATION_LAND = Surface.ROTATION_90;
    public final static int ROTATION_LAND_REVERSE = Surface.ROTATION_270;
    public final static int ROTATION_AUTO = -1;

    public static int getOppositeOrientation(int orientation, @NonNull UserPrefs prefs) {
        if (isPortrait(orientation)) {
            return prefs.isReverseLandscape() ? ROTATION_LAND_REVERSE : ROTATION_LAND;
        } else if (isLandscape(orientation)) {
            return prefs.isReversePortrait() ? ROTATION_PORT_REVERSE : ROTATION_PORT;
        } else {
            return ROTATION_AUTO;
        }
    }

    public static boolean isPortrait(int orientation) {
        return (orientation == ROTATION_PORT) || (orientation == ROTATION_PORT_REVERSE);
    }

    public static boolean isLandscape(int orientation) {
        return (orientation == ROTATION_LAND) || (orientation == ROTATION_LAND_REVERSE);
    }
}
