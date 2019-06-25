package mx.bigapps.unionrides.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
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
import mx.bigapps.unionrides.Activity.Wall;
import mx.bigapps.unionrides.Adapter.Gridadapter;
import mx.bigapps.unionrides.Adapter.Photo_Adapter;
import mx.bigapps.unionrides.Adapter.UserPost_Adapter;
import mx.bigapps.unionrides.Adapter.UserPost_GridAdapter;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.UserList;
import mx.bigapps.unionrides.Model.UserPosts;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;


/**
 * Created by admin on 01-11-2017.
 */
public class Fans2 extends Fragment {
    RecyclerView photo_recycle;

    Photo_Adapter photo_adapter;
    ArrayList<UserList> photolist;
    CircleImageView profile_img;
    LinearLayout commnt;
    ImageView postimage, report, edit;
    ImageView light;
    TextView likecount;
    int like = 6;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    UserList userList;
     RecyclerView userPost;
    Gridadapter userPost_adapter;
    ArrayList<UserPosts> userPostsArrayList;
    UserPosts userPosts;
    TextView notfound;
    SwipeRefreshLayout swipeRefreshLayout;
    String total_count = "";
    String refresh = "";
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount, pastVisiblesItems;
    int page = 0;
    int flagvalue, flagvalue1, curSize;
    LinearLayoutManager layoutManager;
     TextView nofriends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fans2, container, false);
       /* swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.my_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.piink, R.color.darkpurple, R.color.green, R.color.orange);*/
        notfound = (TextView) rootView.findViewById(R.id.notfound);
        nofriends = (TextView) rootView.findViewById(R.id.nofriends);
        profile_img = (CircleImageView) rootView.findViewById(R.id.profile_img);
        userPost = (RecyclerView) rootView.findViewById(R.id.userPost);
        layoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        userPost.setLayoutManager(layoutManager);

        light = (ImageView) rootView.findViewById(R.id.light);
        likecount = (TextView) rootView.findViewById(R.id.likecount);
        photo_recycle = (RecyclerView) rootView.findViewById(R.id.photolist);
        commnt = (LinearLayout) rootView.findViewById(R.id.commnt);
        postimage = (ImageView) rootView.findViewById(R.id.postimage);
        report = (ImageView) rootView.findViewById(R.id.flag);
        edit = (ImageView) rootView.findViewById(R.id.edit);
        // userList();

        userPostsArrayList = new ArrayList<>();
        likecount.setText(String.valueOf(like));
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like = like + 1;
                likecount.setText(String.valueOf(like));
            }
        });

        userPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                Log.d("visibleItemCount", "" + visibleItemCount);
                Log.d("totalItemCount", "" + totalItemCount);
                Log.d("firstVisibleItem", "" + firstVisibleItem);
                Log.d("LogEntity1.size()", "" +  userPostsArrayList.size());
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
                            showAllPost();


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



        showAllPost();


        return rootView;
    }

    public void showAllPost() {
        if (page == 0) {
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        //  userPostsArrayList = new ArrayList<>();
                        hideProgDialog();

                        JSONObject obj = null;
                        String videothumb = null;
                        String commentBy = null, comment = null;
                        String cmntdatetime = null;
                        Log.d("Showwallpost", response.toString());
                        String imagethumb = "";
                        String imagethumb320 = "";
                        String video_web_views_url = null;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                Log.d("Showwallpost", response.toString().substring(start1, end));
                            }

                            ArrayList<UserPosts> list = new ArrayList<UserPosts>();
                            JSONArray jsonArray = obj.getJSONArray("users");
                            total_count = obj.getString("paging_count");
                            Log.d("Page_Count", "<<>>" + total_count);
                            if (jsonArray.length() == 0) {
                                if (PrefMangr.getInstance().getuserType().equals("0")) {
                                    nofriends.setText("Add Post or Add New Users to See Their Posts");
                                } else {
                                    nofriends.setText("Add Post or Follow Users to See Their Posts");
                                }
                                if (helper.status.equals("otherprofile_fan")) {
                                    nofriends.setText("This  Users has no post to see");
                                }
                                if (helper.status.equals("myprofile_fan")) {
                                    nofriends.setText("You  have no post to see");
                                }

                                nofriends.setVisibility(View.VISIBLE);
                                userPost.setVisibility(View.GONE);
                                //  swipeRefreshLayout.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jarray = jsonArray.getJSONObject(i);
                                    String user_id = jarray.getString("user_id");
                                    String post_id = jarray.getString("post_id");
                                    String post_title = jarray.getString("post_title");
                                    // String post_title2 = post_title.substring(0, 1).toUpperCase() + post_title.substring(1).toLowerCase();
                                    String data = jarray.getString("data");
                                    String video_thumbnail = jarray.getString("video_thumbnail");
                                    //  String parent_id = jarray.getString("parent_id");
                                    String parent_id = "";
                                    String flagged = jarray.getString("flagged");
                                    String post_type = jarray.getString("post_type");
                                    String post_from = jarray.getString("post_from");
                                    String posted_by = jarray.getString("posted_by");
                                    String comment_count = jarray.getString("comment_count");
                                    String like_count = jarray.getString("like_count");
                                    String posted_time = jarray.getString("posted_time");
                                    String myPost = jarray.getString("is_my_post");
                                    String profile_image = jarray.getString("profileimage");
                                    JSONArray comments = jarray.getJSONArray("comments");
                                    Log.d("comments", String.valueOf(comments));
                                    JSONArray video_thumb_size = jarray.getJSONArray("video_thumb_size");
                                    String like_status = jarray.getString("like_status");
                                    String postTitle = jarray.getString("post_title");
                                    String image_web_views_url = jarray.getString("image_web_views_url");
                                    if (jarray.has("video_web_views_url")) {
                                        video_web_views_url = jarray.getString("video_web_views_url");
                                    }
                                    if (video_thumb_size.length() != 0) {
                                        JSONObject jarrayvideo = video_thumb_size.getJSONObject(0);
                                        videothumb = jarrayvideo.getString("1242_1242");
                                    } else {
                                        videothumb = null;
                                    }
                                    JSONArray image_thumb_size = jarray.getJSONArray("image_thumb_size");
                                    if (image_thumb_size.length() != 0) {
                                        JSONObject jarrayimage = image_thumb_size.getJSONObject(0);
                                        imagethumb = jarrayimage.getString("640_640");
                                        imagethumb320 = jarrayimage.getString("320_320");
                                    } else {
                                        imagethumb = null;
                                        imagethumb320 = null;
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
                                    String actual_image = jarray.getString("actual_image");
                                    userPosts = new UserPosts(post_id, user_id, post_title, data, video_thumbnail, parent_id, flagged, post_type, post_from, comment_count, like_count, posted_time, videothumb, myPost, posted_by, profile_image, commentBy, comment, cmntdatetime, imagethumb, imagethumb320, like_status, postTitle, video_web_views_url, actual_image, image_web_views_url);
                                    list.add(userPosts);
                                    //  userPostsArrayList.add(userPosts);

                                    nofriends.setVisibility(View.GONE);
                                    userPost.setVisibility(View.VISIBLE);
                                    Log.d("size", String.valueOf(userPostsArrayList.size()));

                                }
                            }
                            if (page == 0) {
                                userPostsArrayList.addAll(list);
                                try {

                                    if (userPostsArrayList.size() > 0) {
                                        userPost.setVisibility(View.VISIBLE);
                                        userPost_adapter = new Gridadapter(getActivity(), userPostsArrayList);
                                        userPost.setAdapter(userPost_adapter);

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
                                // userPost.smoothScrollToPosition(0);
                                curSize = userPostsArrayList.size();
                                userPostsArrayList.addAll(curSize, list);
                                userPost_adapter.notifyItemInserted(curSize);
                                userPost_adapter.notifyItemRangeChanged(curSize, userPostsArrayList.size());
                               // userPost_adapter.notifyDataSetChanged();
                            }
                         /*   if (Integer.parseInt(total_count) > userPostsArrayList.size()) {

                                page = page + 1;
                                showAllPost();
                            } else {
                                mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                                mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                                notfound.setVisibility(View.GONE);
                                userPost.setVisibility(View.VISIBLE);
                                userPost_adapter = new Gridadapter(getActivity(), userPostsArrayList);
                                userPost.setAdapter(userPost_adapter);
                            }*/
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
                if (helper.status.equals("otherprofile_fan")) {
                    params.put("user_id", helper.user_id);
                } else {
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                }
                if (helper.status.equals("myprofile_fan") || helper.status.equals("otherprofile_fan")) {
                    params.put("type", "mypost");
                } else {
                    params.put("type", "allpost");
                }
                params.put("grid_type", "mygrid");
                params.put("page", String.valueOf(page));
                Log.d("Showwallpost", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    private void showProgDialog() {
        // TODO Auto-generated method stub
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(getActivity());
            }
            progress_dialog.setMessage("loading");
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void hideProgDialog() {
        // TODO Auto-generated method stub
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



}
