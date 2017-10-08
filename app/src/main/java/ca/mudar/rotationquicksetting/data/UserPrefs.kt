package ca.mudar.rotationquicksetting.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import ca.mudar.rotationquicksetting.Const
import ca.mudar.rotationquicksetting.Const.PrefsNames
import ca.mudar.rotationquicksetting.Const.PrefsNames.HAS_HELP
import ca.mudar.rotationquicksetting.Const.PrefsNames.HAS_ONBOARDING
import ca.mudar.rotationquicksetting.R

/**
 * Created by mudar on 10/05/17.
 */
class UserPrefs constructor(context: Context) {

    companion object {
        fun setDefaults(context: Context) {
            PreferenceManager.setDefaultValues(context,
                    Const.APP_PREFS_NAME,
                    Context.MODE_PRIVATE,
                    R.xml.prefs_defaults,
                    false)
        }
    }

    private val prefs = context.getSharedPreferences(Const.APP_PREFS_NAME, Context.MODE_PRIVATE)
    private var prefsEditor: SharedPreferences.Editor? = null

    val isReversePortrait: Boolean
        get() = prefs.getBoolean(PrefsNames.REVERSE_PORT, false)

    val isReverseLandscape: Boolean
        get() = prefs.getBoolean(PrefsNames.REVERSE_LAND, false)

    private fun edit(): SharedPreferences.Editor {
        if (prefsEditor == null) {
            prefsEditor = prefs.edit()
        }

        return prefsEditor!!
    }


    fun hasOnboarding(): Boolean {
        return prefs.getBoolean(HAS_ONBOARDING, true)
    }

    fun setOnboardingCompleted() {
        edit().putBoolean(HAS_ONBOARDING, false)
                .commit()
    }

    fun hasHelp(): Boolean {
        return prefs.getBoolean(HAS_HELP, true)
    }

    fun setHelpCompleted() {
        edit().putBoolean(HAS_HELP, false)
                .commit()
    }

    fun hasAutoRotate(): Boolean {
        return prefs.getBoolean(PrefsNames.AUTO_ROTATE, false)
    }
}
