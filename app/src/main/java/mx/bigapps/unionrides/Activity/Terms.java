package mx.bigapps.unionrides.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import mx.bigapps.unionrides.R;

public class Terms extends AppCompatActivity {
    WebView webView;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        back = (ImageView) findViewById(R.id.back);
        webView = (WebView) findViewById(R.id.WebView);
        webView.loadUrl("https://www.unionrides.com/terms/appterm");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
