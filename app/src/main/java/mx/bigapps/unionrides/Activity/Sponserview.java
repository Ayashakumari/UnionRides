package mx.bigapps.unionrides.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import mx.bigapps.unionrides.R;


public class Sponserview extends AppCompatActivity {
    WebView webview;
    ImageView cancle;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        Intent intent = getIntent();
        String url = intent.getStringExtra("image");
        webview = (WebView) findViewById(R.id.webview);
        webview.setVisibility(View.GONE);
        cancle = (ImageView) findViewById(R.id.cancle);
        cancle.setVisibility(View.GONE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webview.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDatabasePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT < 8) {
            webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        } else {
            webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        }
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(Sponserview.this, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("url", url);

               // pd.show();

            }


            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("title", view.getTitle());

                webview.setVisibility(View.VISIBLE);
              //  pd.dismiss();
            }


        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        webview.loadUrl("about:blank");
    }

    @Override
    protected void onStop() {
        super.onStop();
        webview.loadUrl("about:blank");
    }
}
