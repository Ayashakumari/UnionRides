package mx.bigapps.unionrides.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Activity.PostPreviewimage;
import mx.bigapps.unionrides.Activity.Rides;
import mx.bigapps.unionrides.Activity.SquareImageView;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.Added_Image_Entity;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;


/**
 * Created by seemtech2 on 05-10-2017.
 */

public class RiderPhotos_Adapter extends BaseAdapter {

    Context context;
    Activity activity;

    ArrayList<Added_Image_Entity> employer_list = new ArrayList<Added_Image_Entity>();
    int mCurrentPlayingPosition = -1;

    AdapterView.OnItemClickListener onItemClickListener;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String msg, pic_id, file_type;
    LayoutInflater mInflater;

    public RiderPhotos_Adapter(Context context, ArrayList<Added_Image_Entity> employer_list) {
        this.context = context;
        this.employer_list = employer_list;
        this.onItemClickListener = onItemClickListener;
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.rowreiderphoto, null);
        }
        ImageView image_square = (ImageView) convertView.findViewById(R.id.image_square);
//        SquareImageView image = (SquareImageView) convertView.findViewById(R.id.image_square);
        ImageView play = (ImageView) convertView.findViewById(R.id.play);
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progress);
        if (employer_list.get(position).getFile_type().equals("image")) {
            play.setVisibility(View.GONE);
        } else {
            play.setVisibility(View.VISIBLE);

        }
        image_square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (employer_list.get(position).getFile_type().equals("image")) {
                    Intent intent = new Intent(context, PostPreviewimage.class);
                    intent.putExtra("list", "list");
                    intent.putExtra("image", employer_list.get(position).getFile_url());
                    context.startActivity(intent);

                } else {
                 //   progressBar.setVisibility(View.VISIBLE);
                    final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                    dialog.setContentView(R.layout.activity_videoview);
                    // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

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
                           // progressBar.setVisibility(View.GONE);
                            cancle.setVisibility(View.VISIBLE);
                            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                            //  pd.dismiss();
                        }


                    });
                    cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });


                }


                   /* Intent intent = new Intent(context, Web.class);
                    intent.putExtra("video", employer_list.get(position).getFile_url());
                    context.startActivity(intent);*/

            }
        });
        if (employer_list.get(position).getProfile_status().equals("0")) {
            image_square.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_custom);

                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();
                    RelativeLayout takepicture = (RelativeLayout) dialog.findViewById(R.id.gallery_rl);
                    TextView uploadphoto = (TextView) dialog.findViewById(R.id.uploadphoto);
                    RelativeLayout uploadfile = (RelativeLayout) dialog.findViewById(R.id.camera_rl);
                    TextView camera_txt = (TextView) dialog.findViewById(R.id.camera_txt4);
                    RelativeLayout cancel_rl = (RelativeLayout) dialog.findViewById(R.id.cancel_rl);
                    uploadphoto.setText("Do you want to delete this " + employer_list.get(position).getFile_type() + "?");
                    TextView gallery_txt = (TextView) dialog.findViewById(R.id.gallery_txt4);
                    gallery_txt.setText("Yes");
                    camera_txt.setText("NO");
                    takepicture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pic_id = employer_list.get(position).getId();
                            file_type = employer_list.get(position).getFile_type();
                            if (employer_list.get(position).getFile_type().equals("image")) {
                                helper.riderfrag = "0";
                            } else {
                                helper.riderfrag = "1";
                            }
                            dialog.dismiss();
                            makeStringReq();
                        }
                    });
                    uploadfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();

                        }
                    });
                    cancel_rl.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });

                    return false;
                }
            });
        }

        if (employer_list.get(position).getFile_type().equals("image")) {
            Glide.with(context)
                    .load(employer_list.get(position).getImage320())
                    .error(R.drawable.loadinguni)
                    .placeholder(R.drawable.loadinguni)
                    .override(320, 320)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(image_square);
        } else {
            Glide.with(context)
                    .load(employer_list.get(position).getVideo_thumbnail())
                    .error(R.drawable.loadinguni)
                    .placeholder(R.drawable.loading)
                    .override(320, 320)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(image_square);
        }

        return convertView;
    }


/*
    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {
        final int pos = position;
        final Added_Image_Entity entity = employer_list.get(pos);

        itemViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (employer_list.get(position).getFile_type().equals("image")) {
                    onClickShowPic a = (onClickShowPic) new RidesPhotos();

                    a.showPicUrl(employer_list.get(position).getImage320());
                    // notifyDataSetChanged();
                } else {
                    onClickVideo video = (onClickVideo) new VideoRides();
                    video.showVideoUrl(employer_list.get(position).getVideo_thumbnail(), employer_list.get(position).getImage320());
                }
            }
        });
        if (employer_list.get(position).getProfile_status().equals("0")) {
            itemViewHolder.image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_custom);

                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();
                    RelativeLayout takepicture = (RelativeLayout) dialog.findViewById(R.id.gallery_rl);
                    TextView uploadphoto = (TextView) dialog.findViewById(R.id.uploadphoto);
                    RelativeLayout uploadfile = (RelativeLayout) dialog.findViewById(R.id.camera_rl);
                    TextView camera_txt = (TextView) dialog.findViewById(R.id.camera_txt);
                    RelativeLayout cancel_rl = (RelativeLayout) dialog.findViewById(R.id.cancel_rl);
                    uploadphoto.setText("Do you want to delete this " + employer_list.get(position).getFile_type() + "?");
                    TextView gallery_txt = (TextView) dialog.findViewById(R.id.gallery_txt);
                    gallery_txt.setText("Yes");
                    camera_txt.setText("NO");
                    takepicture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pic_id = employer_list.get(position).getId();
                            file_type = employer_list.get(position).getFile_type();
                            dialog.dismiss();
                            makeStringReq();
                        }
                    });
                    uploadfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();

                        }
                    });
                    cancel_rl.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });

                    return false;
                }
            });
        }

        if (employer_list.get(position).getFile_type().equals("image")) {
            Glide.with(context)
                    .load(employer_list.get(position).getImage320())
                    .error(R.drawable.loadinguni)
                    .placeholder(R.drawable.loadinguni)

                    .into(itemViewHolder.image);
        } else {
            Glide.with(context)
                    .load(employer_list.get(position).getVideo_thumbnail())
                    .error(R.drawable.loadinguni)
                    .placeholder(R.drawable.loading)

                    .into(itemViewHolder.image);
        }

    }

    @Override
    public int getItemCount() {
        return employer_list.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowreiderphoto, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);

        // viewHolder.image.setImageBitmap(employer_list.get());


        return viewHolder;
    }*/

    Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case SHOW_PROG_DIALOG:
                    showProgDialog();
                    break;

                case HIDE_PROG_DIALOG:
                    hideProgDialog();
                    break;

                case LOAD_QUESTION_SUCCESS:

                    break;

                default:
                    break;
            }

            return false;
        }

    });

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(context);
            }
            progress_dialog.setMessage(progress_dialog_msg);
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // hide progress
    private void hideProgDialog() {
        try {
            if (progress_dialog != null && progress_dialog.isShowing())
                progress_dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void makeStringReq() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "loading...";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                Log.d("Json Data", response.toString().substring(start1, end));
                            }
                            msg = obj.getString("msg");

                            if (msg.equals("Deleted successfully")) {


                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(context);
                                }
                                alert.setTitle("Successfull");
                                alert.setMessage("Deleted successfully");


                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
                                        dev=preferences2.edit();

                                        dev.putString("email_txt",email_login);
                                        dev.putString("password_txt",password_login);
                                        dev.commit();
*/
                                        Intent intent = new Intent(context, Rides.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        intent.putExtra("flag", 0);
                                        intent.putExtra("cover_id", PrefMangr.getInstance().getCoverPicID());
                                        context.startActivity(intent);
                                        dialog.dismiss();

                                    }
                                });


                                try {
                                    Dialog dialog = alert.create();
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(context);
                                }
                                alert.setTitle("Alert!");
                                alert.setMessage("Some thing Went Wrong");


                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
                                        dev=preferences2.edit();

                                        dev.putString("email_txt",email_login);
                                        dev.putString("password_txt",password_login);
                                        dev.commit();
*/
                                        dialog.dismiss();

                                    }
                                });


                                try {
                                    Dialog dialog = alert.create();
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


//*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(context, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(context, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show();
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            //TODO

                        } else if (error instanceof ParseError) {

//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Delete_image_video_of_ride");


                params.put("user_id", PrefMangr.getInstance().getUserId());

                // params.put("cover_photo_id", PrefMangr.getInstance().getCoverPicID());
                params.put("id", pic_id);
                params.put("file_type", file_type);


                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(stringRequest,
//                tag_string_req);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

}
