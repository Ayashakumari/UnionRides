package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
//import mx.bigapps.unionrides.Activity.Add_Comment;
import mx.bigapps.unionrides.Activity.Gridviewpostpereveiw;
import mx.bigapps.unionrides.Activity.PostPreviewimage;
import mx.bigapps.unionrides.Activity.PublicProfile;
import mx.bigapps.unionrides.Activity.Sponserview;
import mx.bigapps.unionrides.Activity.SquareImageView;
import mx.bigapps.unionrides.Activity.StretchVideoView;
import mx.bigapps.unionrides.Activity.Wall;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Fragment.Posts;
import mx.bigapps.unionrides.Fragment.PostsWall;
import mx.bigapps.unionrides.Model.Comments;
import mx.bigapps.unionrides.Model.UserPosts;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;

import static android.net.http.SslError.SSL_UNTRUSTED;


/**
 * Created by seemtech2 on 05-10-2017.
 */

public class UserPost_Adapter extends RecyclerView.Adapter<UserPost_Adapter.ItemViewHolder> {

    Context context;
    Activity activity;


    ArrayList<UserPosts> employer_list = new ArrayList<UserPosts>();
    int mCurrentPlayingPosition = -1;
    ImageView comments_back;
    ImageView comment_send;
    EditText chat_edtext;
    String  position;
    Comments comments;
    ArrayList<Comments> commentsArrayList;
    RecyclerView recyclerView;
    TextView nocomments;
    PostsWall postsWall = new PostsWall();
    Posts posts=new Posts();

    public UserPost_Adapter(Context context, ArrayList<UserPosts> employer_list) {
        this.context = context;
        this.employer_list = employer_list;
        this.activity = (Activity) context;

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView fanname, onlinetime, likecount, comntcount, posted_time, commentby, comments, postcaption, gotonext;
        CircleImageView profileimg;
        ImageView edit, share, flag, play, like;
        StretchVideoView videoView;
        LinearLayout commnt, commentlayout;
        SquareImageView postimage;
        ProgressBar progress;


        public ItemViewHolder(View itemView) {
            super(itemView);
            progress = (ProgressBar) itemView.findViewById(R.id.progress);

            postcaption = (TextView) itemView.findViewById(R.id.postcaption);
            comments = (TextView) itemView.findViewById(R.id.comments);
            commentby = (TextView) itemView.findViewById(R.id.commentby);
            play = (ImageView) itemView.findViewById(R.id.play);
            posted_time = (TextView) itemView.findViewById(R.id.posted_time);
            fanname = (TextView) itemView.findViewById(R.id.name);
            videoView = (StretchVideoView) itemView.findViewById(R.id.videos);
            likecount = (TextView) itemView.findViewById(R.id.likecount);
            comntcount = (TextView) itemView.findViewById(R.id.comntcount);
            edit = (ImageView) itemView.findViewById(R.id.edit);
            postimage = (SquareImageView) itemView.findViewById(R.id.postimage);
            share = (ImageView) itemView.findViewById(R.id.share);
            flag = (ImageView) itemView.findViewById(R.id.flag);
            profileimg = (CircleImageView) itemView.findViewById(R.id.profile_img);
            onlinetime = (TextView) itemView.findViewById(R.id.onlinetime);
            commnt = (LinearLayout) itemView.findViewById(R.id.commnt);
            commentlayout = (LinearLayout) itemView.findViewById(R.id.commentlayout);
            like = (ImageView) itemView.findViewById(R.id.light);
            gotonext = (TextView) itemView.findViewById(R.id.gotonext);


        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {

        itemViewHolder.fanname.setText(employer_list.get(position).getPosted_by().toString());
        itemViewHolder.postcaption.setText(employer_list.get(position).getPostcaption().toString());
        itemViewHolder.comntcount.setText(employer_list.get(position).getCommentCount() + " Comments");
        if (employer_list.get(position).getlikeCount().equals("0")) {
            itemViewHolder.likecount.setText(employer_list.get(position).getlikeCount() + " Like");
        } else if (employer_list.get(position).getlikeCount().equals("1")) {
            itemViewHolder.likecount.setText(employer_list.get(position).getlikeCount() + " Like");
        } else {
            itemViewHolder.likecount.setText(employer_list.get(position).getlikeCount() + " Likes");
        }
        itemViewHolder.posted_time.setText(employer_list.get(position).getPosted_time());
        itemViewHolder.onlinetime.setText(employer_list.get(position).getPosted_time());

        if (employer_list.get(position).getPostfrom().equals("sponsor")) {
            itemViewHolder.gotonext.setVisibility(View.VISIBLE);

        } else {
            itemViewHolder.gotonext.setVisibility(View.GONE);
        }
        itemViewHolder.fanname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employer_list.get(position).getPostfrom().equals("sponsor")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(employer_list.get(position).getData().toString()));
                    context.startActivity(browserIntent);


                } else {

                }
            }
        });
        itemViewHolder.gotonext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employer_list.get(position).getPostfrom().equals("sponsor")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(employer_list.get(position).getData().toString()));
                    context.startActivity(browserIntent);
                } else {
                  /*  Intent intent = new Intent(context, PostPreviewimage.class);
                    intent.putExtra("list", "list");
                    intent.putExtra("image", employer_list.get(position).getData());
                    context.startActivity(intent);*/
                    Intent intent = new Intent(context, Gridviewpostpereveiw.class);
                    intent.putExtra("image", employer_list.get(position).getImage_web_views_url());
                    context.startActivity(intent);
                }

            }
        });
        // itemViewHolder.postimage.setLayoutParams(new RelativeLayout.LayoutParams(itemViewHolder.postimage.getMeasuredWidth(), itemViewHolder.postimage.getMeasuredWidth()));


       /* if (employer_list.get(position).getPosttype().equals("video")) {

        } else {
            try {
                URL url = new URL(employer_list.get(position).getData());
                Log.d("imagehight", String.valueOf(url));

                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();

                connection.setDoInput(true);
                connection.connect();

                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                Log.d("imagehight", String.valueOf(bitmap));

            } catch (IOException e) {
                e.printStackTrace();

            }
            int w = bitmap.getWidth();
            h = bitmap.getHeight();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            devicewidth = displayMetrics.widthPixels;
            if (h > w) {
                orignalhight = h / w * devicewidth;
            } else {
                orignalhight = w / h * devicewidth;
            }

            itemViewHolder.postimage.getLayoutParams().height = orignalhight;
            Log.d("imagehight", w + "imagewidht" + h + "imagegight" + orignalhight);
            itemViewHolder.postimage.requestLayout();
        }*/
        if (employer_list.get(position).getPosttype().equals("video")) {
            itemViewHolder.play.setVisibility(View.VISIBLE);
            try {
                String image = "" + employer_list.get(position).getVideoThumb();
                if (!image.equals("")) {
                    Picasso.with(context)
                            .load(employer_list.get(position).getVideoThumb())
                            .noFade()
                            //  .resize(orignalhight, devicewidth)
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
                    webview.loadUrl(employer_list.get(position).getVideo_web_views_url());
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
                            return super.shouldOverrideUrlLoading(view, url);

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
                            // itemViewHolder.play.setVisibility(View.VISIBLE);
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

                    Picasso.with(context)
                            .load(employer_list.get(position).getImagethumb640())
                            .placeholder(R.drawable.loadinguni)
                            .placeholder(R.drawable.loadinguni) // optional
                            .error(R.drawable.loadinguni)

                            .into(itemViewHolder.postimage);


                    itemViewHolder.postimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (employer_list.get(position).getPostfrom().equals("sponsor")) {
                                Log.d("postdes", employer_list.get(position).getPostcaption().toString());
                                Intent intent = new Intent(context, PostPreviewimage.class);
                                intent.putExtra("list", "list");
                                intent.putExtra("image", employer_list.get(position).getSponserimage());
                                context.startActivity(intent);

                            } else {
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
                                        // webview.setVisibility(View.VISIBLE);

                                        view.loadUrl(url);
                                        return true;

                                    }

                                    @Override
                                    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                                        Log.d("error", error.toString());
                                       /* final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("notification error ssl certificate invalid");
                                        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                handler.proceed();
                                            }
                                        });
                                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                handler.cancel();
                                            }
                                        });
                                        final AlertDialog dialog = builder.create();
                                        dialog.show();*/// Ignore SSL certificate errors
                                        SslCertificate serverCertificate = error.getCertificate();


                                        // Check if Cert-Domain equals the Uri-Domain
                                        String certDomain = serverCertificate.getIssuedTo().getCName();
                                        Log.d("error", certDomain.toString());

                                        if (certDomain.equals("*.unionrides.com")) {
                                            handler.proceed();
                                        }


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
                                        // itemViewHolder.play.setVisibility(View.VISIBLE);
                                        dialog.dismiss();
                                    }
                                });


                                // }

                              /*  Intent intent = new Intent(context, Gridviewpostpereveiw.class);
                                Log.d("image", employer_list.get(position).getImage_web_views_url());
                                intent.putExtra("image", employer_list.get(position).getImage_web_views_url());
                                context.startActivity(intent);*/

                            }
                        }
                    });
                } else {
                    itemViewHolder.postimage.setBackgroundResource(R.drawable.loadinguni);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if (employer_list.get(position).getComments().equals("")) {
            itemViewHolder.commentlayout.setVisibility(View.GONE);
        } else {
            itemViewHolder.commentlayout.setVisibility(View.VISIBLE);
            itemViewHolder.commentby.setText(employer_list.get(position).getCommentsende() + ":");
            itemViewHolder.comments.setText(employer_list.get(position).getComments());
            itemViewHolder.onlinetime.setText(employer_list.get(position).getCmntdatetime());
        }
        Picasso.with(context)
                .load(employer_list.get(position).getProfile_image())
                .noFade()
                // .resize(orignalhight, devicewidth)
                .placeholder(R.drawable.loadinguni)
                .error(R.drawable.default_user)
                .into(itemViewHolder.profileimg);
        if (employer_list.get(position).getMyPost().equals("yes")) {
            itemViewHolder.edit.setVisibility(View.VISIBLE);
            itemViewHolder.flag.setEnabled(false);
            itemViewHolder.flag.setVisibility(View.GONE);
            /* itemViewHolder.flag.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
             */
        } else {
            itemViewHolder.edit.setVisibility(View.GONE);
            itemViewHolder.flag.setVisibility(View.VISIBLE);
            itemViewHolder.flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                    alertDialogBuilder.setMessage("Would you like to report this post");
                    alertDialogBuilder.setPositiveButton("Report", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {

                            reportPost(employer_list.get(position).getPostid());
                            dialog1.cancel();

                        }

                    });

                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int which) {
                            dialog1.cancel();

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }
        if (employer_list.get(position).getlikeCount().equals("0")) {
            itemViewHolder.like.setImageResource(R.drawable.ic_g_light_inactive);
        } else {
            itemViewHolder.like.setImageResource(R.drawable.icglight);
        }

        itemViewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost(employer_list.get(position).getPostid(), position);


            }


        });
        itemViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                alertDialogBuilder.setMessage("Would you like to delete this post");
                alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {

                        deleteownPost(employer_list.get(position).getPostid());
                        employer_list.remove(position);
                        dialog1.cancel();

                    }

                });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.cancel();

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        itemViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                alertDialogBuilder.setMessage("Would you like to share this post");
                alertDialogBuilder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {

                        sharePost(employer_list.get(position).getPostid());
                        notifyDataSetChanged();
                        dialog1.cancel();

                    }

                });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.cancel();

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        itemViewHolder.profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employer_list.get(position).getPostfrom().equals("sponsor")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(employer_list.get(position).getData().toString()));
                    context.startActivity(browserIntent);


                } else {
                    if (employer_list.get(position).getUserid().equals(PrefMangr.getInstance().getUserId())) {
                        Intent intent = new Intent(context, Wall.class);
                        intent.putExtra("flag", 5);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, PublicProfile.class);
                        helper.user_id = employer_list.get(position).getUserid();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);
                    }
                }
            }
        });
        itemViewHolder.commnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.add_comment);
                nocomments = (TextView) dialog.findViewById(R.id.nocomments);
                comments_back = (ImageView) dialog.findViewById(R.id.comments_back);
                comment_send = (ImageView) dialog.findViewById(R.id.comment_send);
                chat_edtext = (EditText) dialog.findViewById(R.id.chat_edtext);

                recyclerView = (RecyclerView) dialog.findViewById(R.id.messagesContainer);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                showpostcommentlist(employer_list.get(position).getPostid());
                comments_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                comment_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postComments(employer_list.get(position).getPostid(), position);

                    }
                });
                dialog.show();

            }
        });
    }

    private void showpostcommentlist(final String postId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        JSONObject obj = null;
                        String imagethumb;
                        commentsArrayList = new ArrayList<>();
                        Log.d("showpostcommentlist", response.toString());
                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jsonArray = obj.getJSONArray("users");
                            if (jsonArray.length() == 0) {
                                nocomments.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jarray = jsonArray.getJSONObject(i);
                                    String user_id = jarray.getString("user_id");
                                    String fullname = jarray.getString("fullname");
                                    String profileimage = jarray.getString("profileimage");
                                    String comment_id = jarray.getString("comment_id");
                                    String datetime = jarray.getString("datetime");
                                    String comment = jarray.getString("comments");
                                    JSONArray profile_image_thumb_size = jarray.getJSONArray("profile_image_thumb_size");
                                    if (profile_image_thumb_size.length() != 0) {
                                        JSONObject jarrayvideo = profile_image_thumb_size.getJSONObject(0);
                                        imagethumb = jarrayvideo.getString("80_80");
                                    } else {
                                        imagethumb = null;
                                    }
                                    comments = new Comments(fullname, profileimage, comment_id, comment, datetime);
                                    commentsArrayList.add(comments);
//                                    Comment_Adapter comment_adapter = new Comment_Adapter(context, commentsArrayList);
//                                    recyclerView.setAdapter(comment_adapter);
                                    nocomments.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                              /*  userPost_adapter = new UserPost_Adapter(getActivity(), userPostsArrayList);
                                userPost.setAdapter(userPost_adapter);
                                userPost.smoothScrollToPosition(userPostsArrayList.size() - 1);
*/

                                }
                            }
                        } catch (JSONException e1) {

                            e1.printStackTrace();
                        }


                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "showpostcommentlist");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("post_id", postId);
                Log.d("showpostcommentlist", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    private void postComments(final String postId, final int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        JSONObject obj = null;
                        Log.d("Postcomment", response.toString());
                        chat_edtext.setText("");
                        showpostcommentlist(postId);
                        try {
                            obj = new JSONObject(response.toString());
                            String comment_count = obj.getString("comment_count");
                            employer_list.get(position).setCommentCount(comment_count);
                            notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Postcomment");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("post_id", postId);
                params.put("comment", chat_edtext.getText().toString());
                Log.d("Postcomment", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    private void likePost(final String postid, final int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        JSONObject obj = null;
                        Log.d("Postlike", response.toString());
                        try {
                            obj = new JSONObject(response.toString());
                            msg = obj.getString("msg");
                            // Posts.repost(context, position);

                            if (employer_list.get(position).getLike_status().equals("no")) {
                                int like = (Integer.parseInt(employer_list.get(position).getlikeCount()) + 1);
                                employer_list.get(position).setlikeCount(String.valueOf(like));
                                employer_list.get(position).setLike_status("yes");
                            } else {
                                employer_list.get(position).setLike_status("no");
                                employer_list.get(position).setlikeCount(String.valueOf(Integer.parseInt(employer_list.get(position).getlikeCount()) - 1));
                            }
                            notifyDataSetChanged();
                        } catch (JSONException e1) {

                            e1.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Postlike");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("post_id", postid);
                Log.d("Postlike", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    private void reportPost(final String postid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        JSONObject obj = null;
                        Log.d("Postreport", response.toString());
                        try {
                            obj = new JSONObject(response.toString());
                            msg = obj.getString("msg");
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            if (msg.equals("Report successfully.")) {
                                alertDialogBuilder.setMessage("Flag Report sent successfully");
                            } else {
                                alertDialogBuilder.setMessage("You have already reported this post.");
                            }

                            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {

                                    notifyDataSetChanged();

                                }

                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();


                        } catch (JSONException e1) {

                            e1.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Postreport");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("post_id", postid);
                Log.d("Postreport", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    private void sharePost(final String postid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        JSONObject obj = null;
                        Log.d("Sharepost", response.toString());
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setMessage("Post Shared Successfully");
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {

                                posts.repost(context, 0);
                                postsWall.repost(context, 0);
                                dialog1.dismiss();

                            }

                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Sharepost");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("post_id", postid);
                Log.d("Sharepost", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    private void deleteownPost(final String postid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        JSONObject obj = null;
                        Log.d("Showwallpost", response.toString());
                        try {
                            obj = new JSONObject(response.toString());
                            // JSONArray jsonArray = obj.getJSONArray("users");
                            Log.d("Deleteownpost", response.toString());
                            msg = obj.getString("msg");
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                            if (msg.equals("This post is not exist")) {
                                alertDialogBuilder.setMessage("you can't delete others post");
                            } else {
                                alertDialogBuilder.setMessage(msg);
                            }
                            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    // Posts.repost(context, 0);
                                    postsWall.repost(context, 0);
                                    notifyDataSetChanged();

                                }

                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();


                        } catch (JSONException e1) {

                            e1.printStackTrace();
                        }
                    }
                }
                ,
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }

        )

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Deleteownpost");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("post_id", postid);
                Log.d("Showwallpost", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    @Override
    public int getItemCount() {
        return employer_list.size();
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userpost, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);

        return viewHolder;
    }

}
