package ca.mudar.rotationquicksetting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.View

import ca.mudar.rotationquicksetting.Const.RequestCode
import ca.mudar.rotationquicksetting.R
import ca.mudar.rotationquicksetting.utils.PermissionUtils

/**
 * Created by mudar on 17/05/17.
 */

class OnboardingActivity : AppCompatActivity(),
        View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding)

        setupListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode.PERMISSION_RESULT && Settings.System.canWrite(this)) {
            finish()
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.btn_grant_permission) {
            startActivityForResult(PermissionUtils.getPermissionIntent(applicationContext),
                    RequestCode.PERMISSION_RESULT)
        } else if (id == R.id.btn_skip) {
            finish()
        }
    }

    private fun setupListeners() {
        findViewById<View>(R.id.btn_grant_permission).setOnClickListener(this)
        findViewById<View>(R.id.btn_skip).setOnClickListener(this)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, OnboardingActivity::class.java)
        }
    }
}
