package mx.bigapps.unionrides.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import mx.bigapps.unionrides.R;

public class Gridviewpostpereveiw extends AppCompatActivity {
    WebView webview;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridviewpostpereveiw);
        back = (ImageView) findViewById(R.id.back);
        Intent intent = getIntent();
        String imageurl = intent.getStringExtra("image");
        webview = (WebView) findViewById(R.id.postview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);

        webview.getSettings().setAppCachePath(getFilesDir().getAbsolutePath() + "/cache");
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDatabasePath(getFilesDir().getAbsolutePath() + "/databases");
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 9_3 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13E233 Safari/601.1");

        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (Build.VERSION.SDK_INT < 8) {
            webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        } else {
            webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        }
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(imageurl);
        webview.getSettings().setAllowFileAccess(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(Gridviewpostpereveiw.this, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("url", url);

                // pd.show();

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;

            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                SslCertificate serverCertificate = error.getCertificate();


                // Check if Cert-Domain equals the Uri-Domain
                String certDomain = serverCertificate.getIssuedTo().getCName();
                Log.d("error", certDomain.toString());

                if (certDomain.equals("*.unionrides.com")) {
                    handler.proceed();
                }
                // Ignore SSL certificate errors
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("title", view.getTitle());
                // dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                //  pd.dismiss();
            }


        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
