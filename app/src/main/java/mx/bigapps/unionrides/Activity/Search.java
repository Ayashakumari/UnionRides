package mx.bigapps.unionrides.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
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

import mx.bigapps.unionrides.Adapter.NetworkPeople_Adapter;
import mx.bigapps.unionrides.Adapter.searchPeople_Adapter;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.NetWorkUserList;
import mx.bigapps.unionrides.Model.RequstedUserList;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;

public class Search extends Fragment {
    ImageView camera, video, events;
    LinearLayout home, serrch, rides, chat, setting;
    ImageView ivhome, ivsearch, ivrides, ivchat, ivsetting;
    RecyclerView lvRequestedfanList;
    RecyclerView lvnetworkfan;

    NetworkPeople_Adapter networkPeople_adapter;
    searchPeople_Adapter searchPeople_adapter;
    LinearLayoutManager layoutManager, layoutManager2;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    private NetWorkUserList netWorkUserList;
    ArrayList<NetWorkUserList> netWorkUserLists = new ArrayList<>();
    private RequstedUserList requestedUserList;
    private ArrayList<RequstedUserList> requstedUserListArrayList;
    EditText search;
    String s_name;
    private LinearLayout requestedlayout;
    private LinearLayout networklayot;
    private TextView nonetworkfriend, norequestfound;
    ImageView cancle;
    JSONArray jsonArrayfriendlist;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String total_count = "";
    String refresh = "";
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount, pastVisiblesItems;
    int page = 0;
    int flagvalue;
    int flagvalue1;
    static int curSize;
    NestedScrollView scroll_view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_search1, container, false);
        helper.backbutton = "2";
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.my_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.piink, R.color.darkpurple, R.color.green, R.color.orange);

        scroll_view = (NestedScrollView) rootView.findViewById(R.id.scroll_view);
        cancle = (ImageView) rootView.findViewById(R.id.cancle);
        nonetworkfriend = (TextView) rootView.findViewById(R.id.nonetworkfriend);
        norequestfound = (TextView) rootView.findViewById(R.id.norequestfound);
        ivhome = (ImageView) rootView.findViewById(R.id.home_img);
        ivhome.setImageResource(R.drawable.ic_home);
        ivsearch = (ImageView) rootView.findViewById(R.id.search_img);
        ivsearch.setImageResource(R.drawable.ic_search_active);
        ivrides = (ImageView) rootView.findViewById(R.id.rides);
        ivchat = (ImageView) rootView.findViewById(R.id.chat);
        ivsetting = (ImageView) rootView.findViewById(R.id.setting);
        ivsetting.setImageResource(R.drawable.default_user);
        home = (LinearLayout) rootView.findViewById(R.id.toolbar_home);
        serrch = (LinearLayout) rootView.findViewById(R.id.toolbar_search);
        rides = (LinearLayout) rootView.findViewById(R.id.toolbar_rides);
        search = (EditText) rootView.findViewById(R.id.search);
        // chat = (Re) rootView.findViewById(R.id.toolbar_chat);
        setting = (LinearLayout) rootView.findViewById(R.id.toolbar_setting);
        lvnetworkfan = (RecyclerView) rootView.findViewById(R.id.lvnetworkfan);
        lvRequestedfanList = (RecyclerView) rootView.findViewById(R.id.lvRequestedfanList);
       /* requestedlayout = (LinearLayout) rootView.findViewById(R.id.requestedlayout);
        networklayot = (LinearLayout) rootView.findViewById(R.id.networklayout);*/
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager2 = new LinearLayoutManager(getActivity());
        lvRequestedfanList.setLayoutManager(layoutManager2);
        lvnetworkfan.setLayoutManager(layoutManager);
        lvnetworkfan.setVisibility(View.VISIBLE);
        netWorkUserLists = new ArrayList<>();
        ShowAllusers();
        Myfriendlist();
        layoutManager.setAutoMeasureEnabled(true);
        lvnetworkfan.setNestedScrollingEnabled(false);

        //  swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = "1";
                Log.d("refreshh", "333" + refresh);
                lvnetworkfan.removeAllViews();
                lvnetworkfan.removeAllViewsInLayout();
                lvRequestedfanList.removeAllViews();
                lvRequestedfanList.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                page = 0;
                netWorkUserLists.clear();
                requstedUserListArrayList.clear();
                flagvalue1 = flagvalue = 0;
                ShowAllusers();
                Myfriendlist();
            }
        });

        scroll_view.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) scroll_view.getChildAt(scroll_view.getChildCount() - 1);

                int diff = (view.getBottom() - (scroll_view.getHeight() + scroll_view
                        .getScrollY()));

                if (diff == 0) {
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
                                ShowAllusers();


                            } else {
                                /*Toast.makeText(getActivity(),
                                        "End of list", Toast.LENGTH_SHORT).show();*/
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        loading = true;
                    }

                }
            }
        });
      /*  scroll_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d("ScrollView", "scrollX_" + scrollX + "_scrollY_" + scrollY + "_oldScrollX_" + oldScrollX + "_oldScrollY_" + oldScrollY);
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
                            ShowAllusers();


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
*/
       /* lvnetworkfan.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            ShowAllusers();


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
        });*/
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                search.setHint("Pin");

            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    netWorkUserLists.clear();

                    s_name = v.getText().toString();
                    if (s_name.length() > 0) {
                        // requestedlayout.setVisibility(View.GONE);
                        Search(s_name);
                    } else {
                        if (jsonArrayfriendlist.length() > 0) {
                            // requestedlayout.setVisibility(View.VISIBLE);
                        }
                        netWorkUserLists.clear();
                        lvnetworkfan.removeAllViews();
                        ShowAllusers();
                    }
                    networkPeople_adapter.notifyDataSetChanged();
                    return true;
                }

                return false;
            }
        });
        search.addTextChangedListener(new TextWatcher() {

                                          @SuppressLint("DefaultLocale")
                                          @Override
                                          public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                                              s_name = arg0.toString();
                                              if (s_name.length() == 0) {
                                                  if (jsonArrayfriendlist.length() > 0) {
                                                      //  requestedlayout.setVisibility(View.VISIBLE);
                                                  }

                                                  ShowAllusers();

                                              }


                                          }

                                          @Override
                                          public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                                        int arg3) {

                                              // TODO Auto-generated method stub

                                          }

                                          @Override
                                          public void afterTextChanged(Editable arg0) {

                                              networkPeople_adapter.notifyDataSetChanged();
                                              swipeRefreshLayout.setRefreshing(false);
                                              // TODO Auto-generated method stub

                                          }
                                      }

        );
        serrch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Search.class);
                startActivity(intent);

            }
        });
        rides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RidesFragmnts.class);
                startActivity(intent);

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyProfile.class);
                startActivity(intent);

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Wall.class);
                startActivity(intent);

            }
        });
       /* chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Recent_Chat.class);
                startActivity(intent);

            }
        });*/

        return rootView;
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
                                // requestedlayout.setVisibility(View.GONE);
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
                                    searchPeople_adapter = new searchPeople_Adapter(getActivity(), requstedUserListArrayList);

                                    lvRequestedfanList.setAdapter(searchPeople_adapter);
                                    // requestedlayout.setVisibility(View.VISIBLE);
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

                params.put("action", "Showfriendrequest");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                Log.d("Showfriendrequest", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    private void showProgDialog2(Context context) {
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
                progress_dialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(getActivity());
            }
            progress_dialog.setMessage("loading");
            progress_dialog.setCancelable(true);
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

    private void Search(final String s_name) {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "loading...";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        Log.d("Searchuser", response.toString());
                        netWorkUserLists = new ArrayList<>();
                        JSONObject obj = null;
                        String user_status = "";
                        String type = "";
                        String Request_sent = "";
                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jsonArray = obj.getJSONArray("users");
                            if (jsonArray.length() == 0) {
                                // networklayot.setVisibility(View.GONE);
                                nonetworkfriend.setVisibility(View.VISIBLE);

                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jarray = jsonArray.getJSONObject(i);
                                    String user_id = jarray.getString("user_id");
                                    String fullname = jarray.getString("fullname");
                                    String nickname = jarray.getString("nickname");

                                    // String password = jarray.getString("password");
                                    String profileimage = jarray.getString("profileimage");
                                    //    String friend_status = jarray.getString("friend_status");
                                    if (jarray.has("user_status")) {
                                        Request_sent = jarray.getString("user_status");
                                    }
                                    if (jarray.has("type")) {
                                        type = jarray.getString("type");
                                    }
                                    if (jarray.has("user_status")) {
                                        user_status = jarray.getString("user_status");
                                    }


                                    Log.d("name", user_status);
                                    Log.d("name", nickname);

                                    netWorkUserList = new NetWorkUserList(user_id, fullname, nickname, Request_sent, user_status, type, profileimage);
                                    netWorkUserLists.add(netWorkUserList);
                                    networkPeople_adapter = new NetworkPeople_Adapter(getActivity(), netWorkUserLists);

                                    lvnetworkfan.setAdapter(networkPeople_adapter);
                                    // networklayot.setVisibility(View.VISIBLE);
                                    nonetworkfriend.setVisibility(View.GONE);


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
                        //   Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Searchuser");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("keywords", s_name);

                Log.d("Searchuser", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void ShowAllusers() {
        if (page == 0) {
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        swipeRefreshLayout.setRefreshing(false);
                        ArrayList<NetWorkUserList> list = new ArrayList<NetWorkUserList>();
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        hideProgDialog();
                        JSONObject obj = null;
                        Log.d("Showuserlist", response.toString());
                        String user_status = "";
                        String type = "";
                        String Request_sent = "";
                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jsonArray = obj.getJSONArray("users");
                            total_count = obj.getString("paging_count");
                            Log.d("Page_Count", "<<>>" + total_count);
                            if (jsonArray.length() == 0) {
                                if (jsonArray.length() == 0) {
                                    //  networklayot.setVisibility(View.GONE);
                                    nonetworkfriend.setVisibility(View.VISIBLE);
                                    swipeRefreshLayout.setVisibility(View.GONE);
                                }

                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jarray = jsonArray.getJSONObject(i);
                                    String user_id = jarray.getString("user_id");
                                    String fullname = jarray.getString("fullname");
                                    String nickname = jarray.getString("nickname");

                                    // String password = jarray.getString("password");
                                    String profileimage = jarray.getString("profileimage");
                                    //    String friend_status = jarray.getString("friend_status");
                                    if (jarray.has("user_status")) {
                                        Request_sent = jarray.getString("user_status");
                                    }
                                    if (jarray.has("type")) {
                                        type = jarray.getString("type");
                                    }
                                    if (jarray.has("user_status")) {
                                        user_status = jarray.getString("user_status");
                                    }


                                    Log.d("name", user_status);
                                    Log.d("name", nickname);

                                    netWorkUserList = new NetWorkUserList(user_id, fullname, nickname, Request_sent, user_status, type, profileimage);
                                    //  networklayot.setVisibility(View.VISIBLE);
                                    nonetworkfriend.setVisibility(View.GONE);
                                    list.add(netWorkUserList);
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);

                                }
                                if (page == 0) {
                                    netWorkUserLists.addAll(list);
                                    try {

                                        if (netWorkUserLists.size() > 0) {
                                            lvnetworkfan.setVisibility(View.VISIBLE);
                                            networkPeople_adapter = new NetworkPeople_Adapter(getActivity(), netWorkUserLists);
                                            lvnetworkfan.setAdapter(networkPeople_adapter);
                                            swipeRefreshLayout.setRefreshing(false);
                                        } else {
                                            //  lvnetworkfan.setVisibility(View.GONE);
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

                params.put("action", "Showuserlist");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("page", String.valueOf(page));
                Log.d("Showuserlist", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

   /* public static void repost(final Context context) {
        showProgDialog2(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        ArrayList<NetWorkUserList> list = new ArrayList<NetWorkUserList>();
                        JSONObject obj = null;
                        Log.d("Showuserlist", response.toString());
                       /// swipeRefreshLayout.setRefreshing(false);
                        hideProgDialog();
                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jsonArray = obj.getJSONArray("users");
                            total_count = obj.getString("paging_count");
                            Log.d("Page_Count", "<<>>" + total_count);
                            if (jsonArray.length() == 0) {
                                if (jsonArray.length() == 0) {
                                   // networklayot.setVisibility(View.GONE);
                                    nonetworkfriend.setVisibility(View.VISIBLE);
                                   // swipeRefreshLayout.setVisibility(View.GONE);
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


                                    Log.d("name", fullname);

                                    netWorkUserList = new NetWorkUserList(user_id, fullname, nickname, Request_sent, user_status, type, profileimage);
                                  //  networklayot.setVisibility(View.VISIBLE);
                                    nonetworkfriend.setVisibility(View.GONE);
                                    list.add(netWorkUserList);
                                   // swipeRefreshLayout.setVisibility(View.VISIBLE);

                                }
                                if (page == 0) {
                                    netWorkUserLists.addAll(list);
                                    try {

                                        if (netWorkUserLists.size() > 0) {
                                            lvnetworkfan.setVisibility(View.VISIBLE);
                                            networkPeople_adapter = new NetworkPeople_Adapter(context, netWorkUserLists);
                                            lvnetworkfan.setAdapter(networkPeople_adapter);
                                           // swipeRefreshLayout.setRefreshing(false);
                                        } else {
                                          //  lvnetworkfan.setVisibility(View.GONE);
                                           // nonetworkfriend.setVisibility(View.VISIBLE);

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
                      *//*  mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
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
*//*
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Showuserlist");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("page", String.valueOf(page));
                Log.d("Showuserlist", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }*/

    public void repostFriendlist(final Context context) {
        //  showProgDialog2(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        hideProgDialog();
                        requstedUserListArrayList = new ArrayList<>();
                        JSONObject obj = null;
                        Log.d("Showfriendrequest", response.toString());

                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jsonArray = obj.getJSONArray("result");
                            if (jsonArray.length() == 0) {
                                //  requestedlayout.setVisibility(View.GONE);
                                norequestfound.setVisibility(View.VISIBLE);


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

                                    requestedUserList = new RequstedUserList(user_id, fullname, nickname, email, verify_code, type, profileimage);
                                    requstedUserListArrayList.add(requestedUserList);
                                    searchPeople_adapter = new searchPeople_Adapter(context, requstedUserListArrayList);

                                    lvRequestedfanList.setAdapter(searchPeople_adapter);
                                    //  requestedlayout.setVisibility(View.VISIBLE);
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

                params.put("action", "Showfriendrequest");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                //  params.put("page", String.valueOf(page));
                Log.d("Showfriendrequest", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    public static Search newInstance(ArrayList<NetWorkUserList> netWorkUserLists) {
        Search posts = new Search();
        Bundle args = new Bundle();
        // args.putParcelableArrayList(ARG_PERSON_LIST, (ArrayList<? extends Parcelable>) userPostsArrayList);
        posts.setArguments(args);
        return posts;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        netWorkUserLists = new ArrayList<NetWorkUserList>();


    }
}



