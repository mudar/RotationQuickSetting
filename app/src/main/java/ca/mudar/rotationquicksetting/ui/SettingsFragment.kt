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

package ca.mudar.rotationquicksetting.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceGroup
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import ca.mudar.rotationquicksetting.BuildConfig
import ca.mudar.rotationquicksetting.Const
import ca.mudar.rotationquicksetting.Const.PrefsNames
import ca.mudar.rotationquicksetting.R
import ca.mudar.rotationquicksetting.data.UserPrefs
import ca.mudar.rotationquicksetting.utils.OrientationUtils.*
import ca.mudar.rotationquicksetting.utils.PermissionUtils
import java.util.*

class SettingsFragment : PreferenceFragment(),
        SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    private var mPermissionGranted: Preference? = null
    private var mListener: SettingsAboutCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SettingsAboutCallback) {
            mListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager.sharedPreferencesName = Const.APP_PREFS_NAME
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE

        addPreferencesFromResource(R.xml.prefs_settings)
        mPermissionGranted = findPreference(PrefsNames.PERMISSION_GRANTED)

        preferenceManager?.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)

        mPermissionGranted?.onPreferenceClickListener = this

        findPreference(PrefsNames.HELP)?.onPreferenceClickListener = this
        findPreference(PrefsNames.ABOUT)?.onPreferenceClickListener = this

        hideHelpIfPossible()
    }

    override fun onResume() {
        super.onResume()

        setupSummaries()
    }

    override fun onDestroy() {
        super.onDestroy()

        /**
         * Remove the listener
         */
        preferenceManager?.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    /**
     * Implements SharedPreferences.OnSharedPreferenceChangeListener
     *
     * @param sharedPreferences
     * @param key
     */
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (PrefsNames.REVERSE_PORT == key) {
            showTryRotationSnackbar(if (sharedPreferences.getBoolean(key, false))
                ROTATION_PORT_REVERSE
            else
                ROTATION_PORT)
        } else if (PrefsNames.REVERSE_LAND == key) {
            showTryRotationSnackbar(if (sharedPreferences.getBoolean(key, false))
                ROTATION_LAND_REVERSE
            else
                ROTATION_LAND)
        }
    }

    /**
     * Implements Preference.OnPreferenceClickListener
     *
     * @param preference
     * @return
     */
    override fun onPreferenceClick(preference: Preference): Boolean {
        val key = preference.key
        if (PrefsNames.PERMISSION_GRANTED == key) {
            startActivity(PermissionUtils.getPermissionIntent(context))
            return true
        } else if (PrefsNames.HELP == key) {
            showHelp()
            return true
        } else if (PrefsNames.ABOUT == key && mListener != null) {
            mListener!!.onShowAbout()
            return true
        }

        return false
    }

    private fun setupSummaries() {
        mPermissionGranted?.setSummary(getPermissionGrantedSummary())

        findPreference(PrefsNames.ABOUT)?.summary = getString(R.string.prefs_about_summary,
                BuildConfig.VERSION_NAME)
    }

    @StringRes
    private fun getPermissionGrantedSummary(): Int {
        return when (Settings.System.canWrite(context)) {
            true -> R.string.prefs_permission_granted_on
            false -> R.string.prefs_permission_granted_off
        }
    }

    private fun setOrientation(orientation: Int) {
        if (!Settings.System.canWrite(context)) {
            return
        }

        Settings.System.putInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0)
        Settings.System.putInt(context.contentResolver, Settings.System.USER_ROTATION, orientation)
    }

    private fun showTryRotationSnackbar(orientation: Int) {
        if (!Settings.System.canWrite(context)) {
            return
        }

        val message = when (orientation) {
            ROTATION_PORT -> R.string.snackbar_try_rotation_port
            ROTATION_PORT_REVERSE -> R.string.snackbar_try_rotation_port_reverse
            ROTATION_LAND -> R.string.snackbar_try_rotation_land
            ROTATION_LAND_REVERSE -> R.string.snackbar_try_rotation_land_reverse
            else -> return
        }

        view?.let {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.btn_try_it) { setOrientation(orientation) }
                    .setActionTextColor(ContextCompat.getColor(context, R.color.snackbar_action_text))
                    .show()
        }
    }

    private fun showHelp() {
        val language = Locale.getDefault().language
        val viewIntent = Intent(Intent.ACTION_VIEW)
        viewIntent.data = Uri.parse(resources
                .getString(R.string.url_help_quick_settings, language))
        startActivity(viewIntent)
    }

    private fun hideHelpIfPossible() {
        val prefs = UserPrefs(context)
        if (!prefs.hasHelp()) {
            (findPreference(PrefsNames.CAT_GENERAL) as PreferenceGroup)
                    .removePreference(findPreference(PrefsNames.HELP))
            prefs.setHelpCompleted()
        }
    }

    interface SettingsAboutCallback {
        fun onShowAbout()
    }
}
