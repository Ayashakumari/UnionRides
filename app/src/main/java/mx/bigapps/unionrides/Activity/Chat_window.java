package mx.bigapps.unionrides.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;

import mx.bigapps.unionrides.Adapter.Chat_Adapter;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.GCMIntentService;
import mx.bigapps.unionrides.Model.ChatHistory_Entity;
import mx.bigapps.unionrides.Model.SupportEntity;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;


public class Chat_window extends AppCompatActivity {
    RelativeLayout chat_window_header;
    ImageView chat_img;
    int i = 0;
    TextView chat_name_txt;
    ListView list_chat;
    String status_online_offline;
    String hiii, state = "", project_status, user_type;
    final Handler ha = new Handler();
    Chat_Adapter chatAdapter;
    ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    JSONObject jsonObject = new JSONObject();
    ArrayList<ChatHistory_Entity> chat_list;
    ArrayList<SupportEntity> support_list = new ArrayList<SupportEntity>();

    int flagvalue;
    String action = "", friend_id = "", message, msg, firstname, lastname, friend_image = "", fullname, time;
    public static final String Prefs_remember_value = "UserTypeValues";
    SharedPreferences preferences2;
    EditText chat_edit;
    ImageView send_button, header_go;
    TextView error_msg, txt_online;
    RelativeLayout activity_chat_window;
    Timer timer;
    TextView header_txt;
    ImageView header_menu, header_back, header_logo;
    String status, email, aboutme, mobile, is_friend, friendstatus = "", userid_chat = "", admin_id = "";
    String auto_reply_message_doctor;
    String receiver_status_online_offline;
    RelativeLayout relative_online;
    public static String myid;
    public static String friendid;
    public static int checkview = 0;
    String formattedDate, order;
    int gmtvalue;
    String project_id, typingstatus = "0";
    ImageView header_addappointment;
    RelativeLayout header;

    TextView chat_status;
    String otheruser_id, typing_status, user_id, user_name;
    Recent_Chat recent_chat=new Recent_Chat();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_window);
        helper.cat_name3.clear();
        helper.setchatno = "";
        header_back = (ImageView) findViewById(R.id.header_back);

        Intent i = getIntent();
        fullname = i.getStringExtra("firstname");
        project_id = i.getStringExtra("project_id");
        friend_id = i.getStringExtra("friend_id");
        state = i.getStringExtra("status");
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state != null) {
                    recent_chat.refreshRecentChat(Chat_window.this);
                    helper.checkview = 0;
                    typingstatus = "0";
                    ShowAddTyping();
                    ha.removeCallbacksAndMessages(null);
                    chat_status.setVisibility(View.GONE);
                    finish();

                } else {
                    helper.checkview = 0;
                    typingstatus = "0";
                    ShowAddTyping();
                    ha.removeCallbacksAndMessages(null);
                    chat_status.setVisibility(View.GONE);
                    finish();
                }


            }
        });

        Log.d("FULLNAME CHAT", "" + project_id);
        error_msg = (TextView) findViewById(R.id.error_msg);
        chat_status = (TextView) findViewById(R.id.chat_status);
        activity_chat_window = (RelativeLayout) findViewById(R.id.activity_chat_window);
        relative_online = (RelativeLayout) findViewById(R.id.relative_online);
        txt_online = (TextView) findViewById(R.id.txt_online);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        send_button = (ImageView) findViewById(R.id.send_button);
        chat_name_txt = (TextView) findViewById(R.id.header_chattxt);
        chat_name_txt.setVisibility(View.VISIBLE);
        list_chat = (ListView) findViewById(R.id.recent_chat_recycle);

        TimeZone tz = TimeZone.getDefault();
        Log.d("shahhs", "" + tz.getDisplayName(false, TimeZone.SHORT));
        TimeZone tzzz = TimeZone.getDefault();
        Date now = new Date();
        gmtvalue = tzzz.getOffset(now.getTime()) / 1000;
        Log.d("111111", "" + gmtvalue);
        Log.d("gmt", String.valueOf(gmtvalue));
        helper.friendid = friend_id;
        helper.checkview = 1;
        chat_name_txt.setText(fullname);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        formattedDate = df.format(c.getTime());
        Log.d("Time", "" + state);
        Showchathistory();

        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //  chat_status.setVisibility(View.GONE);
                Get_TypingList();
               // helper.cat_name3.clear();
               // Showchathistory();
                ha.postDelayed(this, 2000);
            }
        }, 2000);


        chat_edit.addTextChangedListener(new TextWatcher() {

                                             @SuppressLint("DefaultLocale")
                                             @Override
                                             public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                                                 if (chat_edit.getText().toString().length() > 0) {
                                                     typingstatus = "1";
                                                     ShowAddTyping();
                                                 } else {
                                                     typingstatus = "0";
                                                     ShowAddTyping();
                                                     // chat_status.setVisibility(View.GONE);
                                                 }

                                             }

//                                            }

                                             @Override
                                             public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                                           int arg3) {

                                                 // TODO Auto-generated method stub
//                                                         chat_edit.setText("You will pay : $ "+"0");


                                             }

                                             @Override
                                             public void afterTextChanged(Editable arg0) {
                                                 // chat_status.setVisibility(View.GONE);
                                                 // TODO Auto-generated method stub

                                             }
                                         }

        );


        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = chat_edit.getText().toString();
                helper.setchatno = "1";
                if (state == null) {
                    ChatHistory_Entity ent = new ChatHistory_Entity();
                    ent.setMessage(message);
                    ent.setFromUsers_id(PrefMangr.getInstance().getUserId());
                    ent.setToUsers_id(friend_id);
                    ent.setStatus("sent");
                    SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm a");//dd/MM/yyyy
                    Date now = new Date();
                    String strDate = sdfDate.format(now);
                    ent.setTimee(strDate);
                    chat_list.add(ent);
                    // chatAdapter.notifyDataSetChanged();
                    chat_edit.setText("");
                    sendMessage();
                }
                if (state != null) {
                    if (state.equals("contactchat")) {
                        ChatHistory_Entity ent = new ChatHistory_Entity();
                        ent.setMessage(message);
                        ent.setFromUsers_id(PrefMangr.getInstance().getUserId());
                        ent.setToUsers_id(friend_id);
                        ent.setStatus("sent");
                        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm a");//dd/MM/yyyy
                        Date now = new Date();
                        String strDate = sdfDate.format(now);
                        ent.setTimee(strDate);
                        chat_list.add(ent);
                        // chatAdapter.notifyDataSetChanged();
                        chat_edit.setText("");
                        sendMessage();
                    } else {
                        try {
                            if (!message.equals("")) {
                                ChatHistory_Entity ent = new ChatHistory_Entity();
                                ent.setMessage(message);
                                ent.setFromUsers_id(PrefMangr.getInstance().getUserId());
                                ent.setToUsers_id(friend_id);
                                ent.setStatus("sent");
                                SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm a");//dd/MM/yyyy
                                Date now = new Date();
                                String strDate = sdfDate.format(now);
                                ent.setTimee(strDate);
                                String date_selected;
                                Calendar metFromdate1 = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    metFromdate1.setTime(dateFormat.parse(strDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                date_selected = df.format(metFromdate1.getTime());
                                ent.setTime(date_selected);
                                chat_list.add(ent);
                                chatAdapter.notifyDataSetChanged();
                                chat_edit.setText("");
                                list_chat.smoothScrollToPosition(chatAdapter.getCount());
                                sendMessage();
                            }
                        } catch (Exception e) {

                        }
                    }
                }

            }
        });


    }

    private void ShowAddTyping() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ShowAddTyping", response.toString());
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            msg = obj.getString("msg");

                            if (msg.equals("Add successfully")) {


                            }
                        } catch (JSONException e) {
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
                params.put("action", "Addtyping");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("other_user_id", friend_id);
                params.put("user_name", fullname);
                params.put("typing_status", typingstatus);
                Log.d("Addtyping", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Get_TypingList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Get_TypingList", response.toString());
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());

                            ArrayList<SupportEntity> list = new ArrayList<SupportEntity>();

                            JSONArray jsonParentNode = obj.getJSONArray("show");
                            Log.d("SHOWWWWW", jsonParentNode.toString());

                            if (jsonParentNode.length() == 0) {
                                Log.d("aaaaa", "aaaa");
                                chat_status.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < jsonParentNode.length(); i++) {
                                    JSONObject jsonChildNode = jsonParentNode.getJSONObject(i);

                                    SupportEntity chatHistory_entity = new SupportEntity();
                                    chatHistory_entity.setOtheruser_id(jsonChildNode.getString("other_user_id"));
                                    chatHistory_entity.setUser_id(jsonChildNode.getString("user_id"));
                                    chatHistory_entity.setUser_name(jsonChildNode.getString("user_name"));
                                    chatHistory_entity.setTyping_status(jsonChildNode.getString("typing_status"));
                                    list.add(chatHistory_entity);
                                    if (jsonParentNode.length() != 0) {
                                        otheruser_id = jsonChildNode.getString("other_user_id");
                                        user_id = jsonChildNode.getString("user_id");
                                        user_name = jsonChildNode.getString("user_name");
                                        typing_status = jsonChildNode.getString("typing_status");

                                        Log.d("otherid", "" + otheruser_id);
                                        Log.d("username", "" + user_name);
                                        Log.d("TYPINGSTATUS", "" + typing_status);

                                        Log.d("USERID", "" + user_id);
                                        Log.d("USERID1111", "" + PrefMangr.getInstance().getUserId());

                                        if (typing_status.equals("1")) {
                                            if (otheruser_id.equals(PrefMangr.getInstance().getUserId())) {
                                                chat_status.setVisibility(View.VISIBLE);
                                            }


                                        } else if (typing_status.equals("0")) {
                                            chat_status.setVisibility(View.GONE);
                                        }


                                    }

                                }

                                support_list.addAll(list);

                            }
                        } catch (JSONException e) {
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
                params.put("action", "Gettypinglist");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("other_user_id", friend_id);
                Log.d("Gettypinglist", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    private void Showchathistory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Jsonshowchat", response.toString());
                        JSONObject obj = null;
                        chat_list = new ArrayList<ChatHistory_Entity>();
                        try {
                            obj = new JSONObject(response.toString());
                            ArrayList<ChatHistory_Entity> list = new ArrayList<ChatHistory_Entity>();
//
                            JSONArray jsonParentNode = obj.getJSONArray("show");
                            user_type = obj.getString("user_type");
                            for (int i = 0; i < jsonParentNode.length(); i++) {
                                JSONObject jsonChildNode = jsonParentNode.getJSONObject(i);
                                ChatHistory_Entity chatHistory_entity = new ChatHistory_Entity();
                                chatHistory_entity.setDatee(jsonChildNode.getString("datee"));
                                chatHistory_entity.setFromUsers_id(jsonChildNode.getString("fromUsers_id"));
                                chatHistory_entity.setId(jsonChildNode.getString("id"));
                                chatHistory_entity.setMessage(jsonChildNode.getString("message"));
                                chatHistory_entity.setNew_msg(jsonChildNode.getString("new"));
                                chatHistory_entity.setStatus(jsonChildNode.getString("status"));
                                chatHistory_entity.setTime(jsonChildNode.getString("time"));
                                chatHistory_entity.setPic(jsonChildNode.getString("pic"));
                                chatHistory_entity.setTimee(jsonChildNode.getString("timee"));
                                chatHistory_entity.setToUsers_id(jsonChildNode.getString("toUsers_id"));

                                Calendar metFromdate1 = Calendar.getInstance();

                                String date_selected = "";
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    metFromdate1.setTime(dateFormat.parse(jsonChildNode.getString("time")));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                date_selected = df.format(metFromdate1.getTime());

                                if (helper.cat_name3.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ",").contains(date_selected)) {
                                    Log.d("1", "1");
                                    chatHistory_entity.setNo("1");
                                } else {
                                    chatHistory_entity.setNo("2");

                                    helper.cat_name3.add(date_selected);
                                }
                                list.add(chatHistory_entity);
                            }
                            chat_list.addAll(list);
                            if (chat_list.size() != 0) {
                                list_chat.setVisibility(View.VISIBLE);
                                chatAdapter = new Chat_Adapter(Chat_window.this, chat_list);
                                list_chat.setAdapter(chatAdapter);
                                list_chat.setSelection(chat_list.size());
                                list_chat.smoothScrollToPosition(chatAdapter.getCount());
                                //scrollMyListViewToBottom();

                            } else {

                                list_chat.setVisibility(View.GONE);
                                error_msg.setVisibility(View.VISIBLE);
                                error_msg.setText("No Chats Found");

                            }
                        } catch (JSONException e) {
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
                params.put("action", "Showchathistory");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("friend_id", friend_id);
                Log.d("CHATSHOWMESSAGE", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void sendMessage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("JsonData", response.toString());
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            error_msg.setVisibility(View.GONE);
                            list_chat.setVisibility(View.VISIBLE);
                            msg = obj.getString("msg");
                            Showchathistory();

                        } catch (JSONException e) {
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
                params.put("action", "Chatmessage");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("friend_id", friend_id);
                params.put("message", message);

                Log.d("CHATSENDMEASSAGE", String.valueOf(params));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 100, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private final BroadcastReceiver HandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String from_user_id = intent.getExtras().getString("friend_id");
            String user_name = intent.getExtras().getString("firstname");
            //  String project_id = intent.getExtras().getString("project_id");
            String time = intent.getExtras().getString("time");
            String user_photo = intent.getExtras().getString("friend_image");
            String message = intent.getExtras().getString("message");

            Log.d("from_user_id", "AA" + from_user_id);
            Log.d("user_name", "AA" + user_name);
            Log.d("message", "AA" + message);
            Log.d("project_id", "AA" + project_id);
            Log.d("time", "AA" + time);

            ChatHistory_Entity ent = new ChatHistory_Entity();
            ent.setMessage(message);
            ent.setFromUsers_id(from_user_id);
//            ent.setToUsers_id(friend_id);
            ent.setStatus("received");
            ent.setNo("1");
            ent.setTime(time);
            chat_list.add(ent);
            chatAdapter.notifyDataSetChanged();

            list_chat.smoothScrollToPosition(chatAdapter.getCount());


        }
    };

    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(HandleMessageReceiver);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    ;

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentfilter = new IntentFilter(GCMIntentService.MESSAGE_ACTION);
        registerReceiver(HandleMessageReceiver, intentfilter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (state != null) {
            recent_chat.refreshRecentChat(Chat_window.this);
            helper.checkview = 0;
            typingstatus = "0";
            ShowAddTyping();
            ha.removeCallbacksAndMessages(null);
            chat_status.setVisibility(View.GONE);
            finish();

        } else {
            helper.checkview = 0;
            typingstatus = "0";
            ShowAddTyping();
            ha.removeCallbacksAndMessages(null);
            chat_status.setVisibility(View.GONE);
            finish();
        }

    }

    protected void onStop() {
        super.onStop();

        ha.removeCallbacksAndMessages(null);

//        ha.removeCallbacks(Runnable);
    }


}
