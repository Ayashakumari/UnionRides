package mx.bigapps.unionrides.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Adapter.RecentChat_Adapter;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.RecentChatEntity;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;

public class Recent_Chat extends Fragment {

    static SwipeMenuListView chat_recycle;
    TextView header_txt;
     TextView error_msg;
    ImageView header_menu, add, header_logo;
    RelativeLayout header;
    ArrayList<String> chat_list = new ArrayList<String>();
     RecentChat_Adapter recentChat_adapter;
    LinearLayoutManager layoutManager;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    static ArrayList<RecentChatEntity> recent_chatlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat, container, false);
        error_msg = (TextView) rootView.findViewById(R.id.error_msg);

        chat_recycle = (SwipeMenuListView) rootView.findViewById(R.id.chat_recycle);
        add = (ImageView) rootView.findViewById(R.id.add);

        recent_chatlist = new ArrayList<>();
        showRecentChat();
      /*  SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                //create an action that will be showed on swiping an item in the list
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getActivity());
                item1.setBackground(new ColorDrawable(Color.RED));
                // set width of an option (px)
                item1.setWidth(450);
                item1.setTitle("Delete");
                item1.setTitleSize(18);
                item1.setTitleColor(Color.WHITE);
                menu.addMenuItem(item1);


            }
        };
        //set MenuCreator
        chat_recycle.setMenuCreator(creator);*/
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AllcontactList.class));
            }
        });
        chat_recycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent i2 = new Intent(getActivity(), Chat_window.class);
                i2.putExtra("firstname", recent_chatlist.get(i).getFull_name());
                i2.putExtra("friend_id", recent_chatlist.get(i).getFriend_id());
                i2.putExtra("project_id", recent_chatlist.get(i).getProject_id());
                i2.putExtra("status", "chat");
                startActivity(i2);
            }
        });
       /* chat_recycle.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                // String value = (String) RecentChat_Adapter.getItem(position);
                switch (index) {
                    case 0:

                        AlertDialog.Builder alert;
                        if (Build.VERSION.SDK_INT >= 11) {
                            alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                        } else {
                            alert = new AlertDialog.Builder(getActivity());
                        }
                        alert.setTitle("Alert!");
                        alert.setMessage("Do you want to delete chat");


                        alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {

                                    if (recent_chatlist.size() == 1) {

                                        chat_recycle.setVisibility(View.GONE);
                                        error_msg.setText("No Chats Available");
                                        error_msg.setVisibility(View.VISIBLE);
                                    }
                                    deleteChat(recent_chatlist.get(position).getFriend_id());

                                    recent_chatlist.remove(position);

                                    recentChat_adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        });

                        try {
                            Dialog dialog = alert.create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        break;
                    case 1:
                        // Toast.makeText(getActivity(), "Action 2 for " + value, Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
*/

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

    private void showRecentChat() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "loading...";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        Log.d("Showrecentchatmesage", response.toString());
                        JSONObject obj = null;
                        recent_chatlist = new ArrayList<RecentChatEntity>();
                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jArray = obj.getJSONArray("show");
                            Log.d("Json_Array", "" + jArray);
                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonObject = jArray.getJSONObject(i);
                                Log.d("Object", "" + jsonObject);
                                RecentChatEntity entity = new RecentChatEntity();

                                entity.setFull_name(jsonObject.getString("full_name"));
                                entity.setChat_id(jsonObject.getString("chat_id"));
                                entity.setFriend_id(jsonObject.getString("user_id"));
                                entity.setPicture(jsonObject.getString("picture"));
                                entity.setUnreade_count(jsonObject.getString("unreade_count"));
                                entity.setDate_added(jsonObject.getString("date_added"));
                                entity.setMessage(jsonObject.getString("message"));
                                entity.setTimee(jsonObject.getString("timee"));
                                recent_chatlist.add(entity);
                            }

                            if (recent_chatlist.size() > 0) {
                                recentChat_adapter = new RecentChat_Adapter(getActivity(), recent_chatlist);
                                chat_recycle.setAdapter(recentChat_adapter);
                                chat_recycle.setVisibility(View.VISIBLE);
                            } else {
                                chat_recycle.setVisibility(View.GONE);
                                error_msg.setText("No Chats Available");
                                error_msg.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("action", "Showrecentchatmesage");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                Log.d("Showrecentchatmesage", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void deleteChat(final String friend_id) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Deletechathistory", response.toString());
                        JSONObject obj = null;


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
                params.put("action", "Deletechathistory");
                params.put("friend_id", friend_id);
                params.put("user_id", PrefMangr.getInstance().getUserId());
                Log.d("Deletechathistory", String.valueOf(params));
                return params;
            }
//
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public  void refreshRecentChat(final Context context) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Showrecentchatmesage", response.toString());
                        JSONObject obj = null;
                        recent_chatlist = new ArrayList<RecentChatEntity>();
                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jArray = obj.getJSONArray("show");
                            Log.d("Json_Array", "" + jArray);
                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonObject = jArray.getJSONObject(i);
                                Log.d("Object", "" + jsonObject);
                                RecentChatEntity entity = new RecentChatEntity();

                                entity.setFull_name(jsonObject.getString("full_name"));
                                entity.setChat_id(jsonObject.getString("chat_id"));
                                entity.setFriend_id(jsonObject.getString("user_id"));
                                entity.setPicture(jsonObject.getString("picture"));
                                entity.setUnreade_count(jsonObject.getString("unreade_count"));
                                entity.setDate_added(jsonObject.getString("date_added"));
                                entity.setMessage(jsonObject.getString("message"));
                                entity.setTimee(jsonObject.getString("timee"));


                                recent_chatlist.add(entity);
                            }

                            if (recent_chatlist.size() > 0) {
                                recentChat_adapter = new RecentChat_Adapter(context, recent_chatlist);
                                chat_recycle.setAdapter(recentChat_adapter);
                                chat_recycle.setVisibility(View.VISIBLE);
                            } else {
                                chat_recycle.setVisibility(View.GONE);
                                error_msg.setText("No Chats Available");
                                error_msg.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("action", "Showrecentchatmesage");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                Log.d("Showrecentchatmesage", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
}
