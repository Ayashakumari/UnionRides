package mx.bigapps.unionrides.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
//import mx.bigapps.unionrides.Activity.Add_Comment;
import mx.bigapps.unionrides.Activity.Add_Comment;
import mx.bigapps.unionrides.Activity.PostPreviewimage;
import mx.bigapps.unionrides.Activity.PublicProfile;
import mx.bigapps.unionrides.Adapter.Photo_Adapter;
import mx.bigapps.unionrides.Adapter.UserPost_Adapter;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.UserList;
import mx.bigapps.unionrides.Model.UserPosts;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;

/**
 * Created by admin on 01-11-2017.
 */
public class Clubs extends Fragment {
     RecyclerView photo_recycle;

   Photo_Adapter photo_adapter;
    ArrayList<UserList> photolist;
    CircleImageView profile_img;
    LinearLayout commnt;
    ImageView postimage, report, edit;
    ImageView light;
    TextView likecount;
    int like = 6;
   ProgressDialog progress_dialog;
    final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
   String progress_dialog_msg = "", tag_string_req = "string_req";
    UserList userList;
     RecyclerView userPost;
    UserPost_Adapter userPost_adapter;
    ArrayList<UserPosts> userPostsArrayList = new ArrayList<>();
  UserPosts userPosts;
    TextView notfound;
    SwipeRefreshLayout swipeRefreshLayout;
    String total_count = "";
    String refresh = "";
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount, pastVisiblesItems;
    static int page = 0;
    int flagvalue, flagvalue1, curSize;
    LinearLayoutManager layoutManager;
     TextView nofriends;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.club, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.my_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.piink, R.color.darkpurple, R.color.green, R.color.orange);
        notfound = (TextView) rootView.findViewById(R.id.notfound);
        nofriends = (TextView) rootView.findViewById(R.id.nofriends);
        profile_img = (CircleImageView) rootView.findViewById(R.id.profile_img);
        userPost = (RecyclerView) rootView.findViewById(R.id.userPost);

        layoutManager = new LinearLayoutManager(getActivity());
        userPost.setLayoutManager(layoutManager);
        light = (ImageView) rootView.findViewById(R.id.light);
        likecount = (TextView) rootView.findViewById(R.id.likecount);
        photo_recycle = (RecyclerView) rootView.findViewById(R.id.photolist);
        commnt = (LinearLayout) rootView.findViewById(R.id.commnt);
        postimage = (ImageView) rootView.findViewById(R.id.postimage);
        report = (ImageView) rootView.findViewById(R.id.flag);
        edit = (ImageView) rootView.findViewById(R.id.edit);
        userList();
        //showAllPost();


        likecount.setText(String.valueOf(like));
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like = like + 1;
                likecount.setText(String.valueOf(like));
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = "1";
                Log.d("refreshh", "333" + refresh);
                userPost.removeAllViews();
                userPost.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                page = 0;
                userPostsArrayList.clear();
                //list.clear();
                flagvalue1 = flagvalue = 0;
                //showAllPost();
            }
        });
        userPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = userPost.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                Log.d("visibleItemCount", "" + visibleItemCount);
                Log.d("totalItemCount", "" + totalItemCount);
                Log.d("firstVisibleItem", "" + firstVisibleItem);
                Log.d("LogEntity1.size()", "" + userPostsArrayList.size());
                Log.d("total_count", total_count);
                Log.d("page", "" + page);
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    Log.d("page1", "" + page);
                    try {
                        if (userPostsArrayList.size() < Integer.parseInt(total_count)) {
                            Log.d("page2", "" + page);

                            page = page + 1;
                            // showAllPost();


                        } else {
                            Toast.makeText(getActivity(),
                                    "End of list", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    loading = true;
                }


            }
        });
      /*  userPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            page = page + 1;
                            showAllPost();

                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
            }
        });*/

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());


                alertDialogBuilder.setMessage("Would you like to delete this post");
                alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {


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


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert("Would you like to report this post? ");
            }
        });
        postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostPreviewimage.class);
                startActivity(intent);
            }
        });
        commnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Add_Comment.class);
                startActivity(intent);

            }
        });
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PublicProfile.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

   /* public void showAllPost() {
       *//* if (page == 0) {
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
        }*//*


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        //  userPostsArrayList = new ArrayList<>();
                        *//*mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);*//*
                        hideProgDialog();

                        JSONObject obj = null;
                        String videothumb = null;
                        String commentBy = null, comment = null;
                        String cmntdatetime = null;
                        Log.d("Showwallpost", response.toString());

                        try {
                            obj = new JSONObject(response.toString());
                            ArrayList<UserPosts> list = new ArrayList<UserPosts>();
                            JSONArray jsonArray = obj.getJSONArray("users");
                            total_count = obj.getString("paging_count");
                            Log.d("Page_Count", "<<>>" + total_count);
                            if (jsonArray.length() == 0) {
                                if (PrefMangr.getInstance().getuserType().equals("0")) {
                                    notfound.setText("Add Post or Add New Users to See Their Posts");
                                } else {
                                    notfound.setText("Add Post or Follow Users to See Their Posts");
                                }
                                notfound.setVisibility(View.VISIBLE);
                                userPost.setVisibility(View.GONE);
                                swipeRefreshLayout.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jarray = jsonArray.getJSONObject(i);
                                    String user_id = jarray.getString("user_id");
                                    String post_id = jarray.getString("post_id");
                                    String post_title = jarray.getString("post_title");
                                    // String post_title2 = post_title.substring(0, 1).toUpperCase() + post_title.substring(1).toLowerCase();
                                    String data = jarray.getString("data");
                                    String video_thumbnail = jarray.getString("video_thumbnail");
                                    String parent_id = jarray.getString("parent_id");
                                    String flagged = jarray.getString("flagged");
                                    String post_type = jarray.getString("post_type");
                                    String post_from = jarray.getString("post_from");
                                    String posted_by = jarray.getString("posted_by");
                                    String comment_count = jarray.getString("comment_count");
                                    String like_count = jarray.getString("like_count");
                                    String posted_time = jarray.getString("posted_time");
                                    String myPost = jarray.getString("is_my_post");
                                    String profile_image = jarray.getString("profile_image");
                                    JSONArray comments = jarray.getJSONArray("comments");
                                    Log.d("comments", String.valueOf(comments));
                                    JSONArray video_thumb_size = jarray.getJSONArray("video_thumb_size");
                                    if (video_thumb_size.length() != 0) {
                                        JSONObject jarrayvideo = video_thumb_size.getJSONObject(0);
                                        videothumb = jarrayvideo.getString("1242_1242");
                                    } else {
                                        videothumb = null;
                                    }
                                    if (comments.length() != 0) {
                                        JSONObject jarraycomment = comments.getJSONObject(comments.length() - 1);
                                        commentBy = jarraycomment.getString("comment_by");
                                        comment = jarraycomment.getString("comments");
                                        cmntdatetime = jarraycomment.getString("datetime");
                                    } else {
                                        commentBy = "";
                                        comment = "";
                                        cmntdatetime = "";
                                    }
                                    userPosts = new UserPosts(post_id, user_id, post_title, data, video_thumbnail, parent_id, flagged, post_type, post_from, comment_count, like_count, posted_time, videothumb, myPost, posted_by, profile_image, commentBy, comment, cmntdatetime);
                                    // userPostsArrayList.add(userPosts);
                                    list.add(userPosts);
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);

                                    *//*userPost_adapter = new UserPost_Adapter(getActivity(), userPostsArrayList);
                                    userPost.setAdapter(userPost_adapter);*//*
                                    notfound.setVisibility(View.GONE);
                                    userPost.setVisibility(View.VISIBLE);


                                }

                                if (page == 0) {
                                    userPostsArrayList.addAll(list);
                                    try {

                                        if (userPostsArrayList.size() > 0) {
                                            userPost.setVisibility(View.VISIBLE);
                                            userPost_adapter = new UserPost_Adapter(getActivity(), userPostsArrayList);
                                            userPost.setAdapter(userPost_adapter);
                                            swipeRefreshLayout.setRefreshing(false);
                                        } else {
                                            userPost.setVisibility(View.GONE);
                                            notfound.setVisibility(View.VISIBLE);

                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    curSize = userPostsArrayList.size();
                                    userPost_adapter.notifyDataSetChanged();
                                } else {

                                    curSize = userPostsArrayList.size();
                                    userPostsArrayList.addAll(curSize, list);
                                    userPost_adapter.notifyItemInserted(curSize);
                                    userPost_adapter.notifyItemRangeChanged(curSize, userPostsArrayList.size());
                                    userPost_adapter.notifyDataSetChanged();
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
                        if (page == 0) {
                            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                            mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        }
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

                params.put("action", "Showwallpost");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("page", String.valueOf(page));
                Log.d("Showwallpost", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }*/

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

    private void userList() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "loading...";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;

                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj = null;
                        photolist = new ArrayList();
                        Log.d("Showuserlist", response.toString());

                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jsonArray = obj.getJSONArray("users");
                            if (jsonArray.length() == 0) {
                                photo_recycle.setVisibility(View.GONE);
                                nofriends.setVisibility(View.VISIBLE);
                                photo_recycle.setVisibility(View.VISIBLE);

                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jarray = jsonArray.getJSONObject(i);
                                    String user_id = jarray.getString("user_id");
                                    String fullname = jarray.getString("fullname");
                                    String nickname = jarray.getString("nickname");
                                    //  String email = jarray.getString("email");
                                    String email = "";
                                    // String password = jarray.getString("password");

                                    String profileimage = jarray.getString("profileimage");
                                    //    String friend_status = jarray.getString("friend_status");
                                    String type = jarray.getString("type");
                                    //  String verify_code = jarray.getString("verify_code");
                                    String verify_code = "";

                                    Log.d("name", fullname);
                                    userList = new UserList(user_id, fullname, nickname, email, verify_code, type, profileimage);
                                    photolist.add(userList);
                                    photo_adapter = new Photo_Adapter(getActivity(), photolist);
                                    photo_recycle.setAdapter(photo_adapter);
                                    photo_recycle.smoothScrollToPosition(0);
                                    photo_recycle.setVisibility(View.VISIBLE);
                                    nofriends.setVisibility(View.GONE);
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

                params.put("action", "Myfollowuserlist");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                Log.d("Showuserlist", params.toString());
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

    }

    /*
        public static void repost(final Context context) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String msg;
                            userPostsArrayList = new ArrayList<>();
                            //  mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                            // mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                            JSONObject obj = null;
                            String videothumb = null;
                            String commentBy = "";
                            String comment = "";
                            String cmntdatetime = "";
                            Log.d("Showwallpost", response.toString());
                            try {
                                obj = new JSONObject(response.toString());
                                JSONArray jsonArray = obj.getJSONArray("users");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jarray = jsonArray.getJSONObject(i);
                                    String user_id = jarray.getString("user_id");
                                    String post_id = jarray.getString("post_id");
                                    String post_title = jarray.getString("post_title");
                                    String data = jarray.getString("data");
                                    String video_thumbnail = jarray.getString("video_thumbnail");
                                    String parent_id = jarray.getString("parent_id");
                                    String flagged = jarray.getString("flagged");
                                    String post_type = jarray.getString("post_type");
                                    String post_from = jarray.getString("post_from");
                                    String comment_count = jarray.getString("comment_count");
                                    String like_count = jarray.getString("like_count");
                                    String posted_time = jarray.getString("posted_time");
                                    String myPost = jarray.getString("is_my_post");
                                    String posted_by = jarray.getString("posted_by");
                                    String profile_image = jarray.getString("profile_image");
                                    JSONArray comments = jarray.getJSONArray("comments");
                                    Log.d("comments", String.valueOf(comments));
                                    JSONArray video_thumb_size = jarray.getJSONArray("video_thumb_size");
                                    if (video_thumb_size.length() != 0) {
                                        JSONObject jarrayvideo = video_thumb_size.getJSONObject(0);
                                        videothumb = jarrayvideo.getString("320_320");
                                    } else {
                                        videothumb = null;
                                    }
                                    if (comments.length() != 0) {
                                        JSONObject jarraycomment = comments.getJSONObject(comments.length() - 1);
                                        commentBy = jarraycomment.getString("comment_by");
                                        comment = jarraycomment.getString("comments");
                                        cmntdatetime = jarraycomment.getString("datetime");
                                    } else {
                                        commentBy = "";
                                        comment = "";
                                        cmntdatetime = "";
                                    }
                                    userPosts = new UserPosts(post_id, user_id, post_title, data, video_thumbnail, parent_id, flagged, post_type, post_from, comment_count, like_count, posted_time, videothumb, myPost, posted_by, profile_image, commentBy, comment, cmntdatetime);

                                    userPostsArrayList.add(userPosts);
                                    userPost_adapter = new UserPost_Adapter(context, userPostsArrayList);
                                    userPost.setAdapter(userPost_adapter);
                                    userPost.smoothScrollToPosition(userPostsArrayList.size() - 1);


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

                    params.put("action", "Showwallpost");
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                    params.put("page", String.valueOf(page));
                    Log.d("Showwallpost", params.toString());
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);


        }*/
    public  void repostUser(final Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;


                        JSONObject obj = null;
                        Log.d("Showuserlist", response.toString());
                        photolist = new ArrayList();
                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jsonArray = obj.getJSONArray("users");
                            if (jsonArray.length() == 0) {
                                photo_recycle.setVisibility(View.GONE);
                                nofriends.setVisibility(View.VISIBLE);
                                photo_recycle.setVisibility(View.VISIBLE);

                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jarray = jsonArray.getJSONObject(i);
                                    String user_id = jarray.getString("user_id");
                                    String fullname = jarray.getString("fullname");
                                    String nickname = jarray.getString("nickname");
                                    //  String email = jarray.getString("email");
                                    String email = "";
                                    // String password = jarray.getString("password");

                                    String profileimage = jarray.getString("profileimage");
                                    //    String friend_status = jarray.getString("friend_status");
                                    String type = jarray.getString("type");
                                    //  String verify_code = jarray.getString("verify_code");
                                    String verify_code = "";

                                    Log.d("name", fullname);
                                    userList = new UserList(user_id, fullname, nickname, email, verify_code, type, profileimage);
                                    photolist.add(userList);
                                    photo_adapter = new Photo_Adapter(context, photolist);
                                    photo_recycle.setAdapter(photo_adapter);
                                    photo_recycle.smoothScrollToPosition(0);
                                    photo_recycle.setVisibility(View.VISIBLE);
                                    nofriends.setVisibility(View.GONE);
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

                params.put("action", "Myfollowuserlist");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                Log.d("Showuserlist", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }
}





