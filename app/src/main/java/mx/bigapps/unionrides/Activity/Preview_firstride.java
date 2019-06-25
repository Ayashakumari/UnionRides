package mx.bigapps.unionrides.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.halilibo.bettervideoplayer.BetterVideoPlayer;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Adapter.CommentAdapter;
import mx.bigapps.unionrides.Adapter.PreviewAdapter;
import mx.bigapps.unionrides.Adapter.Rides_pic_adapter;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.Cover_Photo_List;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;

import static mx.bigapps.unionrides.Activity.RidesFragmnts.cover_photo_lists;

public class Preview_firstride extends AppCompatActivity {

    ImageView headerlogo;
    PreviewAdapter previewadapter;



    RecyclerView recyclerthree;
    LinearLayoutManager layoutManager;

    static Context context;
    private ProgressDialog progress_dialog;

    Handler mHandler;

    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String imageurl, position;
    int page = 0, curSize;
//    ArrayList<Cover_Photo_List> cover_photo = new ArrayList<>();
    ArrayList <Cover_Photo_List> cover_photo=new ArrayList<>();
    ArrayList<Cover_Photo_List> cover_photo1=new ArrayList<>();


    WebView videoview;
    Zoom img;
    ProgressBar progressBar;
    Intent intent;
    String video, image, list;
    ImageView button_Close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview_firstride);

        final Intent intent = getIntent();

        image = intent.getStringExtra("image");


        button_Close = findViewById(R.id.button_Close);
        img = (Zoom) findViewById(R.id.pager);
        img.setMaxZoom(4f);
        recyclerthree = findViewById(R.id.recycler_three);



        LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerthree.setLayoutManager(manager);
       /* previewadapter = new PreviewAdapter(Preview_firstride.this,RidesFragmnts.cover_photo_lists);
        recyclerthree.setAdapter(previewadapter);*/

        makeStringReq();

        button_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }

        });

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
    }

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
//        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
//        progress_dialog_msg = "loading...";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Show_ride_list", response.toString());
                        Log.d("pos",""+helper.pos);

//                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
//                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("result");

                            for (int i=helper.pos;i<jsonArray.length();i++) {
                                Cover_Photo_List photo_entity = new Cover_Photo_List();

                                 JSONObject object = jsonArray.getJSONObject(i);
                                photo_entity.setCover_photo_id(object.getString("ride_id"));
                                photo_entity.setCover_photo(object.getString("640_640"));
                                photo_entity.setUser_id(object.getString("user_id"));
                                photo_entity.setDatetime(object.getString("datetime"));
                                photo_entity.setMainPic(object.getString("photo_url"));
                                photo_entity.setDescription(object.getString("description"));
                                photo_entity.setPosted_by(object.getString("posted_by"));
                                photo_entity.setProfile_picture(object.getString("profile_picture"));


                                JSONArray jsonsArray = object.getJSONArray("comment");
                                   for (int j=1;j<jsonsArray.length();j++) {

                                    JSONObject objects = jsonsArray.getJSONObject(j);
                                       photo_entity.setComment_id(objects.getString("comment_id"));
                                       photo_entity.setComments(objects.getString("comments"));

                                       photo_entity.setRide_id(objects.getString("ride_id"));
                                       photo_entity.setComment_by(objects.getString("comment_by"));
                                       cover_photo1.add(photo_entity);
                                }

                                if (helper.status.equals("myprofile_ride")) {
                                    photo_entity.setClass_status("1");
                                } else if (helper.status.equals("otherprofile_ride")) {
                                    photo_entity.setClass_status("2");
                                } else {
                                    photo_entity.setClass_status("0");
                                }
                                    cover_photo1.add(photo_entity);
                            }

                           // previewadapter = new PreviewAdapter(Preview_firstride.this, cover_photo_lists);
                          //  recyclerthree.setAdapter(previewadapter);

                           if (page == 0) {
                               curSize = 0;
                               Log.d("6666", "66666");
                               cover_photo.addAll(cover_photo1);
                            }

                            previewadapter= new PreviewAdapter(Preview_firstride.this,cover_photo);
                            recyclerthree.setAdapter(previewadapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        catch (Exception e) {
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

                params.put("action", "Show_ride_list");
                params.put("user_id", PrefMangr.getInstance().getUserId());
//                    params.put("comment",typecomments.getText().toString());

                Log.d("Show_ride_list", params.toString());
                return params;


            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(Preview_firstride.this);
        requestQueue.add(stringRequest);
    }

}







