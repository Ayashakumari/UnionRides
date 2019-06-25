package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mx.bigapps.unionrides.Activity.Gridviewpostpereveiw;
import mx.bigapps.unionrides.Activity.PostPreviewimage;
import mx.bigapps.unionrides.Activity.SquareImageView;
import mx.bigapps.unionrides.Model.UserPosts;
import mx.bigapps.unionrides.R;


/**
 * Created by seemtech2 on 05-10-2017.
 */

public class UserPost_GridAdapter extends BaseAdapter {

    Context context;
    Activity activity;

    ArrayList<UserPosts> employer_list = new ArrayList<UserPosts>();
    int mCurrentPlayingPosition = -1;
    LayoutInflater mInflater;

    public UserPost_GridAdapter(Context context, ArrayList<UserPosts> employer_list) {
        this.context = context;
        this.employer_list = employer_list;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return employer_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.userpostgrid, null);
        }

        ImageView play = (ImageView) convertView.findViewById(R.id.play);
        play.setVisibility(View.GONE);

        SquareImageView postimage = (SquareImageView) convertView.findViewById(R.id.postimage);
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progress);


        if (employer_list.get(position).getPosttype().equals("video")) {
            play.setVisibility(View.VISIBLE);
            try {
                String image = "" + employer_list.get(position);
                if (!image.equals("")) {
                    Picasso.with(context)
                            .load(employer_list.get(position).getVideoThumb())
                            .noFade()
                            .placeholder(R.drawable.loadinguni)

                            .error(R.drawable.loadinguni)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(postimage);

                    //  UrlImageViewHelper.setUrlDrawable(holder.find_people_img,find_peoplelist.get(position).getImage());
                } else {
                    postimage.setBackgroundResource(R.drawable.loadinguni);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            postimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Intent intent = new Intent(context, Web.class);
                    intent.putExtra("video", employer_list.get(position).getData());
                    context.startActivity(intent);*/

                    final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                    dialog.setContentView(R.layout.activity_videoview);
                    // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                  //  progressBar.setVisibility(View.VISIBLE);
                    final WebView webview = (WebView) dialog.findViewById(R.id.webview);
                    webview.setVisibility(View.GONE);
                    final ImageView cancle = (ImageView) dialog.findViewById(R.id.cancle);
                    cancle.setVisibility(View.GONE);
                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    webview.setWebViewClient(new WebViewClient());
                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    webview.getSettings().setDomStorageEnabled(true);
                    webview.getSettings().setAppCacheEnabled(true);
                    webview.getSettings().setAppCachePath(context.getFilesDir().getAbsolutePath() + "/cache");
                    webview.getSettings().setDatabaseEnabled(true);
                    webview.getSettings().setDatabasePath(context.getFilesDir().getAbsolutePath() + "/databases");
                    webview.getSettings().setPluginState(WebSettings.PluginState.ON);
                    if (Build.VERSION.SDK_INT < 8) {
                        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
                    } else {
                        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
                    }
                    webview.setWebChromeClient(new WebChromeClient());
                    webview.loadUrl(employer_list.get(position).getVideo_web_views_url());
                    webview.setWebViewClient(new WebViewClient() {
                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                            Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            Log.d("url", url);

                            // pd.show();

                        }


                        @Override
                        public void onPageFinished(WebView view, String url) {
                            Log.d("title", view.getTitle());
                            // dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                            dialog.show();
                            webview.setVisibility(View.VISIBLE);
                          //  progressBar.setVisibility(View.GONE);
                            cancle.setVisibility(View.VISIBLE);
                            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                            //  pd.dismiss();
                        }


                    });
                    cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });


                }
            });

        } else {
            play.setVisibility(View.GONE);
            try {
                String image = "" + employer_list.get(position);
                if (!image.equals("")) {
                    Glide.with(context)
                            .load(employer_list.get(position).getImagethumb320())
                            .placeholder(R.drawable.loadinguni)
                            .error(R.drawable.loadinguni)
                            .into(postimage);
                    postimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, Gridviewpostpereveiw.class);
                            intent.putExtra("image", employer_list.get(position).getImage_web_views_url());
                            context.startActivity(intent);
                        }
                    });
                } else {
                    postimage.setBackgroundResource(R.drawable.loadinguni);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        return convertView;
    }


}
