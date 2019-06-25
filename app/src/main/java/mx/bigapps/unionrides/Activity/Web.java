package mx.bigapps.unionrides.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.R;

public class Web extends Activity {
    ImageView about_back;
    WebView youtube_webView;
    String video_url = "";
    JCVideoPlayerStandard jcVideoPlayerStandard;
    String thunb;
    Button close, Rotate;

    String view_photo_status;
    public static final String PREFS_VIEW_PHOTO = "MyPrefsviewphoto";
    String rotation = "";
    private ProgressDialog pDialog;
    private VideoView videoView;
    int flag = 0;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_11);
        try {
            Intent i = getIntent();
            flag = i.getIntExtra("flag", 0);
            if (flag == 1) {
                video_url = PrefMangr.getInstance().getVideoUrl();

            } else {
                video_url = i.getStringExtra("video");
            }
            thunb = i.getStringExtra("image");
            rotation = i.getStringExtra("rotation");
        } catch (Exception e) {

        }
        Log.d("enter url is", ">>" + video_url);
        try {
//          String link="http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
            videoView = (VideoView) findViewById(R.id.videoView);

            // Create a progressbar
            pDialog = new ProgressDialog(Web.this);
            // Set progressbar title
            //pDialog.setTitle("Android Video Streaming Tutorial");
            // Set progressbar message
            pDialog.setMessage("buffering...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            Drawable drawable = getResources().getDrawable(R.drawable.loadinguni);
            //Getting a drawable from progress dialog


            pDialog.setIndeterminateDrawable(drawable);
            // Show progressbar
            pDialog.show();
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });


            try {
                // Start the MediaController
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);
                final Uri video = Uri.parse(video_url);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(video);


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            videoView.requestFocus();
            videoView.setZOrderOnTop(true);
          /*  videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    pDialog.dismiss();
                    videoView.start();
                }
            });*/
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int arg1,
                                                       int arg2) {
                            // TODO Auto-generated method stub
                            pDialog.dismiss();
                            mp.start();
                        }
                    });
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
            Log.d("exception", String.valueOf(e));
        }

        close = (Button) findViewById(R.id.close);

        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                view_photo_status = "show_photo_status";
                SharedPreferences preferences_photo;
                preferences_photo = getSharedPreferences(PREFS_VIEW_PHOTO, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences_photo.edit();
                editor.putString("view_photo_status", view_photo_status);
                editor.commit();
                Log.d("view_photo_status", " " + view_photo_status);
                finish();


            }
        });

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        view_photo_status = "show_photo_status";
        SharedPreferences preferences_photo;
        preferences_photo = getSharedPreferences(PREFS_VIEW_PHOTO, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences_photo.edit();
        editor.putString("view_photo_status", view_photo_status);
        editor.commit();
        Log.d("view_photo_status", " " + view_photo_status);
        JCVideoPlayer.releaseAllVideos();
        finish();

    }

    @Override
    protected void onDestroy() {
        //+-JCVideoPlayer.releaseAllVideos();
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}


	
	
	
