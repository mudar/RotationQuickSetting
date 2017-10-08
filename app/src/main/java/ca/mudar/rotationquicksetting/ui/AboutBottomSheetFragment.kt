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

/**
 * Created by mudar on 16/05/17.
 */

class AboutBottomSheetFragment : BottomSheetDialogFragment(),
        View.OnClickListener {

    companion object {
        fun newInstance(): AboutBottomSheetFragment {
            return AboutBottomSheetFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_about, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupListeners()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.about_credits -> showWebsite(R.string.url_mudar_ca)
            R.id.about_source_code -> showWebsite(R.string.url_github)
            R.id.about_license -> startActivity(EulaActivity.newIntent(activity))
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
