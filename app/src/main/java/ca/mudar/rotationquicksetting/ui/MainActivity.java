package ca.mudar.rotationquicksetting.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import ca.mudar.rotationquicksetting.Const.FragmentTags;
import ca.mudar.rotationquicksetting.data.UserPrefs;

/**
 * Created by mudar on 10/05/17.
 */

public class MainActivity extends AppCompatActivity implements
        SettingsFragment.SettingsAboutCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showOnboardingIfNecessary();

        // Set default preferences
        UserPrefs.setDefaults(getApplicationContext());

        if (savedInstanceState == null) {
            final Fragment fragment = SettingsFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment, FragmentTags.SETTINGS)
                    .commit();
        }
    }

    /**
     * Implements SettingsFragment.SettingsAboutCallback
     * Shows about the About BottomSheetDialogFragment
     */
    @Override
    public void onShowAbout() {
        final AboutBottomSheetFragment bottomSheet = AboutBottomSheetFragment.newInstance();
        bottomSheet.show(getSupportFragmentManager(), FragmentTags.ABOUT);
    }

    private void showOnboardingIfNecessary() {
        final UserPrefs prefs = UserPrefs.getInstance(getApplicationContext());
        if (prefs.hasOnboarding()) {
            if (!Settings.System.canWrite(this)) {
                startActivity(OnboardingActivity.newIntent(getApplicationContext()));
            }
            prefs.setOnboardingCompleted();
        }
    }
}
