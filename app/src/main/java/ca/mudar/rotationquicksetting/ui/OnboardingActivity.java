package ca.mudar.rotationquicksetting.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ca.mudar.rotationquicksetting.Const.RequestCode;
import ca.mudar.rotationquicksetting.R;
import ca.mudar.rotationquicksetting.utils.PermissionUtils;

/**
 * Created by mudar on 17/05/17.
 */

public class OnboardingActivity extends AppCompatActivity implements
        View.OnClickListener {
    public static Intent newIntent(Context context) {
        return new Intent(context, OnboardingActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboarding);

        setupListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.PERMISSION_RESULT && Settings.System.canWrite(this)) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_grant_permission) {
            startActivityForResult(PermissionUtils.getPermissionIntent(getApplicationContext()),
                    RequestCode.PERMISSION_RESULT);
        } else if (id == R.id.btn_skip) {
            finish();
        }
    }

    private void setupListeners() {
        findViewById(R.id.btn_grant_permission).setOnClickListener(this);
        findViewById(R.id.btn_skip).setOnClickListener(this);
    }
}
