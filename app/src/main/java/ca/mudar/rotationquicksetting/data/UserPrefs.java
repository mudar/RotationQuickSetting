package ca.mudar.rotationquicksetting.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ca.mudar.rotationquicksetting.Const;
import ca.mudar.rotationquicksetting.Const.PrefsNames;
import ca.mudar.rotationquicksetting.R;

import static ca.mudar.rotationquicksetting.Const.PrefsNames.HAS_HELP;
import static ca.mudar.rotationquicksetting.Const.PrefsNames.HAS_ONBOARDING;

/**
 * Created by mudar on 10/05/17.
 */

public class UserPrefs {

    private static UserPrefs instance;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mPrefsEditor;

    private UserPrefs(Context context) {
        mPrefs = context.getSharedPreferences(Const.APP_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static UserPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new UserPrefs(context);
        }
        return instance;
    }

    private SharedPreferences.Editor edit() {
        if (mPrefsEditor == null) {
            mPrefsEditor = mPrefs.edit();
        }

        return mPrefsEditor;
    }

    public static void setDefaults(Context context) {
        PreferenceManager.setDefaultValues(context,
                Const.APP_PREFS_NAME,
                Context.MODE_PRIVATE,
                R.xml.prefs_defaults,
                false);
    }


    public boolean hasOnboarding() {
        return mPrefs.getBoolean(HAS_ONBOARDING, true);
    }

    public void setOnboardingCompleted() {
        edit().putBoolean(HAS_ONBOARDING, false)
                .commit();
    }

    public boolean hasHelp() {
        return mPrefs.getBoolean(HAS_HELP, true);
    }

    public void setHelpCompleted() {
        edit().putBoolean(HAS_HELP, false)
                .commit();
    }

    public boolean isReversePortrait() {
        return mPrefs.getBoolean(PrefsNames.REVERSE_PORT, false);
    }

    public boolean isReverseLandscape() {
        return mPrefs.getBoolean(PrefsNames.REVERSE_LAND, false);
    }

    public boolean hasAutoRotate() {
        return mPrefs.getBoolean(PrefsNames.AUTO_ROTATE, false);
    }
}
