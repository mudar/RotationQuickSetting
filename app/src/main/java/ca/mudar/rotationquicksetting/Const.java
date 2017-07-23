package ca.mudar.rotationquicksetting;

/**
 * Created by mudar on 10/05/17.
 */

public class Const {

    /**
     * SharedPreferences
     */
    public static final String APP_PREFS_NAME = "rotationqs_prefs";

    public interface PrefsNames {
        String CAT_GENERAL = "prefs_cat_general";

        String REVERSE_PORT = "prefs_reverse_port";
        String REVERSE_LAND = "prefs_reverse_land";
        String AUTO_ROTATE = "prefs_auto_rotate";
        String PERMISSION_GRANTED = "prefs_permission_granted";
        String HELP = "prefs_help";
        String ABOUT = "prefs_about";
        String EULA = "prefs_eula";
        String HAS_ONBOARDING = "has_onboarding";
        String HAS_HELP = "has_help";
    }

    public interface FragmentTags {
        String SETTINGS = "f_settings";
        String ABOUT = "f_about";
    }

    public interface RequestCode {
        int PERMISSION_RESULT = 10;
    }

    // Assets
    public interface LocalAssets {
        String LICENSE = "gpl-3.0-standalone.html";
    }
}
