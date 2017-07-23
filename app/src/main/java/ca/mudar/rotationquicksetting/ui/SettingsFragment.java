package ca.mudar.rotationquicksetting.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.Locale;

import ca.mudar.rotationquicksetting.BuildConfig;
import ca.mudar.rotationquicksetting.Const;
import ca.mudar.rotationquicksetting.Const.PrefsNames;
import ca.mudar.rotationquicksetting.R;
import ca.mudar.rotationquicksetting.data.UserPrefs;
import ca.mudar.rotationquicksetting.utils.PermissionUtils;

import static ca.mudar.rotationquicksetting.utils.OrientationUtils.ROTATION_LAND;
import static ca.mudar.rotationquicksetting.utils.OrientationUtils.ROTATION_LAND_REVERSE;
import static ca.mudar.rotationquicksetting.utils.OrientationUtils.ROTATION_PORT;
import static ca.mudar.rotationquicksetting.utils.OrientationUtils.ROTATION_PORT_REVERSE;

/**
 * Created by mudar on 10/05/17.
 */

public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    private SharedPreferences mSharedPrefs;
    private Preference mPermissionGranted;
    private SettingsAboutCallback mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof SettingsAboutCallback) {
            mListener = (SettingsAboutCallback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final PreferenceManager pm = this.getPreferenceManager();
        pm.setSharedPreferencesName(Const.APP_PREFS_NAME);
        pm.setSharedPreferencesMode(Context.MODE_PRIVATE);

        addPreferencesFromResource(R.xml.prefs_settings);
        mPermissionGranted = findPreference(PrefsNames.PERMISSION_GRANTED);

        mSharedPrefs = pm.getSharedPreferences();

        mSharedPrefs.registerOnSharedPreferenceChangeListener(this);

        mPermissionGranted.setOnPreferenceClickListener(this);
        findPreference(PrefsNames.HELP).setOnPreferenceClickListener(this);
        findPreference(PrefsNames.ABOUT).setOnPreferenceClickListener(this);
        findPreference(PrefsNames.EULA).setOnPreferenceClickListener(this);

        hideHelpIfPossible();
    }

    @Override
    public void onResume() {
        super.onResume();

        setupSummaries();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /**
         * Remove the listener
         */
        if (mSharedPrefs != null) {
            mSharedPrefs.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    /**
     * Implements SharedPreferences.OnSharedPreferenceChangeListener
     *
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PrefsNames.REVERSE_PORT.equals(key)) {
            showTryRotationSnackbar(sharedPreferences.getBoolean(key, false) ?
                    ROTATION_PORT_REVERSE : ROTATION_PORT);
        } else if (PrefsNames.REVERSE_LAND.equals(key)) {
            showTryRotationSnackbar(sharedPreferences.getBoolean(key, false) ?
                    ROTATION_LAND_REVERSE : ROTATION_LAND);
        }
    }

    /**
     * Implements Preference.OnPreferenceClickListener
     *
     * @param preference
     * @return
     */
    @Override
    public boolean onPreferenceClick(Preference preference) {
        final String key = preference.getKey();
        if (PrefsNames.PERMISSION_GRANTED.equals(key)) {
            startActivity(PermissionUtils.getPermissionIntent(getContext()));
            return true;
        } else if (PrefsNames.HELP.equals(key)) {
            showHelp();
            return true;
        } else if (mListener != null) {
            if (PrefsNames.ABOUT.equals(key)) {
                mListener.onShowAbout();
                return true;
            } else if (PrefsNames.EULA.equals(key)) {
                mListener.onShowEula();
                return true;
            }
        }

        return false;
    }

    private void setupSummaries() {
        mPermissionGranted.setSummary(getPermissionGrantedSummary());

        findPreference(PrefsNames.ABOUT).setSummary(getString(
                R.string.prefs_about_summary, BuildConfig.VERSION_NAME));
    }

    @StringRes
    private int getPermissionGrantedSummary() {
        return Settings.System.canWrite(getContext()) ?
                R.string.prefs_permission_granted_on : R.string.prefs_permission_granted_off;
    }

    private void setOrientation(int orientation) {
        if (!Settings.System.canWrite(getContext())) {
            return;
        }

        final ContentResolver contentResolver = getContext().getContentResolver();
        Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0);
        Settings.System.putInt(contentResolver, Settings.System.USER_ROTATION, orientation);
    }

    private void showTryRotationSnackbar(final int orientation) {
        if (getView() == null || !Settings.System.canWrite(getContext())) {
            return;
        }

        @StringRes int message;
        switch (orientation) {
            case ROTATION_PORT:
                message = R.string.snackbar_try_rotation_port;
                break;
            case ROTATION_PORT_REVERSE:
                message = R.string.snackbar_try_rotation_port_reverse;
                break;
            case ROTATION_LAND:
                message = R.string.snackbar_try_rotation_land;
                break;
            case ROTATION_LAND_REVERSE:
                message = R.string.snackbar_try_rotation_land_reverse;
                break;
            default:
                return;
        }
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                .setAction(R.string.btn_try_it, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOrientation(orientation);
                    }
                })
                .setActionTextColor(ContextCompat.getColor(getContext(), R.color.snackbar_action_text))
                .show();
    }

    private void showHelp() {
        final String language = Locale.getDefault().getLanguage();
        final Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        viewIntent.setData(Uri.parse(getResources()
                .getString(R.string.url_help_quick_settings, language)));
        startActivity(viewIntent);
    }

    private void hideHelpIfPossible() {
        final UserPrefs prefs = UserPrefs.getInstance(getContext());
        if (!prefs.hasHelp()) {
            ((PreferenceGroup) findPreference(PrefsNames.CAT_GENERAL))
                    .removePreference(findPreference(PrefsNames.HELP));
            prefs.setHelpCompleted();
        }
    }

    public interface SettingsAboutCallback {
        void onShowAbout();

        void onShowEula();
    }
}
