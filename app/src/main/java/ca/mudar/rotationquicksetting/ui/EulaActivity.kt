package ca.mudar.rotationquicksetting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import ca.mudar.rotationquicksetting.Const
import ca.mudar.rotationquicksetting.R
import kotlinx.android.synthetic.main.activity_eula.*

/**
 * Created by mudar on 11/06/17.
 */

class EulaActivity : AppCompatActivity() {
    private val ASSETS_URI = "file:///android_asset/"

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, EulaActivity::class.java)
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_eula)

        loadWebView(webview)
    }

    private fun loadWebView(webView: WebView) {
        // Set basic style
        webView.setBackgroundColor(ContextCompat.getColor(this, R.color.webview_bg))
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        // Open links in external browser
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest?): Boolean {
                request?.let {
                    val intent = Intent(Intent.ACTION_VIEW, request.url)
                    startActivity(intent)
                    return true
                }

                return false
            }
        }

        // Load HTML content from assets
        webView.loadUrl(ASSETS_URI + Const.LocalAssets.LICENSE)
    }
}
