package ca.mudar.rotationquicksetting.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ca.mudar.rotationquicksetting.Const;
import ca.mudar.rotationquicksetting.R;

/**
 * Created by mudar on 11/06/17.
 */

public class EulaActivity extends AppCompatActivity {
    private static final String ASSETS_URI = "file:///android_asset/";


    public static Intent newIntent(Context context) {
        return new Intent(context, EulaActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_eula);

        loadWebView((WebView) findViewById(R.id.webview));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void loadWebView(WebView v) {
        // Set basic style
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.webview_bg));
        v.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // Open links in external browser
        v.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request != null) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });

        // Load HTML content from assets
        v.loadUrl(ASSETS_URI + Const.LocalAssets.LICENSE);
    }

}
