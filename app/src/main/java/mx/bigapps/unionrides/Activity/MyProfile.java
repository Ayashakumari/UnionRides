package mx.bigapps.unionrides.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Fragment.Fans2;
import mx.bigapps.unionrides.Fragment.Myevents;
import mx.bigapps.unionrides.Fragment.Posts;
import mx.bigapps.unionrides.Fragment.PostsWall;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.Commom_Method;
import mx.bigapps.unionrides.utils.helper;

public class MyProfile extends Fragment {
    LinearLayout home, serrch, rides, setting;
    RelativeLayout chat;
    ImageView ivhome, ivsearch, ivrides, ivchat, ivsetting;
    LinearLayout linearLayoutfollowing;
    TextView txtfollowing;
    TextView events_txt, post_txt, txtrides;
    ImageView camera, video, events, header_setting, headerlogo;
     TextView follwingCount;
    RelativeLayout fanslayout;
    LinearLayout llride, llpost, llevents;
    ImageView edit;
    LinearLayout commnt;
    ImageView light;

    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    JSONObject jobj;
    String msg;

    TextView likecount;
    int like = 6;
    RelativeLayout profilelayout;
    Fragment fragment = null;
    FrameLayout frameLayout;
    ImageView post, evnts, header_ride;
    TextView txtRide, txtpost, txtevent;

    static String user_id;
    static String fullname;
    static String nickname;
    static String email;
    static String password;
    static String dob;
    static String verify_code;
    static String is_verified;
    static String gmt_value;
    static String profileimage;
    static String type;
    static String login_status;
    static String device_id;
    static String admin;
    static String gpsCount;
    static String active_status;
    static String added_date;
    static String friend_count;
    static String friend_status;
    static String All_post_count;
    static String friend_request_count;
    static String user_type = "";
    static String following_count;
    static String follow_status;
    static String user_status;

    static ImageView profile_img;
    static TextView profile_name;
    static TextView post_count_txt;
    static TextView fan_count_txt;
    static TextView following_count_txt;
    static TextView no6;
    static TextView no5;
    static TextView no4;
    static TextView no3;
    static TextView no2;
    static TextView no1;
    static TextView no;
    LinearLayout llfans;
    LinearLayout llgrid;
    ImageView grid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_my_profile, container, false);
        no6 = (TextView) rootView.findViewById(R.id.no6);
        no5 = (TextView) rootView.findViewById(R.id.no5);
        no4 = (TextView) rootView.findViewById(R.id.no4);
        no3 = (TextView) rootView.findViewById(R.id.no3);
        no2 = (TextView) rootView.findViewById(R.id.no2);
        no1 = (TextView) rootView.findViewById(R.id.no1);
        no = (TextView) rootView.findViewById(R.id.no);
        grid = (ImageView) rootView.findViewById(R.id.grid);
        llgrid = (LinearLayout) rootView.findViewById(R.id.llgrid);
        llfans = (LinearLayout) rootView.findViewById(R.id.llfans);
        fan_count_txt = (TextView) rootView.findViewById(R.id.fan_count_txt);
        profile_img = (ImageView) rootView.findViewById(R.id.profile_img);
        txtrides = (TextView) rootView.findViewById(R.id.rides2);
        txtRide = (TextView) rootView.findViewById(R.id.rides2);
        following_count_txt = (TextView) rootView.findViewById(R.id.following_count_txt);
        txtevent = (TextView) rootView.findViewById(R.id.events_txt);
        txtpost = (TextView) rootView.findViewById(R.id.post_txt);
        ivhome = (ImageView) rootView.findViewById(R.id.home_img);
        ivhome.setImageResource(R.drawable.ic_home);
        ivsearch = (ImageView) rootView.findViewById(R.id.search_img);
        ivrides = (ImageView) rootView.findViewById(R.id.rides);
        ivchat = (ImageView) rootView.findViewById(R.id.chat);
        ivsetting = (ImageView) rootView.findViewById(R.id.setting);
        ivsetting.setImageResource(R.drawable.default_user);
        post_count_txt = (TextView) rootView.findViewById(R.id.post_count);
        home = (LinearLayout) rootView.findViewById(R.id.toolbar_home);
        serrch = (LinearLayout) rootView.findViewById(R.id.toolbar_search);
        rides = (LinearLayout) rootView.findViewById(R.id.toolbar_rides);
        chat = (RelativeLayout) rootView.findViewById(R.id.toolbar_chat);
        setting = (LinearLayout) rootView.findViewById(R.id.toolbar_setting);
        fanslayout = (RelativeLayout) rootView.findViewById(R.id.fanslayout);
        fanslayout.setVisibility(View.VISIBLE);
        follwingCount = (TextView) rootView.findViewById(R.id.follwingCount);
        follwingCount.setVisibility(View.VISIBLE);
        profile_name = (TextView) rootView.findViewById(R.id.profile_name);
        headerlogo = (ImageView) rootView.findViewById(R.id.header_logo);
        headerlogo.setVisibility(View.GONE);
        header_setting = (ImageView) rootView.findViewById(R.id.header_setting);
        header_setting.setImageResource(R.drawable.header_setting);
        header_setting.setVisibility(View.VISIBLE);
        camera = (ImageView) rootView.findViewById(R.id.header_camera);
        camera.setVisibility(View.GONE);
        video = (ImageView) rootView.findViewById(R.id.header_video);
        video.setVisibility(View.GONE);
        events = (ImageView) rootView.findViewById(R.id.header_event);
        events.setVisibility(View.VISIBLE);
        profilelayout = (RelativeLayout) rootView.findViewById(R.id.profilelayout2);
        llpost = (LinearLayout) rootView.findViewById(R.id.lppost);
        llevents = (LinearLayout) rootView.findViewById(R.id.llevent);
        frameLayout = (FrameLayout) rootView.findViewById(R.id.content_frame);
        commnt = (LinearLayout) rootView.findViewById(R.id.commnt);
        light = (ImageView) rootView.findViewById(R.id.light);
        likecount = (TextView) rootView.findViewById(R.id.likecount);

        llride = (LinearLayout) rootView.findViewById(R.id.llride);
        linearLayoutfollowing = (LinearLayout) rootView.findViewById(R.id.llfollowing);
        edit = (ImageView) rootView.findViewById(R.id.edit);
        camera = (ImageView) rootView.findViewById(R.id.header_camera);
        camera.setImageResource(R.drawable.back);
        video = (ImageView) rootView.findViewById(R.id.header_video);
        video.setVisibility(View.GONE);
        events = (ImageView) rootView.findViewById(R.id.header_event);
        events.setImageResource(R.drawable.ic_exit);
        events.setVisibility(View.VISIBLE);
        header_ride = (ImageView) rootView.findViewById(R.id.ride);
        post = (ImageView) rootView.findViewById(R.id.posts);
        evnts = (ImageView) rootView.findViewById(R.id.event);
        events_txt = (TextView) rootView.findViewById(R.id.events_txt);
        post_txt = (TextView) rootView.findViewById(R.id.post_txt);
        txtrides = (TextView) rootView.findViewById(R.id.txtrides);
        likecount.setText(String.valueOf(like));

        header_ride.setImageResource(R.drawable.ic_rides_active);
        post.setImageResource(R.drawable.profile_post_inactive);
        evnts.setImageResource(R.drawable.profile_ic_event_inactive);
        txtRide.setTextColor(Color.parseColor("#4099FF"));
        txtpost.setTextColor(Color.parseColor("#676767"));
        txtevent.setTextColor(Color.parseColor("#676767"));
        profilelayout.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        llgrid.setVisibility(View.GONE);
        helper.status = "myprofile_ride";
        fragment = new RidesFragmnts();

        makeStringReq();

        if (fragment != null) {
            FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
        linearLayoutfollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.publicProfile = "";
                Intent intent = new Intent(getActivity(), FollowingList.class);
                intent.putExtra("userId", PrefMangr.getInstance().getUserId());
                startActivity(intent);
            }
        });
        llfans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.publicProfile = "";
                Intent intent = new Intent(getActivity(), FriendList.class);
                intent.putExtra("userId", PrefMangr.getInstance().getUserId());
                startActivity(intent);
            }
        });
        header_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Settings.class);
                startActivity(intent);
            }


        });
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Account_Setting.class);
                startActivity(intent);
            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(getActivity());
                }
                alert.setMessage("Do you want to logout ?");


                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            logout(PrefMangr.getInstance().getUserId());

                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        dialog.dismiss();
                    }

                });


                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


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
        });
        fanslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Wall.class);
                intent.putExtra("search", "search");
                startActivity(intent);
            }
        });


        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like = like + 1;
                likecount.setText(String.valueOf(like));
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());


                alertDialogBuilder.setMessage("Would you like to delete this post");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {


                        dialog1.cancel();

                    }

                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.cancel();

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });


        llride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* ((ScrollView) rootView.findViewById(R.id.scroll)).post(new Runnable() {
                    public void run() {
                        ((ScrollView) rootView.findViewById(R.id.scroll)).fullScroll(View.FOCUS_UP);
                    }
                });*/
                txtRide.setTextColor(Color.parseColor("#4099FF"));
                txtevent.setTextColor(Color.BLACK);
                txtpost.setTextColor(Color.BLACK);
                header_ride.setImageResource(R.drawable.ic_rides_active);
                post.setImageResource(R.drawable.profile_post_inactive);
                evnts.setImageResource(R.drawable.profile_ic_event_inactive);
                profilelayout.setVisibility(View.GONE);
                llgrid.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                txtRide.setTextColor(Color.parseColor("#4099FF"));
                txtpost.setTextColor(Color.parseColor("#676767"));
                txtevent.setTextColor(Color.parseColor("#676767"));
                helper.status = "myprofile_ride";
                fragment = new RidesFragmnts();
                if (fragment != null) {
                    FragmentManager fragmentManager = getChildFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });
       /* txtrides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PublicProfileEvents.class);
                startActivity(intent);
            }
        });*/
        llpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtpost.setTextColor(Color.parseColor("#4099FF"));
                txtevent.setTextColor(Color.BLACK);
                txtrides.setTextColor(Color.BLACK);
                header_ride.setImageResource(R.drawable.ic_rides_inactive);
                post.setImageResource(R.drawable.profile_post_active);
                evnts.setImageResource(R.drawable.profile_ic_event_inactive);
                txtpost.setTextColor(Color.parseColor("#4099FF"));
                txtRide.setTextColor(Color.parseColor("#676767"));
                txtevent.setTextColor(Color.parseColor("#676767"));
                profilelayout.setVisibility(View.GONE);
                llgrid.setVisibility(View.VISIBLE);

                frameLayout.setVisibility(View.VISIBLE);
                helper.status = "myprofile_fan";
                // Posts.repost(getContext(), 0);

                fragment = new Posts();

                if (fragment != null) {
                    FragmentManager fragmentManager = getChildFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });
        llgrid.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          txtpost.setTextColor(Color.parseColor("#4099FF"));
                                          txtevent.setTextColor(Color.BLACK);
                                          txtrides.setTextColor(Color.BLACK);
                                          header_ride.setImageResource(R.drawable.ic_rides_inactive);
                                          post.setImageResource(R.drawable.profile_post_active);
                                          evnts.setImageResource(R.drawable.profile_ic_event_inactive);
                                          txtpost.setTextColor(Color.parseColor("#4099FF"));
                                          txtRide.setTextColor(Color.parseColor("#676767"));
                                          txtevent.setTextColor(Color.parseColor("#676767"));
                                          profilelayout.setVisibility(View.GONE);
                                          frameLayout.setVisibility(View.VISIBLE);
                                          helper.status = "myprofile_fan";

                                          if (grid.getDrawable().getConstantState().equals
                                                  (getResources().getDrawable(R.drawable.list_n).getConstantState())) {
                                              grid.setImageResource(R.drawable.grid_n);

                                              //  Posts.repost(getContext(), 0);
                                              fragment = new Posts();
                                              if (fragment != null) {
                                                  FragmentManager fragmentManager = getChildFragmentManager();
                                                  fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


                                              } else {
                                                  Log.e("MainActivity", "Error in creating fragment");
                                              }

                                          } else if (grid.getDrawable().getConstantState().equals
                                                  (getResources().getDrawable(R.drawable.grid_n).getConstantState())) {
                                              grid.setImageResource(R.drawable.list_n);
                                              // Posts.repost(Wall.this, 0);
                                              fragment = new Fans2();
                                              if (fragment != null) {
                                                  FragmentManager fragmentManager = getChildFragmentManager();
                                                  fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


                                              } else {
                                                  Log.e("MainActivity", "Error in creating fragment");
                                              }
                                          } else {
                                              Log.e("MainActivity", "Error in creating fragment");
                                          }

                                      }


                                  }

        );


        llevents.setOnClickListener(new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View v) {
               /* ((ScrollView) rootView.findViewById(R.id.scroll)).post(new Runnable() {
                    public void run() {
                        ((ScrollView) rootView.findViewById(R.id.scroll)).fullScroll(View.FOCUS_UP);
                    }
                });*/
                                            txtevent.setTextColor(Color.parseColor("#4099FF"));
                                            txtpost.setTextColor(Color.BLACK);
                                            txtrides.setTextColor(Color.BLACK);
                                            header_ride.setImageResource(R.drawable.ic_rides_inactive);
                                            post.setImageResource(R.drawable.profile_post_inactive);
                                            evnts.setImageResource(R.drawable.profile_ic_event_active);
                                            txtevent.setTextColor(Color.parseColor("#4099FF"));
                                            txtRide.setTextColor(Color.parseColor("#676767"));
                                            txtpost.setTextColor(Color.parseColor("#676767"));
                                            llgrid.setVisibility(View.GONE);
                                            profilelayout.setVisibility(View.GONE);
                                            frameLayout.setVisibility(View.VISIBLE);
                                            helper.status = "myprofile_event";
                                            fragment = new Myevents();

                                            if (fragment != null) {
                                                FragmentManager fragmentManager = getChildFragmentManager();
                                                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


                                            } else {
                                                Log.e("MainActivity", "Error in creating fragment");
                                            }
                                        }
                                    }

        );
        return rootView;
    }

    private void logout(final String userId) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        JSONObject obj = null;
                        Log.d("Logout", response.toString());
                        try {
                            obj = new JSONObject(response.toString());
                            // JSONArray jsonArray = obj.getJSONArray("users");
                            Log.d("Logout", response.toString());
                            msg = obj.getString("msg");
                            Wall wall = new Wall();
                            wall.swipeRefreshLayout.setRefreshing(true);
                            PostsWall postsWall = new PostsWall();
                            postsWall.showAllPostsscroll(getActivity(), 0);
                            postsWall.page = 0;
                            PrefMangr.getInstance().setUSerId(null);
                            Intent intent = new Intent(getActivity(), Welcome.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                           wall.finish();
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

                params.put("action", "Logout");
                params.put("user_id", userId);
                Log.d("Logout", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    private void showAlert(String msg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {

                txtfollowing.setText("Follow   ");
                dialog.dismiss();
            }

        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }    //  myphotolist.smoothScrollToPosition(10);

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
                progress_dialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(getActivity());
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
                                Log.d("Myprofiledata", response.toString().substring(start1, end));
                            }
///                            msg=obj.getString("msg");

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
                            //user_status = object.getString("user_status");
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


                            Picasso.with(getActivity()).load(profileimage).placeholder(R.drawable.loading).into(profile_img);

                            if (profileimage.equals("https://www.unionrides.com/assets/images/noimage.png")) {
                                Picasso.with(getActivity()).load(R.drawable.uploadpic).placeholder(R.drawable.loading).into(profile_img);
                            }
                            profile_name.setText(fullname);
                            post_count_txt.setText(All_post_count);
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

                            fan_count_txt.setText(friend_count);
                            following_count_txt.setText(following_count);
                            follwingCount.setText(friend_request_count);
                            if (friend_request_count.equals("0")) {
                                follwingCount.setVisibility(View.GONE);
                            } else {
                                follwingCount.setVisibility(View.VISIBLE);
                            }
                            // makeStringReqView();
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
                            Toast.makeText(getActivity(), "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getActivity(), "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
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

                params.put("action", "Showuserdetails");
                params.put("user_id", PrefMangr.getInstance().getUserId());



                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(stringRequest,
//                tag_string_req);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    public  void repostdata(final Context context) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


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
///                            msg=obj.getString("msg");

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


                            Picasso.with(context).load(profileimage).placeholder(R.drawable.loading).into(profile_img);
                            if (profileimage.equals("https://www.unionrides.com/assets/images/noimage.png")) {
                                Picasso.with(context).load(R.drawable.uploadpic).placeholder(R.drawable.loading).into(profile_img);
                            }
                            profile_name.setText(fullname);
                            post_count_txt.setText(All_post_count);
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

                            fan_count_txt.setText(friend_count);
                            following_count_txt.setText(following_count);
                            follwingCount.setText(friend_request_count);
                            if (friend_request_count.equals("0")) {
                                follwingCount.setVisibility(View.GONE);
                            } else {
                                follwingCount.setVisibility(View.VISIBLE);
                            }
///*
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


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Showuserdetails");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }
}
