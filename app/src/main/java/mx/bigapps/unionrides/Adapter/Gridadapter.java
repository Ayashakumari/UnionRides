package mx.bigapps.unionrides.Adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import mx.bigapps.unionrides.Activity.Gridviewpostpereveiw;
import mx.bigapps.unionrides.Activity.SquareImageView;
import mx.bigapps.unionrides.Model.UserPosts;
import mx.bigapps.unionrides.R;

/**
 * Created by admin on 26-06-2018.
 */


public class Gridadapter extends RecyclerView.Adapter<Gridadapter.ItemViewHolder> {

    Context context;
    Activity activity;

    ArrayList<UserPosts> employer_list = new ArrayList<UserPosts>();
    int mCurrentPlayingPosition = -1;
    LayoutInflater mInflater;

    public Gridadapter(Context context, ArrayList<UserPosts> employer_list) {
        this.context = context;
        this.employer_list = employer_list;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView play;
        SquareImageView postimage;
        ProgressBar progressBar;

        public ItemViewHolder(View itemView) {
            super(itemView);
            play = (ImageView) itemView.findViewById(R.id.play);
            postimage = (SquareImageView) itemView.findViewById(R.id.postimage);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {
        //  itemViewHolder.txt_status.setText(balance_list.get(position));
        if (employer_list.get(position).getPosttype().equals("video")) {
            itemViewHolder.play.setVisibility(View.VISIBLE);
            try {
                String image = "" + employer_list.get(position);
                if (!image.equals("")) {
                    Picasso.with(context)
                            .load(employer_list.get(position).getVideoThumb())
                            .noFade()
                            .placeholder(R.drawable.loadinguni)

                            .error(R.drawable.loadinguni)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(itemViewHolder.postimage);

                    //  UrlImageViewHelper.setUrlDrawable(holder.find_people_img,find_peoplelist.get(position).getImage());
                } else {
                    itemViewHolder.postimage.setBackgroundResource(R.drawable.loadinguni);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            itemViewHolder.postimage.setOnClickListener(new View.OnClickListener() {
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
            itemViewHolder.play.setVisibility(View.GONE);
            try {
                String image = "" + employer_list.get(position);
                if (!image.equals("")) {
                    Glide.with(context)
                            .load(employer_list.get(position).getImagethumb320())
                            .placeholder(R.drawable.loadinguni)
                            .error(R.drawable.loadinguni)
                            .into(itemViewHolder.postimage);
                    itemViewHolder.postimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WebView view = new WebView(context);
                            view.loadUrl(employer_list.get(position).getVideo_web_views_url());

                            final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                            dialog.setContentView(R.layout.activity_videoview);
                            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                            final WebView webview = (WebView) dialog.findViewById(R.id.webview);

                            final ImageView cancle = (ImageView) dialog.findViewById(R.id.cancle);

                            final ProgressBar progress = (ProgressBar) dialog.findViewById(R.id.progress);
                            dialog.show();
                            webview.getSettings().setJavaScriptEnabled(true);
                            webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                            webview.getSettings().setLoadWithOverviewMode(true);
                            webview.getSettings().setUseWideViewPort(true);
                            webview.setWebViewClient(new WebViewClient());
                            webview.getSettings().setJavaScriptEnabled(true);
                            webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                            webview.getSettings().setDomStorageEnabled(true);
                            webview.getSettings().setAppCacheEnabled(true);

                            webview.getSettings().setAppCachePath(context.getFilesDir().getAbsolutePath() + "/cache");
                            webview.getSettings().setDatabaseEnabled(true);
                            webview.getSettings().setDatabasePath(context.getFilesDir().getAbsolutePath() + "/databases");
                            webview.getSettings().setPluginState(WebSettings.PluginState.ON);
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
                            webview.loadUrl(employer_list.get(position).getImage_web_views_url());
                            webview.getSettings().setAllowFileAccess(true);
                            webview.setWebChromeClient(new WebChromeClient());
                            webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

                            webview.getSettings().setJavaScriptEnabled(true);
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
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    // TODO Auto-generated method stub
                                    progress.setVisibility(View.VISIBLE);
                                    webview.setVisibility(View.VISIBLE);

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
                                    dialog.show();
                                    webview.setVisibility(View.VISIBLE);

                                    cancle.setVisibility(View.VISIBLE);
                                    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                                    //  pd.dismiss();
                                }


                            });
                            cancle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    webview.clearCache(true);
                                   notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                           /* Intent intent = new Intent(context, Gridviewpostpereveiw.class);
                            intent.putExtra("image", employer_list.get(position).getImage_web_views_url());
                            context.startActivity(intent);*/
                        }
                    });
                } else {
                    itemViewHolder.postimage.setBackgroundResource(R.drawable.loadinguni);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public int getItemCount() {
        return employer_list.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userpostgrid, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);
        return viewHolder;
    }


}

