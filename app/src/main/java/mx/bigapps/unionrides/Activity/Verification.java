package mx.bigapps.unionrides.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Application.AppController;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;

public class Verification extends AppCompatActivity {

    TextView verify_code1, verify_code2, verify_code3, verify_code4, verify_txt, resend_now_txt;
    ImageView header_menu, header_back, header_logo;
    TextView header_txt;
    String msg, otp;
    String otp1, otp2, otp3, otp4;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    String user_id, facebook_id, username, full_name, profile_name, profile_description, email_address, calling_code, mobile_number, password, profile_pic_url, background_pic_url,
            login_status, active_status, device_id, is_verified, verify_code, gmt_value, push_notification, email_notification, subscribed_plan, bid_limit, bid_used, subscribed_date, project_fee_percent, added_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);


        verify_code1 = (TextView) findViewById(R.id.verify_code1);
        verify_code2 = (TextView) findViewById(R.id.verify_code2);
        verify_code3 = (TextView) findViewById(R.id.verify_code3);
        verify_code4 = (TextView) findViewById(R.id.verify_code4);
        verify_txt = (TextView) findViewById(R.id.verify_txt);
        resend_now_txt = (TextView) findViewById(R.id.resend_now_txt);


        resend_now_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // ResendOtp();
            }


        });


        verify_code1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (verify_code1.getText().toString().length() == 1)     //size as per your requirement
                {
                    verify_code2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                otp1 = verify_code1.getText().toString();
                Log.d("otp1", "" + otp1);
                // TODO Auto-generated method stub
            }


        });

//        verify_code1.addTextChangedListener(new TextWatcher() {
//
//            public void onTextChanged(CharSequence s, int start,int before, int count)
//            {
//                // TODO Auto-generated method stub
//                if(verify_code1.getText().toString().length()==1)     //size as per your requirement
//                {
//                    verify_code2.requestFocus();
//                }
//            }
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//                // TODO Auto-generated method stub
//
//            }
//
//            public void afterTextChanged(Editable s) {
//
//                // TODO Auto-generated method stub
//            }
//
//        });

        verify_code2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (verify_code2.getText().toString().length() == 1)     //size as per your requirement
                {
                    verify_code3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {

                otp2 = verify_code2.getText().toString();
                Log.d("otp2", "" + otp2);


                // TODO Auto-generated method stub
            }


        });

        verify_code3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (verify_code3.getText().toString().length() == 1)     //size as per your requirement
                {
                    verify_code4.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                otp3 = verify_code3.getText().toString();
                Log.d("otp3", "" + otp3);
                // TODO Auto-generated method stub


            }

        });


        Log.d("otp1", "" + otp1);


        Log.d("otp4", "" + otp4);


        verify_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp4 = verify_code4.getText().toString();
                otp1 = verify_code1.getText().toString();
                otp2 = verify_code2.getText().toString();
                otp3 = verify_code3.getText().toString();
                otp = otp1 + otp2 + otp3 + otp4;
                Log.d("otp2222", "" + otp);
                if (otp1.equals("") || otp2.equals("") || otp3.equals("") || otp4.equals("")) {
                    Toast.makeText(Verification.this, "please enter the verification code", Toast.LENGTH_SHORT).show();

                } else {
                    // if (AppController.isOnline(Verification.this)) {

                    makeStringReq();
                      /*  } else {
                            AppController.showAlert(Verification.this, getString(R.string.networkError_Message));

                        }*/
                }
            }
        });


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
                            msg = obj.getString("msg");
                            //Log.d("MESSAGE","<<>>"+msg);
                            if (obj.has("response")) {

                                JSONObject jarray = obj.getJSONObject("user_details");
                                user_id = jarray.getString("user_id");
                                facebook_id = jarray.getString("facebook_id");

                                username = jarray.getString("username");


                                full_name = jarray.getString("full_name");


                                profile_name = jarray.getString("profile_name");
                                profile_description = jarray.getString("profile_description");

                                email_address = jarray.getString("email_address");

                                calling_code = jarray.getString("calling_code");

                                mobile_number = jarray.getString("mobile_number");
                                Log.d("mobile_number", "" + mobile_number);
                                password = jarray.getString("password");
                                Log.d("facebook_id", "" + facebook_id);

                                profile_pic_url = jarray.getString("profile_pic_url");
                                Log.d("facebook_id", "" + facebook_id);
                                background_pic_url = jarray.getString("background_pic_url");
                                Log.d("device_id", "" + device_id);
                                login_status = jarray.getString("login_status");

                                active_status = jarray.getString("active_status");
                                Log.d("login_status", "" + login_status);
                                device_id = jarray.getString("device_id");

                                is_verified = jarray.getString("is_verified");

                                verify_code = jarray.getString("verify_code");
                                gmt_value = jarray.getString("gmt_value");
                                Log.d("is_verified", "" + is_verified);
                                push_notification = jarray.getString("push_notification");

                                email_notification = jarray.getString("email_notification");
                                subscribed_plan = jarray.getString("subscribed_plan");
                                bid_limit = jarray.getString("bid_limit");
                                bid_used = jarray.getString("bid_used");
                                subscribed_date = jarray.getString("subscribed_date");
                                project_fee_percent = jarray.getString("project_fee_percent");
                                added_date = jarray.getString("added_date");

                            }
                            if (msg.equals("OTP verified successfully")) {
                            /*    AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(Otp_Activity.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(Otp_Activity.this);
                                }
                                //alert.setTitle("Registration!");
                                alert.setTitle("Successful");
                                alert.setMessage("Verified successfully");


                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


*/
                               /* ssp.set_id(Verification.this,user_id);
                                ssp.setFacebook_id(Verification.this,facebook_id);
                                ssp.setUsername(Verification.this,username);
                                ssp.setFull_name(Verification.this,full_name);
                                ssp.setProfile_name(Verification.this,profile_name);
                                ssp.setKEY_PROFILEdESCRIPTION(Verification.this,profile_description);
                                ssp.setEmail_address(Verification.this,email_address);
                                ssp.setKEY_calling_code(Verification.this,calling_code);
                                ssp.set_mobile(Verification.this,mobile_number);
                                ssp.setKeyPassword(Verification.this,password);
                                ssp.setKeyProfilpicurl(Verification.this,profile_pic_url);
                                ssp.setKeyBackgropundpicurl(Verification.this,background_pic_url);
                                ssp.setLogin_status(Verification.this,login_status);

                                ssp.setKeyIsverified(Verification.this,is_verified);
                                ssp.setKeyVeriycode(Verification.this,verify_code);
                                ssp.setKeyGmtvalue(Verification.this,gmt_value);
                                ssp.setKEY_subscribed_plan(Verification.this,subscribed_plan);
                                ssp.setKeyBidlimit(Verification.this,bid_limit);

                                ssp.setKEY_bid_used(Verification.this,bid_used);

                                ssp.setKeySubscribeddate(Verification.this,subscribed_date);
                                ssp.setKEY_added_date(Verification.this,added_date);
                                ssp.setKeyActivestatus(Verification.this,active_status);
      */
                                Intent intent = new Intent(Verification.this, Wall.class);
                                startActivity(intent);


                            }
                               /* });


                                try {
                                    Dialog dialog = alert.create();
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }*/

                            // }
                            else if (msg.equals("Oh Snap..! You have entered wrong OTP, Please enter correct one")) {
                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(Verification.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(Verification.this);
                                }
                                //alert.setTitle("Registration!");
                                alert.setMessage("Your verified OTP has been expired.Please resend OTP");


                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

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

                            } else if (msg.equals("You have already verified your Account, Please login")) {

                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(Verification.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(Verification.this);
                                }
                                //alert.setTitle("Registration!");
                                alert.setMessage("You have already verified your Account, Please login");


                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

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


                        } catch (JSONException e) {
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
                            Toast.makeText(Verification.this, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Verification.this, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(Verification.this, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Verification.this, "Network Error", Toast.LENGTH_LONG).show();
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

                params.put("action", "Otpverify");
                params.put("user_id", "2");
                params.put("otp", otp);

                Log.d("params", params.toString());
                return params;
            }

        };

//		RequestQueue requestQueue = Volley.newRequestQueue(this);
//		requestQueue.add(stringRequest);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest,
                tag_string_req);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


    private void ResendOtp() {

        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "loading...";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        Log.d("JsonLogin", response.toString());
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            msg = obj.getString("msg");



/*                            JSONObject jsonObject1=obj.getJSONObject("response");
                            user_id=jsonObject1.getString("user_id");
                                    name=jsonObject1.getString("name");*/
                                 /*   email
                                    "password": "1234",
                                    "latitude": "28.6175817",
                                    "longitude": "77.3878645"*/


                            if (msg.equals("Resent OTP on mail and sms successfully, Please enter it to verify your account")) {
                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(Verification.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(Verification.this);
                                }
                                //alert.setTitle("Registration!");
                                alert.setMessage("Your account verification Otp has been Resend to mobile");


                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

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
                params.put("action", "Resendotp");
                params.put("user_id", "2");


                Log.d("MainActivity", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(Verification.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(Verification.this);
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

}