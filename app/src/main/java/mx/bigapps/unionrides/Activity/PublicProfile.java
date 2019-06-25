package mx.bigapps.unionrides.Activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Fragment.Fans2;
import mx.bigapps.unionrides.Fragment.Myevents;
import mx.bigapps.unionrides.Fragment.Posts;
import mx.bigapps.unionrides.Fragment.PostsWall;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.Adapter.ItemAdapter;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.Commom_Method;
import mx.bigapps.unionrides.utils.helper;

public class PublicProfile extends AppCompatActivity {
    ImageView camera, video, events, header_ride, header_logo;
    TextView events_txt, post_txt, txtrides;
    LinearLayout linearLayoutfollowing;
    TextView txtfollowing;
    LinearLayout llride, llpost, llevents;
    ImageView edit;
    LinearLayout commnt;
    ImageView light;
    TextView likecount;
    int like = 6;
    RelativeLayout profilelayout;
    Fragment fragment = null;
    FrameLayout frameLayout;
    ImageView post, evnts, tick;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    JSONObject jobj;
    String msg;
    int flagValue = 0;
    String user_id, fullname, nickname, email, password, dob, verify_code, is_verified, gmt_value, profileimage, type, login_status, device_id, admin, gpsCount, active_status, added_date, friend_count,
            friend_status, All_post_count, friend_request_count, user_type, following_count, follow_status, user_status, request_type;
    Activity activity;
    ImageView profile_img;
    TextView profile_name, following_count_txt, post_count_txt, fan_count_txt;
    Intent intents;
    int flag = 0;
    LinearLayout llfans;
    TextView no6;
     TextView no5;
     TextView no4;
    TextView no3;
     TextView no2;
     TextView no1;
    TextView no;
    LinearLayout llgrid;
    ImageView grid;
    String block_status;
    BottomSheetBehavior behavior;
    RecyclerView recyclerView;
    ItemAdapter mAdapter;
    PostsWall postsWall=new PostsWall();
    Posts posts=new Posts();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);
        grid = (ImageView) findViewById(R.id.grid);
        no6 = (TextView) findViewById(R.id.no6);
        no5 = (TextView) findViewById(R.id.no5);
        no4 = (TextView) findViewById(R.id.no4);
        no3 = (TextView) findViewById(R.id.no3);
        no2 = (TextView) findViewById(R.id.no2);
        no1 = (TextView) findViewById(R.id.no1);
        no = (TextView) findViewById(R.id.no);
        tick = (ImageView) findViewById(R.id.tick);
        activity = PublicProfile.this;
        intents = getIntent();
        flag = intents.getIntExtra("flag", 0);
        llgrid = (LinearLayout) findViewById(R.id.llgrid);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        post_count_txt = (TextView) findViewById(R.id.post_count);
        fan_count_txt = (TextView) findViewById(R.id.fan_count_txt);
        profile_name = (TextView) findViewById(R.id.profile_name);
        following_count_txt = (TextView) findViewById(R.id.following_count_txt);
        profilelayout = (RelativeLayout) findViewById(R.id.profilelayout);
        llpost = (LinearLayout) findViewById(R.id.lppost);
        llevents = (LinearLayout) findViewById(R.id.llevent);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        commnt = (LinearLayout) findViewById(R.id.commnt);
        light = (ImageView) findViewById(R.id.light);
        likecount = (TextView) findViewById(R.id.likecount);

        llride = (LinearLayout) findViewById(R.id.llride);
        llfans = (LinearLayout) findViewById(R.id.llfans);
        linearLayoutfollowing = (LinearLayout) findViewById(R.id.llfollowing);
        txtfollowing = (TextView) findViewById(R.id.txtfollowing);
        edit = (ImageView) findViewById(R.id.edit);
        camera = (ImageView) findViewById(R.id.header_camera);
        camera.setImageResource(R.drawable.back);
        video = (ImageView) findViewById(R.id.header_video);
        video.setVisibility(View.GONE);
        events = (ImageView) findViewById(R.id.header_event);
        events.setImageResource(R.drawable.imagechat);
        events.setVisibility(View.VISIBLE);
        header_ride = (ImageView) findViewById(R.id.ride);
        header_logo = (ImageView) findViewById(R.id.header_logo);
        header_logo.setImageResource(R.drawable.header_logo);
        header_logo.setVisibility(View.VISIBLE);
        post = (ImageView) findViewById(R.id.posts);
        evnts = (ImageView) findViewById(R.id.event);
        events_txt = (TextView) findViewById(R.id.events_txt);
        post_txt = (TextView) findViewById(R.id.post_txt);
        txtrides = (TextView) findViewById(R.id.rides2);
        likecount.setText(String.valueOf(like));
        llfans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.publicProfile = "1";
                Intent intent = new Intent(PublicProfile.this, FriendList.class);
                intent.putExtra("userId", user_id);
                startActivity(intent);
            }
        });
        linearLayoutfollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.publicProfile = "1";
                Intent intent = new Intent(PublicProfile.this, FollowingList.class);
                intent.putExtra("userId", user_id);
                startActivity(intent);
            }
        });
        if (flag == 1) {
            header_ride.setImageResource(R.drawable.ic_rides_active);
            post.setImageResource(R.drawable.profile_post_inactive);
            evnts.setImageResource(R.drawable.profile_ic_event_inactive);
            txtrides.setTextColor(Color.parseColor("#4099FF"));
            post_txt.setTextColor(Color.parseColor("#676767"));
            events_txt.setTextColor(Color.parseColor("#676767"));
            profilelayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            llgrid.setVisibility(View.GONE);
            helper.status = "otherprofile_ride";
            fragment = new RidesFragmnts();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
        } else if (flag == 3) {
            events_txt.setTextColor(Color.parseColor("#4099FF"));
            post_txt.setTextColor(Color.parseColor("#676767"));
            txtrides.setTextColor(Color.parseColor("#676767"));
            header_ride.setImageResource(R.drawable.ic_rides_inactive);
            post.setImageResource(R.drawable.profile_post_inactive);
            evnts.setImageResource(R.drawable.profile_ic_event_active);
            profilelayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            llgrid.setVisibility(View.GONE);
            helper.status = "otherprofile_event";
            fragment = new Myevents();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


            } else {
                Log.e("MainActivity", "Error in creating fragment");

            }
        } else {
            post_txt.setTextColor(Color.parseColor("#4099FF"));
            txtrides.setTextColor(Color.parseColor("#676767"));
            events_txt.setTextColor(Color.parseColor("#676767"));
            profilelayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            helper.status = "otherprofile_fan";
            // Posts.repost(PublicProfile.this, 0);
            fragment = new Posts();
            llgrid.setVisibility(View.VISIBLE);

            //  Posts.repostUser(PublicProfile.this);

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }

        }
        flagValue = 0;
        makeStringReq();
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like = like + 1;
                likecount.setText(String.valueOf(like));
            }
        });
        commnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublicProfile.this, Add_Comment.class);
                startActivity(intent);

            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final BottomSheetDialog alertDialog = new BottomSheetDialog(PublicProfile.this);
                alertDialog.setContentView(R.layout.dialoglayoutblock);
                alertDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView block = (TextView) alertDialog.findViewById(R.id.block);
                TextView sendmeassage = (TextView) alertDialog.findViewById(R.id.sendmeassage);

                if (block_status.equals("Yes")) {
                    block.setText("Unblock");
                    sendmeassage.setVisibility(View.GONE);
                }
                if (block_status.equals("No")) {
                    block.setText("Block");
                    sendmeassage.setVisibility(View.VISIBLE);
                }
                block.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Blockuser();
                        alertDialog.cancel();
                    }
                });
                sendmeassage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PublicProfile.this, Chat_window.class);
                        i.putExtra("firstname", fullname);
                        i.putExtra("friend_id", user_id);
                        // i.putExtra("status", "chat");
                        startActivity(i);
                        alertDialog.cancel();
                    }
                });

                alertDialog.show();
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.status = "";
                if (helper.back_status.equals("search")) {
                    Intent intent = new Intent(PublicProfile.this, Wall.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("flag", 2);
                    startActivity(intent);
                } else if (helper.back_status.equals("comment")) {
                    posts.repost(PublicProfile.this, 0);
                    finish();
                } else {
                    Intent intent = new Intent(PublicProfile.this, Wall.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    postsWall.repost(PublicProfile.this, 0);
                    startActivity(intent);
                }

            }
        });

        txtfollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtfollowing.getText().toString().equals("Fan")) {
                    flagValue = 1;
                    showAlert("Do you Want to Send Fan Request");

                } else if (txtfollowing.getText().toString().equals("Request Sent")) {
                    Toast.makeText(PublicProfile.this, "Your already sent friend request to " + fullname, Toast.LENGTH_SHORT).show();
                } else if (txtfollowing.getText().toString().equals("Unfan")) {
                    flagValue = 2;
                    showAlert("Do you Want to Unfan this user");
                } else if (txtfollowing.getText().toString().equals("Accept Request")) {
                    flagValue = 3;
                    showAlert("Press Accept or Decline to Process this request");
                } else if (txtfollowing.getText().toString().equals("Follow")) {
                    flagValue = 4;
                    showAlert("Do you Want to follow");
                } else if (txtfollowing.getText().toString().equals("Unfollow")) {
                    flagValue = 5;
                    showAlert("Do you Want to unfollow");
                } else {

                }


            }
        });
        llride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(PublicProfile.this, RidesFragmnts.class));
                header_ride.setImageResource(R.drawable.ic_rides_active);
                post.setImageResource(R.drawable.profile_post_inactive);

                evnts.setImageResource(R.drawable.profile_ic_event_inactive);
                txtrides.setTextColor(Color.parseColor("#4099FF"));
                post_txt.setTextColor(Color.parseColor("#676767"));
                events_txt.setTextColor(Color.parseColor("#676767"));
                profilelayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                llgrid.setVisibility(View.GONE);
                helper.status = "otherprofile_ride";
                fragment = new RidesFragmnts();
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });

        llpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header_ride.setImageResource(R.drawable.ic_rides_inactive);
                post.setImageResource(R.drawable.profile_post_active);
                evnts.setImageResource(R.drawable.profile_ic_event_inactive);
                post_txt.setTextColor(Color.parseColor("#4099FF"));
                txtrides.setTextColor(Color.parseColor("#676767"));
                events_txt.setTextColor(Color.parseColor("#676767"));
                profilelayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                helper.status = "otherprofile_fan";
                fragment = new Posts();
                //  Fans.repostUser(PublicProfile.this);
                // Posts.repost(PublicProfile.this, 0);
                llgrid.setVisibility(View.VISIBLE);
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });
        llgrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header_ride.setImageResource(R.drawable.ic_rides_inactive);
                post.setImageResource(R.drawable.profile_post_active);
                evnts.setImageResource(R.drawable.profile_ic_event_inactive);
                post_txt.setTextColor(Color.parseColor("#4099FF"));
                txtrides.setTextColor(Color.parseColor("#676767"));
                events_txt.setTextColor(Color.parseColor("#676767"));
                profilelayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                helper.status = "otherprofile_fan";
                // fragment = new Fans2();
                //  Fans.repostUser(PublicProfile.this);


                if (grid.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.list_n).getConstantState())) {
                    grid.setImageResource(R.drawable.grid_n);
                    fragment = new Posts();
                    //  Fans.repostUser(PublicProfile.this);
                    // Posts.repost(PublicProfile.this, 0);
                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


                    }

                } else if (grid.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.grid_n).getConstantState())) {
                    grid.setImageResource(R.drawable.list_n);
                    // Posts.repost(Wall.this, 0);
                    fragment = new Fans2();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


                    } else {
                        Log.e("MainActivity", "Error in creating fragment");
                    }
                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });
        llevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                events_txt.setTextColor(Color.parseColor("#4099FF"));
                post_txt.setTextColor(Color.parseColor("#676767"));
                txtrides.setTextColor(Color.parseColor("#676767"));
                header_ride.setImageResource(R.drawable.ic_rides_inactive);
                post.setImageResource(R.drawable.profile_post_inactive);
                evnts.setImageResource(R.drawable.profile_ic_event_active);
                profilelayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                llgrid.setVisibility(View.GONE);
                helper.status = "otherprofile_event";
                fragment = new Myevents();
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });

    }

    private void Blockuser() {
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
                                Log.d("Blockuser", response.toString().substring(start1, end));
                            }
                            if (obj.has("msg")) {
                                msg = obj.getString("msg");
                            }
                            if (obj.has("status")) {
                                String status = obj.getString("status");
                                if (status.equals("Unblocked")) {
                                    block_status = "No";

                                }
                                if (status.equals("Blocked")) {
                                    block_status = "Yes";
                                }
                            }

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
                            Toast.makeText(activity, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(activity, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(activity, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show();
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

//


                params.put("action", "Blockuser");
                params.put("other_user_id", helper.user_id);
                params.put("user_id", PrefMangr.getInstance().getUserId());

                Log.d("Blockuser", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(PublicProfile.this);
        requestQueue.add(stringRequest);


    }

    private void showAlert(String msg) {
        String positive, negative;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PublicProfile.this);

        alertDialogBuilder.setMessage(msg);
        if (flagValue == 3) {
            positive = "Accept";
            negative = "Decine";
        } else {
            positive = "Yes";
            negative = "No";
        }

        alertDialogBuilder.setPositiveButton(positive, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (flagValue == 3) {
                    request_type = "accept";
                }
                makeStringReq();

                dialog.dismiss();
            }

        });

        alertDialogBuilder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (flagValue == 3) {
                    request_type = "decline";
                    makeStringReq();
                }
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

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
                progress_dialog = new ProgressDialog(PublicProfile.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(PublicProfile.this);
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
                            if (obj.has("msg")) {
                                msg = obj.getString("msg");
                            }
                            if (flagValue == 0) {
                                JSONObject object = obj.getJSONObject("users");

                                user_id = object.getString("user_id");
                                Commom_Method.printLog("user_id", user_id);
                                fullname = object.getString("fullname");
                                Commom_Method.printLog("fullname", fullname);
                                nickname = object.getString("nickname");
                                Commom_Method.printLog("nickname", nickname);
                                email = object.getString("email");
                                Commom_Method.printLog("email", email);
                                password = object.getString("password");
                                Commom_Method.printLog("password", password);
                                dob = object.getString("dob");
                                Commom_Method.printLog("dob", dob);
                                verify_code = object.getString("verify_code");
                                Commom_Method.printLog("verify_code", verify_code);
                                if (object.has("block_status")) {
                                    block_status = object.getString("block_status");
                                }
                                is_verified = object.getString("is_verified");
                                Commom_Method.printLog("is_verified", is_verified);
                                gmt_value = object.getString("gmt_value");
                                Commom_Method.printLog("gmt_value", gmt_value);
                                profileimage = object.getString("profileimage");
                                Commom_Method.printLog("profileimage", profileimage);
                                type = object.getString("type");
                                Commom_Method.printLog("type", type);
                                login_status = object.getString("login_status");
                                Commom_Method.printLog("login_status", login_status);
                                device_id = object.getString("device_id");
                                Commom_Method.printLog("device_id", device_id);
                                admin = object.getString("admin");
                                Commom_Method.printLog("admin", admin);
                                gpsCount = object.getString("gpsCount");
                                Commom_Method.printLog("gpsCount", gpsCount);
                                active_status = object.getString("active_status");
                                Commom_Method.printLog("active_status", active_status);
                                added_date = object.getString("added_date");
                                Commom_Method.printLog("added_date", added_date);
                                friend_count = object.getString("friend_count");
                                Commom_Method.printLog("friend_count", friend_count);
                                if (object.has("user_status")) {
                                    user_status = object.getString("user_status");
                                }
                                Commom_Method.printLog("friend_status", friend_status);
                                if (object.has("user_type")) {
                                    user_type = object.getString("user_type");
                                }
                                Commom_Method.printLog("user_type", user_type);

                                following_count = object.getString("following_count");
                                Commom_Method.printLog("following_count", following_count);
                                follow_status = object.getString("follow_status");
                                Commom_Method.printLog("follow_status", follow_status);
                                All_post_count = object.getString("All_post_count");
                                Commom_Method.printLog("All_post_count", All_post_count);
                                friend_request_count = obj.getString("friend_request_count");
                                Commom_Method.printLog("friend_request_count", friend_request_count);


                                Picasso.with(activity).load(profileimage).placeholder(R.drawable.loading).error(R.drawable.default_user).into(profile_img);
                                profile_name.setText(fullname);
                                PrefMangr.getInstance().setFullName(fullname);
                                post_count_txt.setText(All_post_count);
                                fan_count_txt.setText(friend_count);
                                following_count_txt.setText(following_count);

                                String num = All_post_count;
                                String num2 = All_post_count;
                                String s = "000000";

                                int result = Integer.parseInt(num);
                                s = String.format("%06d", result);
                                Log.d("postcount", s);

                                String str1 = "";
                                for (int i = 0; i < 6 - num.length(); i++) {
                                    str1 += "0";
                                }
                                num = str1 + num;

                                String[] parts = num.split("");
                                if (num2.length() == 1 & parts[parts.length - 1].equals("0")) {
                                    no6.setBackgroundResource(R.drawable.edit_card3);
                                    no6.setTextColor(Color.WHITE);
                                    no5.setBackgroundResource(R.drawable.edit_card3);
                                    no5.setTextColor(Color.WHITE);
                                    no4.setBackgroundResource(R.drawable.edit_card3);
                                    no4.setTextColor(Color.WHITE);
                                    no3.setBackgroundResource(R.drawable.edit_card3);
                                    no3.setTextColor(Color.WHITE);
                                    no2.setBackgroundResource(R.drawable.edit_card3);
                                    no2.setTextColor(Color.WHITE);
                                    no1.setBackgroundResource(R.drawable.edit_card3);
                                    no1.setTextColor(Color.WHITE);

                                }
                                if (num2.length() == 1) {
                                    no6.setBackgroundResource(R.drawable.edit_card3);
                                    no6.setTextColor(Color.WHITE);
                                    no5.setBackgroundResource(R.drawable.edit_card3);
                                    no5.setTextColor(Color.WHITE);
                                    no4.setBackgroundResource(R.drawable.edit_card3);
                                    no4.setTextColor(Color.WHITE);
                                    no3.setBackgroundResource(R.drawable.edit_card3);
                                    no3.setTextColor(Color.WHITE);
                                    no2.setBackgroundResource(R.drawable.edit_card3);
                                    no2.setTextColor(Color.WHITE);

                                }
                                if (num2.length() == 2) {
                                    no6.setBackgroundResource(R.drawable.edit_card3);
                                    no6.setTextColor(Color.WHITE);
                                    no5.setBackgroundResource(R.drawable.edit_card3);
                                    no5.setTextColor(Color.WHITE);
                                    no4.setBackgroundResource(R.drawable.edit_card3);
                                    no4.setTextColor(Color.WHITE);
                                    no3.setBackgroundResource(R.drawable.edit_card3);
                                    no3.setTextColor(Color.WHITE);


                                }
                                if (num2.length() == 3) {
                                    no6.setBackgroundResource(R.drawable.edit_card3);
                                    no6.setTextColor(Color.WHITE);
                                    no5.setBackgroundResource(R.drawable.edit_card3);
                                    no5.setTextColor(Color.WHITE);
                                    no4.setBackgroundResource(R.drawable.edit_card3);
                                    no4.setTextColor(Color.WHITE);


                                }
                                if (num2.length() == 4) {
                                    no6.setBackgroundResource(R.drawable.edit_card3);
                                    no6.setTextColor(Color.WHITE);
                                    no5.setBackgroundResource(R.drawable.edit_card3);
                                    no5.setTextColor(Color.WHITE);


                                }
                                if (num2.length() == 5) {
                                    no6.setBackgroundResource(R.drawable.edit_card3);
                                    no6.setTextColor(Color.WHITE);


                                }

                                String a = parts[parts.length - 6];
                                Log.d("postcount2", String.valueOf(a));
                                String b = parts[parts.length - 5];

                                String c = parts[parts.length - 4];

                                String d = parts[parts.length - 3];

                                String e = parts[parts.length - 2];

                                String f = parts[parts.length - 1];
                                no1.setText(String.valueOf(f));
                                // no.setText(f);
                                no2.setText(String.valueOf(e));
                                no3.setText(String.valueOf(d));
                                no4.setText(String.valueOf(c));
                                no5.setText(String.valueOf(b));
                                no6.setText(String.valueOf(a));
                                no.setText(f);
                                Log.d("postcount5", String.valueOf(f));


                                if (type.equals("0")) {
                                    if (user_status.equals("Add Friend")) {
                                        tick.setVisibility(View.GONE);
                                        txtfollowing.setText("Fan");
                                    } else if (user_status.equals("Request_sent")) {
                                        tick.setVisibility(View.GONE);
                                        txtfollowing.setText("Request Sent");
                                    } else if (user_status.equals("Fan")) {
                                        tick.setVisibility(View.VISIBLE);
                                        txtfollowing.setText("Unfan");
                                    } else {
                                        tick.setVisibility(View.GONE);
                                        txtfollowing.setText("Accept Request");
                                    }

                                } else {

                                    if (user_status.equals("Follow")) {
                                        tick.setVisibility(View.GONE);
                                        txtfollowing.setText("Follow");
                                    } else {
                                        tick.setVisibility(View.VISIBLE);
                                        txtfollowing.setText("Unfollow");
                                    }
                                }
                            } else if (flagValue > 0) {
                                if (msg.equals("You have already sent friend request")) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PublicProfile.this);
                                    // alertDialogBuilder.setTitle("Alert!");
                                    alertDialogBuilder.setMessage(msg);


                                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(PublicProfile.this, PublicProfile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        }

                                    });


                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                } else if (msg.equals("You have already friend this user")) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PublicProfile.this);
                                    //  alertDialogBuilder.setTitle("Alert!");
                                    alertDialogBuilder.setMessage(msg);


                                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(PublicProfile.this, PublicProfile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        }

                                    });


                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                } else if (msg.equals("Request sent successfully.")) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PublicProfile.this);
                                    //   alertDialogBuilder.setTitle("Alert!");
                                    alertDialogBuilder.setMessage(msg);


                                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            Intent intent = new Intent(PublicProfile.this, PublicProfile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);

                                            dialog.dismiss();
                                        }

                                    });


                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                } else if (msg.equals("Invalid request. Please check your friend and user id")) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PublicProfile.this);
                                    //  alertDialogBuilder.setTitle("Alert!");
                                    alertDialogBuilder.setMessage(msg);


                                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(PublicProfile.this, PublicProfile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            finish();

                                            dialog.dismiss();
                                        }

                                    });


                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                } else if (msg.equals("Unfriend successfully.")) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PublicProfile.this);
                                    // alertDialogBuilder.setTitle("Alert!");
                                    alertDialogBuilder.setMessage(msg);


                                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(PublicProfile.this, PublicProfile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        }

                                    });


                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                } else if (msg.equals("Follow user successfully")) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PublicProfile.this);
                                    // alertDialogBuilder.setTitle("Alert!");
                                    alertDialogBuilder.setMessage(msg);


                                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(PublicProfile.this, PublicProfile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        }

                                    });


                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                } else if (msg.equals("You have already follow this user.")) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PublicProfile.this);
                                    //  alertDialogBuilder.setTitle("Alert!");
                                    alertDialogBuilder.setMessage(msg);


                                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(PublicProfile.this, PublicProfile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        }

                                    });


                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                } else if (msg.equals("Un follow user")) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PublicProfile.this);
                                    //  alertDialogBuilder.setTitle("Alert!");
                                    alertDialogBuilder.setMessage(msg);


                                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(PublicProfile.this, PublicProfile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        }

                                    });


                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                } else if (msg.equals("Request accept successfully")) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PublicProfile.this);
                                    // alertDialogBuilder.setTitle("Alert!");
                                    alertDialogBuilder.setMessage(msg);


                                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(PublicProfile.this, PublicProfile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        }

                                    });


                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                } else {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PublicProfile.this);
                                    /// alertDialogBuilder.setTitle("Alert!");
                                    alertDialogBuilder.setMessage("Something Went Wrong");


                                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }

                                    });


                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            }


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
                            Toast.makeText(activity, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(activity, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(activity, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show();
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

//

                if (flagValue == 0) {
                    params.put("action", "Showuserdetails");
                    params.put("user_id", helper.user_id);
                    params.put("my_user_id", PrefMangr.getInstance().getUserId());
                } else if (flagValue == 1) {
                    params.put("action", "Sentfriendrequest");
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                    params.put("other_user_id", helper.user_id);

                } else if (flagValue == 2) {
                    params.put("action", "Unfriend");
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                    params.put("friend_id", helper.user_id);

                } else if (flagValue == 3) {
                    params.put("action", "Acceptdeclinerequest");
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                    params.put("other_user_id", helper.user_id);
                    params.put("type", request_type);

                } else if (flagValue == 4) {
                    params.put("action", "Followuser");
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                    params.put("other_user_id", helper.user_id);


                } else if (flagValue == 5) {
                    params.put("action", "Unfollowuser");
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                    params.put("other_user_id", helper.user_id);


                }

                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(PublicProfile.this);
        requestQueue.add(stringRequest);
        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(stringRequest,
//                tag_string_req);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        helper.status = "";
        helper.status = "";
        if (helper.back_status.equals("search")) {
            Intent intent = new Intent(PublicProfile.this, Wall.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("flag", 2);
            startActivity(intent);
        } else if (helper.back_status.equals("comment")) {
            finish();
        } else {
            helper.status = "";
            helper.status = "";
            posts.repost(PublicProfile.this, 0);
            Intent intent = new Intent(PublicProfile.this, Wall.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }
}

