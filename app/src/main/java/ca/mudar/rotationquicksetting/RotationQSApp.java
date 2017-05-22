package ca.mudar.rotationquicksetting;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import ca.mudar.rotationquicksetting.data.UserPrefs;
import io.fabric.sdk.android.Fabric;

/**
 * Created by mudar on 22/05/17.
 */

public class RotationQSApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.USE_CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
        }

        UserPrefs.setDefaults(this);

    }

}
