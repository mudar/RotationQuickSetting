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

package ca.mudar.rotationquicksetting.utils;

import android.support.annotation.NonNull;
import android.view.Surface;

import ca.mudar.rotationquicksetting.data.UserPrefs;

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
