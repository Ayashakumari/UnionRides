package mx.bigapps.unionrides.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import mx.bigapps.unionrides.Adapter.Followingpeople_Adapter;
import mx.bigapps.unionrides.Adapter.searchPeople_Adapter;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.NetWorkUserList;
import mx.bigapps.unionrides.Model.RequstedUserList;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;

public class FollowingList extends Activity {
    ImageView camera, video, events;
    LinearLayout home, serrch, rides, chat, setting;
    ImageView ivhome, ivsearch, ivrides, ivchat, ivsetting;
    static RecyclerView lvRequestedfanList;
    static RecyclerView lvnetworkfan;

    static Followingpeople_Adapter networkPeople_adapter;
    static searchPeople_Adapter searchPeople_adapter;
    LinearLayoutManager layoutManager, layoutManager2;
    private static ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    static NetWorkUserList netWorkUserList;
    static ArrayList<NetWorkUserList> netWorkUserLists = new ArrayList<>();
    static RequstedUserList requestedUserList;
    static ArrayList<RequstedUserList> requstedUserListArrayList;
    EditText search;
    String s_name;
    static LinearLayout requestedlayout;
    static LinearLayout networklayot;
    static TextView nonetworkfriend, norequestfound;
    ImageView cancle;
    JSONArray jsonArrayfriendlist;
    public static SwipeRefreshLayout swipeRefreshLayout;
    static String total_count = "";
    String refresh = "";
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount, pastVisiblesItems;
    static int page = 0;
    int flagvalue;
    int flagvalue1;
    static int curSize;
    ImageView back;
    String otherUserId;


    @Override
    public void onCreate(Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        setContentView(R.layout.followinglist);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.my_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.piink, R.color.darkpurple, R.color.green, R.color.orange);
        back = (ImageView) findViewById(R.id.back);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.my_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.piink, R.color.darkpurple, R.color.green, R.color.orange);
        nonetworkfriend = (TextView) findViewById(R.id.nonetworkfriend);
        nonetworkfriend.setText("No Following users found");
        lvnetworkfan = (RecyclerView) findViewById(R.id.lvnetworkfan);
        layoutManager = new LinearLayoutManager(FollowingList.this);
        lvnetworkfan.setLayoutManager(layoutManager);
        lvnetworkfan.setVisibility(View.VISIBLE);
        netWorkUserLists = new ArrayList<>();
        Intent intent = getIntent();
        otherUserId = intent.getStringExtra("userId");
        ShowAllusers(otherUserId);


        swipeRefreshLayout.setRefreshing(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = "1";
                Log.d("refreshh", "333" + refresh);
                lvnetworkfan.removeAllViews();
                lvnetworkfan.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                page = 0;
                netWorkUserLists.clear();

                flagvalue1 = flagvalue = 0;
                ShowAllusers(otherUserId);
            }
        });
        lvnetworkfan.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = lvnetworkfan.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                Log.d("visibleItemCount", "" + visibleItemCount);
                Log.d("totalItemCount", "" + totalItemCount);
                Log.d("firstVisibleItem", "" + firstVisibleItem);
                Log.d("LogEntity1.size()", "" + netWorkUserLists.size());
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
                        if (netWorkUserLists.size() < Integer.parseInt(total_count)) {
                            Log.d("page2", "" + page);

                            page = page + 1;
                            ShowAllusers(otherUserId);


                        } else {
                            Toast.makeText(FollowingList.this,
                                    "End of list", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    loading = true;
                }


            }
        });


    }

    private void Myfriendlist() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        // hideProgDialog();
                        requstedUserListArrayList = new ArrayList<>();
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj = null;
                        Log.d("Showfriendrequest", response.toString());

                        try {
                            obj = new JSONObject(response.toString());
                            jsonArrayfriendlist = obj.getJSONArray("result");
                            if (jsonArrayfriendlist.length() == 0) {
                                requestedlayout.setVisibility(View.GONE);
                                norequestfound.setVisibility(View.VISIBLE);


                            } else {
                                for (int i = 0; i < jsonArrayfriendlist.length(); i++) {
                                    JSONObject jarray = jsonArrayfriendlist.getJSONObject(i);
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

                                    requestedUserList = new RequstedUserList(user_id, fullname, nickname, email, verify_code, type, profileimage);
                                    requstedUserListArrayList.add(requestedUserList);
                                    searchPeople_adapter = new searchPeople_Adapter(FollowingList.this, requstedUserListArrayList);

                                    lvRequestedfanList.setAdapter(searchPeople_adapter);
                                    requestedlayout.setVisibility(View.VISIBLE);
                                    norequestfound.setVisibility(View.GONE);


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
                            Toast.makeText(FollowingList.this, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(FollowingList.this, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(FollowingList.this, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(FollowingList.this, "Network Error", Toast.LENGTH_LONG).show();
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

                params.put("action", "Showfriendrequest");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                Log.d("Showfriendrequest", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(FollowingList.this);
        requestQueue.add(stringRequest);


    }

    private static void showProgDialog2(Context context) {
        // TODO Auto-generated method stub
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(context);
            }
            progress_dialog.setMessage("loading");
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showProgDialog() {
        // TODO Auto-generated method stub
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(FollowingList.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(FollowingList.this);
            }
            progress_dialog.setMessage("loading");
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void hideProgDialog() {
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


    private void ShowAllusers(final String otherUserId) {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "loading...";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        swipeRefreshLayout.setRefreshing(false);
                        ArrayList<NetWorkUserList> list = new ArrayList<NetWorkUserList>();
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj = null;
                        Log.d("Myfollowuserlist", response.toString());

                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jsonArray = obj.getJSONArray("users");
                            //  total_count = obj.getString("paging_count");
                            // Log.d("Page_Count", "<<>>" + total_count);
                            if (jsonArray.length() == 0) {
                                if (jsonArray.length() == 0) {
                                    lvnetworkfan.setVisibility(View.GONE);
                                    nonetworkfriend.setVisibility(View.VISIBLE);
                                    swipeRefreshLayout.setVisibility(View.GONE);
                                }

                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jarray = jsonArray.getJSONObject(i);
                                    String user_id = jarray.getString("user_id");
                                    String fullname = jarray.getString("fullname");
                                    String nickname = jarray.getString("nickname");
                                    String Request_sent = jarray.getString("user_status");

                                    // String password = jarray.getString("password");

                                    String profileimage = jarray.getString("profileimage");
                                    //    String friend_status = jarray.getString("friend_status");
                                    String type = jarray.getString("type");
                                    String user_status = jarray.getString("user_status");


                                    Log.d("name", user_status);
                                    Log.d("name", nickname);

                                    netWorkUserList = new NetWorkUserList(user_id, fullname, nickname, Request_sent, user_status, type, profileimage);
                                    lvnetworkfan.setVisibility(View.VISIBLE);
                                    nonetworkfriend.setVisibility(View.GONE);
                                    list.add(netWorkUserList);
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);

                                }
                                if (page == 0) {
                                    netWorkUserLists.addAll(list);
                                    try {

                                        if (netWorkUserLists.size() > 0) {
                                            lvnetworkfan.setVisibility(View.VISIBLE);
                                            networkPeople_adapter = new Followingpeople_Adapter(FollowingList.this, netWorkUserLists);
                                            lvnetworkfan.setAdapter(networkPeople_adapter);
                                            swipeRefreshLayout.setRefreshing(false);
                                        } else {
                                            lvnetworkfan.setVisibility(View.GONE);
                                            nonetworkfriend.setVisibility(View.VISIBLE);

                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    curSize = netWorkUserLists.size();
                                    networkPeople_adapter.notifyDataSetChanged();
                                } else {

                                    curSize = netWorkUserLists.size();
                                    netWorkUserLists.addAll(curSize, list);
                                    networkPeople_adapter.notifyItemInserted(curSize);
                                    networkPeople_adapter.notifyItemRangeChanged(curSize, netWorkUserLists.size());
                                    networkPeople_adapter.notifyDataSetChanged();
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
                            Toast.makeText(FollowingList.this, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(FollowingList.this, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(FollowingList.this, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(FollowingList.this, "Network Error", Toast.LENGTH_LONG).show();
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
                // params.put("page", String.valueOf(page));
                params.put("other_user_id", otherUserId);
                Log.d("Myfollowuserlist", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(FollowingList.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    public static void notFound() {
        lvnetworkfan.setVisibility(View.GONE);
        nonetworkfriend.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }
}



