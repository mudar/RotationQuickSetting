package ca.mudar.rotationquicksetting.ui

import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import ca.mudar.rotationquicksetting.Const.FragmentTags
import ca.mudar.rotationquicksetting.data.UserPrefs

/**
 * Created by mudar on 10/05/17.
 */

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
