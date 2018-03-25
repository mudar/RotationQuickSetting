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

package ca.mudar.rotationquicksetting.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import ca.mudar.rotationquicksetting.Const
import ca.mudar.rotationquicksetting.Const.PrefsNames
import ca.mudar.rotationquicksetting.Const.PrefsNames.HAS_HELP
import ca.mudar.rotationquicksetting.Const.PrefsNames.HAS_ONBOARDING
import ca.mudar.rotationquicksetting.R

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
