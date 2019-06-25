package mx.bigapps.unionrides.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.helper;

/**
 * Created by admin on 13-10-2017.
 */
public class PostPreviewimage extends AppCompatActivity implements BetterVideoCallback {
    ImageView header_back, header_menu, header_logo;
    TextView header_txt;
    RelativeLayout header_layout;

    static Context context;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String imageurl, position;
    ImageView imageView;
    ImageView viewPager;
    static ArrayList picurls;

    int pos;
    static TextView count;
    ImageView btnClose;
    String video, image, list;
    WebView videoview;
    Zoom img;
    BetterVideoPlayer videoshow;
    ProgressBar progressBar;


    TextView add_comment,user_comment;
    EditText type_comment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);
        videoview = (WebView) findViewById(R.id.videoview);
        videoshow = (BetterVideoPlayer) findViewById(R.id.bvp);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        img = (Zoom) findViewById(R.id.pager);
        //  img.setImageResource(R.drawable.imagecar);
        img.setMaxZoom(4f);
        Intent intent = getIntent();
        video = intent.getStringExtra("video");
        image = intent.getStringExtra("image");
        list = intent.getStringExtra("list");
        if (video != null) {
            img.setVisibility(View.GONE);
            videoshow.setSource(Uri.parse(video));
            videoshow.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(video);
           /* Glide.with(this)
                    .load("https://raw.githubusercontent.com/MarcinMoskala/VideoPlayView/master/art/loading.gif")
                    .into(videoshow.getLoadingView());*/


        } else {
            img.setVisibility(View.VISIBLE);
            videoshow.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            try {

                if (!image.equals("")) {
                    Picasso.with(context).load(image)
                            .placeholder(R.drawable.loadinguni)
                            .error(R.drawable.loadinguni)

                           // .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(img);

                } else {
                    img.setBackgroundResource(R.drawable.default_user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        btnClose = (ImageView) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.status.equals("otherprofile_fan")) {
                    Intent intent = new Intent(PostPreviewimage.this, PublicProfile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("flag", 2);
                } else if (helper.status.equals("myprofile_fan")) {
                    Intent intent = new Intent(PostPreviewimage.this, Wall.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("flag", 5);
                    startActivity(intent);
                } else if (helper.status.equals("Event")) {
                    Intent intent = new Intent(PostPreviewimage.this, EventsMain.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    // intent.putExtra("flag", 5);
                    startActivity(intent);
                } else if (list != null) {
                   /* Intent intent = new Intent(PostPreviewimage.this, Wall.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);*/
                    finish();
                } else {

                  /*  Intent intent = new Intent(PostPreviewimage.this, Wall.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("flag", 100);
                    startActivity(intent);*/

                    finish();
                }
                finish();
            }
        });


        // setContentView(img);

    }

    private int getScale() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width) / new Double(1000000);
        val = val * 100d;
        return val.intValue();
    }


    @Override
    public void onStarted(BetterVideoPlayer player) {

    }

    @Override
    public void onPaused(BetterVideoPlayer player) {

    }

    @Override
    public void onPreparing(BetterVideoPlayer player) {

    }

    @Override
    public void onPrepared(BetterVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(BetterVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(BetterVideoPlayer player) {

    }

    @Override
    public void onToggleControls(BetterVideoPlayer player, boolean isShowing) {

    }
}
