package ca.mudar.rotationquicksetting

import android.os.Build

/**
 * Created by mudar on 10/05/17.
 */

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
