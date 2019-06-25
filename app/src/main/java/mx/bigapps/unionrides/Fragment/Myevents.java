package mx.bigapps.unionrides.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import mx.bigapps.unionrides.Adapter.Myevents_Adapter;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.AlleventData;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;

/**
 * Created by admin on 01-11-2017.
 */
public class Myevents extends Fragment {
    RecyclerView photo_recycle;
    LinearLayoutManager layoutManager;
    Myevents_Adapter myevents_adapter;
    ArrayList photolist;
    CircleImageView profile_img;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    AlleventData alleventData;
    ArrayList<AlleventData> alleventDataArrayList;
    TextView noeventfound;
    SwipeRefreshLayout swipeRefreshLayout;
    String total_count = "";
    String refresh = "";
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount, pastVisiblesItems;
    static int page = 0;
    int flagvalue, flagvalue1, curSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.events, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.my_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.piink, R.color.darkpurple, R.color.green, R.color.orange);
        noeventfound = (TextView) rootView.findViewById(R.id.noeventfound);
        photo_recycle = (RecyclerView) rootView.findViewById(R.id.photolist);
        layoutManager = new LinearLayoutManager(getActivity());
        photo_recycle.setLayoutManager(layoutManager);
        layoutManager.onSaveInstanceState();
        photolist = new ArrayList();
        allEventList();


        photo_recycle.setHasFixedSize(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = "1";
                Log.d("refreshh", "333" + refresh);
                photo_recycle.removeAllViews();
                photo_recycle.removeAllViewsInLayout();
                loading = true;
                previousTotal = 0;
                visibleThreshold = 5;
                firstVisibleItem = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                page = 0;
                alleventDataArrayList.clear();
                flagvalue1 = flagvalue = 0;
                allEventList();
            }
        });
        photo_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = photo_recycle.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                Log.d("visibleItemCount", "" + visibleItemCount);
                Log.d("totalItemCount", "" + totalItemCount);
                Log.d("firstVisibleItem", "" + firstVisibleItem);
                Log.d("LogEntity1.size()", "" + alleventDataArrayList.size());
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
                        if (alleventDataArrayList.size() < Integer.parseInt(total_count)) {
                            Log.d("page2", "" + page);

                            page = page + 1;
                            allEventList();


                        } else {
                           /* Toast.makeText(getActivity(),
                                    "End of list", Toast.LENGTH_SHORT).show();*/
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    loading = true;
                }


            }
        });
        return rootView;
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

    private void allEventList() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "loading...";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        alleventDataArrayList = new ArrayList();
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        ArrayList<AlleventData> list = new ArrayList<AlleventData>();
                        JSONObject obj = null;
                        String image320="";
                        Log.d("Showallevents", response.toString());

                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jsonArray = obj.getJSONArray("users");
                            total_count = obj.getString("paging_count");
                            Log.d("Page_Count", "<<>>" + total_count);
                            if (jsonArray.length() == 0) {
                                photo_recycle.setVisibility(View.GONE);
                                noeventfound.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setVisibility(View.GONE);

                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jarray = jsonArray.getJSONObject(i);
                                    String user_id = jarray.getString("user_id");
                                    String event_id = jarray.getString("event_id");
                                    String event_title = jarray.getString("event_title");
                                    String start_date = jarray.getString("start_date");
                                    String start_time = jarray.getString("start_time");

                                    String end_date = jarray.getString("end_date");
                                    String end_time = jarray.getString("end_time");
                                    String description = jarray.getString("description");
                                    String profiletype = jarray.getString("public");

                                    String event_location = jarray.getString("event_location");
                                    String event_image = jarray.getString("event_image");
                                    String latitude = jarray.getString("latitude");
                                    String longitude = jarray.getString("longitude");
                                    String date_added = jarray.getString("date_added");
                                    String posted_by = jarray.getString("posted_by");
                                    String posted_time = jarray.getString("posted_time");
                                    String profile_image = jarray.getString("profile_image");
                                    String my_event = jarray.getString("my_event");
                                    String event_join_status = jarray.getString("event_join_status");
                                    String eventjoinCount = jarray.getString("eventjoinCount");
                                    JSONArray image_thumb_size = jarray.getJSONArray("event_image_thumbs");
                                    if (image_thumb_size.length() != 0) {
                                        JSONObject jarrayimage = image_thumb_size.getJSONObject(0);
                                        image320 = jarrayimage.getString("320_320");
                                    }
                                    String user_profile_status;
                                    if (helper.status.equals("otherprofile_event")) {
                                        user_profile_status = "1";
                                    } else {
                                        user_profile_status = "0";
                                    }
                                    alleventData = new AlleventData(event_id, user_id, event_title, start_date, start_time, end_date, end_time, description, profiletype, event_location, event_image, latitude, longitude, date_added, posted_by, posted_time, profile_image, my_event, event_join_status, user_profile_status, eventjoinCount, image320);
                                    photo_recycle.setVisibility(View.VISIBLE);
                                    noeventfound.setVisibility(View.GONE);
                                    list.add(alleventData);
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);


                                    //nofriends.setVisibility(View.GONE);
                                }
                                if (page == 0) {
                                    alleventDataArrayList.addAll(list);
                                    try {

                                        if (alleventDataArrayList.size() > 0) {
                                            photo_recycle.setVisibility(View.VISIBLE);
                                            myevents_adapter = new Myevents_Adapter(getActivity(), alleventDataArrayList);
                                            photo_recycle.setAdapter(myevents_adapter);

                                            swipeRefreshLayout.setRefreshing(false);
                                        } else {
                                            photo_recycle.setVisibility(View.GONE);
                                            noeventfound.setVisibility(View.VISIBLE);
                                            swipeRefreshLayout.setVisibility(View.GONE);
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    curSize = alleventDataArrayList.size();
                                    myevents_adapter.notifyDataSetChanged();
                                } else {

                                    curSize = alleventDataArrayList.size();
                                    alleventDataArrayList.addAll(curSize, list);
                                    myevents_adapter.notifyItemInserted(curSize);
                                    myevents_adapter.notifyItemRangeChanged(curSize, alleventDataArrayList.size());
                                    myevents_adapter.notifyDataSetChanged();
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

                params.put("action", "Showallevents");
                if (helper.status.equals("otherprofile_event")) {
                    params.put("user_id", helper.user_id);
                    params.put("my_user_id", PrefMangr.getInstance().getUserId());
                } else {
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                }
                params.put("type", "Myevents");

                params.put("page", String.valueOf(page));
                Log.d("Showallevents", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }
}
