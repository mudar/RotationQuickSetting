package ca.mudar.rotationquicksetting.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ca.mudar.rotationquicksetting.data.UserPrefs;

/**
 * Created by mudar on 10/05/17.
 */

public class MainActivity extends AppCompatActivity implements
        SettingsFragment.SettingsAboutCallback {
    private final static String FRAGMENT_TAG_SETTINGS = "f_settings";
    private final static String FRAGMENT_TAG_ABOUT = "f_about";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set default preferences
        UserPrefs.setDefaults(getApplicationContext());

        if (savedInstanceState == null) {
            final Fragment fragment = SettingsFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment, FRAGMENT_TAG_SETTINGS)
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
        bottomSheet.show(getSupportFragmentManager(), FRAGMENT_TAG_ABOUT);
    }
}
