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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.View

import ca.mudar.rotationquicksetting.Const.RequestCode
import ca.mudar.rotationquicksetting.R
import ca.mudar.rotationquicksetting.utils.PermissionUtils

class OnboardingActivity : AppCompatActivity(),
        View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding)

        setupListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
