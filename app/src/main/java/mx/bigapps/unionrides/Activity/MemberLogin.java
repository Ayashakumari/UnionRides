package mx.bigapps.unionrides.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Fragment.PostsWall;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;

/**
 * Created by admin on 01-11-2017.
 */
public class MemberLogin extends AppCompatActivity {
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    EditText acct_email, password;
    ImageView cross;
    TextView forgetPassword;
    ImageView welcome_login;
    int gmtvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_login);
        TimeZone tzzz = TimeZone.getDefault();
        Date now = new Date();
        gmtvalue = tzzz.getOffset(now.getTime()) / 1000;
        Log.d("111111", "" + gmtvalue);
        Log.d("gmt", String.valueOf(gmtvalue));
        welcome_login = (ImageView) findViewById(R.id.welcome_login);
        acct_email = (EditText) findViewById(R.id.acct_email);
        password = (EditText) findViewById(R.id.password);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemberLogin.this, Reset_password.class));

            }
        });
        Refreshtoken refreshtoken = new Refreshtoken();
        Thread t = new Thread(refreshtoken);
        t.start();
        welcome_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String e = acct_email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(e) || !Patterns.EMAIL_ADDRESS.matcher(e).matches()) {

                    Toast.makeText(MemberLogin.this, " Enter valid Email", Toast.LENGTH_LONG).show();

                    return;
                } else if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(MemberLogin.this, " Enter Password", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    //startActivity(new Intent(getApplicationContext(), Wall.class));
                    Login(e, pass);
                }
                // startActivity(new Intent(getApplicationContext(), Wall.class));
            }
        });
        cross = (ImageView) findViewById(R.id.cross);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemberLogin.this, Welcome.class));
            }
        });
    }

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(MemberLogin.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(MemberLogin.this);
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

    private void Login(final String e, final String pass) {
        HttpsTrustManager.allowAllSSL();
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
                        Log.d("Login", response.toString());

                        try {
                            obj = new JSONObject(response.toString());
                            msg = obj.getString("msg");
                            showAlert(msg);
                            JSONObject jarray = obj.getJSONObject("user_details");
                            String user_id = jarray.getString("user_id");
                            String fullname = jarray.getString("fullname");
                            String nickname = jarray.getString("nickname");
                            String email = jarray.getString("email");
                            String password = jarray.getString("password");
                            String type = jarray.getString("type");
                            String profileimage = jarray.getString("profileimage");
                            PrefMangr.getInstance().setUSerId(user_id);
                            PrefMangr.getInstance().setUserName(fullname);
                            PrefMangr.getInstance().setUsernickName(nickname);
                            PrefMangr.getInstance().setUseremail(email);
                            PrefMangr.getInstance().setPassword(password);
                            PrefMangr.getInstance().setuserType(type);
                            PrefMangr.getInstance().setProfilepic(profileimage);


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
                            Log.d("error", String.valueOf(error));
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(MemberLogin.this, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(MemberLogin.this, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(MemberLogin.this, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(MemberLogin.this, "Network Error", Toast.LENGTH_LONG).show();
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

                params.put("action", "Login");
                params.put("email", e);
                params.put("password", pass);
                params.put("gmt_value", String.valueOf(gmtvalue));
                params.put("device_id", PrefMangr.getInstance().getDeviceId());
                params.put("device_type", "android");

                Log.d("Login", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void showAlert(final String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemberLogin.this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (msg.equals("Login successfully")) {
                    PostsWall postsWall=new PostsWall();
                    if(postsWall.userPost!=null){

                        postsWall.refresh = "1";
                        Log.d("refreshh", "333" + postsWall.refresh);
                        postsWall.userPost.removeAllViews();
                        postsWall.userPost.removeAllViewsInLayout();
                        postsWall.loading = true;
                        postsWall.previousTotal = 0;
                        postsWall.visibleThreshold = 5;
                        postsWall.firstVisibleItem = 0;
                        postsWall.visibleItemCount = 0;
                        postsWall.totalItemCount = 0;
                        postsWall.page = 0;
                        postsWall.userPostsArrayList.clear();

                        postsWall.flagvalue1 = postsWall.flagvalue = 0;
                       // postsWall.showAllPostsscroll(MemberLogin.this, 0);
                    }
                    startActivity(new Intent(getApplicationContext(), Wall.class));

                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MemberLogin.this, Welcome.class));
    }
}
