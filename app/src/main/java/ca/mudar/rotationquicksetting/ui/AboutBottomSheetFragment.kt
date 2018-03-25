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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ca.mudar.rotationquicksetting.R
import kotlinx.android.synthetic.main.fragment_about.*

class AboutBottomSheetFragment : BottomSheetDialogFragment(),
        View.OnClickListener {

    companion object {
        fun newInstance(): AboutBottomSheetFragment {
            return AboutBottomSheetFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupListeners()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.about_credits -> showWebsite(R.string.url_mudar_ca)
            R.id.about_source_code -> showWebsite(R.string.url_github)
            R.id.about_license -> activity?.let {
                startActivity(EulaActivity.newIntent(it))
            }
            R.id.about_rate_app -> showWebsite(R.string.url_playstore)
        }
    }

    private fun setupListeners() {
        about_credits?.setOnClickListener(this)
        about_source_code?.setOnClickListener(this)
        about_license?.setOnClickListener(this)
        about_rate_app?.setOnClickListener(this)
    }

    private fun showWebsite(@StringRes website: Int) {
        val viewIntent = Intent(Intent.ACTION_VIEW)
        viewIntent.data = Uri.parse(resources.getString(website))
        startActivity(viewIntent)
    }
}
