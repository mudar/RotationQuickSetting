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

package ca.mudar.rotationquicksetting

import android.os.Build

object Const {

    /**
     * SharedPreferences
     */
    const val APP_PREFS_NAME = "rotationqs_prefs"

    object PrefsNames {
        val CAT_GENERAL = "prefs_cat_general"

        val REVERSE_PORT = "prefs_reverse_port"
        val REVERSE_LAND = "prefs_reverse_land"
        val AUTO_ROTATE = "prefs_auto_rotate"
        val PERMISSION_GRANTED = "prefs_permission_granted"
        val HELP = "prefs_help"
        val ABOUT = "prefs_about"
        val HAS_ONBOARDING = "has_onboarding"
        val HAS_HELP = "has_help"
    }

    object FragmentTags {
        val SETTINGS = "f_settings"
        val ABOUT = "f_about"
    }

    object RequestCode {
        val PERMISSION_RESULT = 10
    }

    // Assets
    object LocalAssets {
        val LICENSE = "gpl-3.0-standalone.html"
    }

    const val NOTIFICATION_CHANNEL_ID = "atPeace"

    // Device compatibility
    val SUPPORTS_OREO = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}
