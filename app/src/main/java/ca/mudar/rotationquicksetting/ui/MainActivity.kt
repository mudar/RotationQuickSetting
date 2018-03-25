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

import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import ca.mudar.rotationquicksetting.Const.FragmentTags
import ca.mudar.rotationquicksetting.data.UserPrefs

class MainActivity : AppCompatActivity(),
        SettingsFragment.SettingsAboutCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showOnboardingIfNecessary()

        if (savedInstanceState == null) {
            val fragment = SettingsFragment.newInstance()
            fragmentManager.beginTransaction()
                    .replace(android.R.id.content, fragment, FragmentTags.SETTINGS)
                    .commit()
        }
    }

    /**
     * Implements SettingsFragment.SettingsAboutCallback
     * Shows about the About BottomSheetDialogFragment
     */
    override fun onShowAbout() {
        AboutBottomSheetFragment.newInstance()
                .show(supportFragmentManager, FragmentTags.ABOUT)
    }

    private fun showOnboardingIfNecessary() {
        val prefs = UserPrefs(applicationContext)
        if (prefs.hasOnboarding()) {
            if (!Settings.System.canWrite(this)) {
                startActivity(OnboardingActivity.newIntent(applicationContext))
            }
            prefs.setOnboardingCompleted()
        }
    }
}
