package mx.bigapps.unionrides.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;

/**
 * Created by dev on 01-11-2017.
 */

public class CreateAccount extends AppCompatActivity {
    EditText fname, nick_name, email, password, repass;
    AppCompatRadioButton c2;
    AppCompatRadioButton c1;
    ImageView cross;
    TextView terms;
    private ProgressDialog progress_dialog;
    int gmtvalue;
    AppCompatRadioButton radio_rider, radio_Club;
    String AccountType = null;
    RadioGroup radioGroup;
    String userId;

    @Override
    protected void onCreate(final Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        setContentView(R.layout.create_account);
        TimeZone tz = TimeZone.getDefault();
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
       /* radio_rider = (AppCompatRadioButton) findViewById(R.id.radio_rider);
        radio_Club = (AppCompatRadioButton) findViewById(R.id.radio_club);*/
        Log.d("shahhs", "" + tz.getDisplayName(false, TimeZone.SHORT));

        TimeZone tzzz = TimeZone.getDefault();
        final Date now = new Date();
        gmtvalue = tzzz.getOffset(now.getTime()) / 1000;
        Log.d("111111", "" + gmtvalue);
        Log.d("gmt", String.valueOf(gmtvalue));
        terms = (TextView) findViewById(R.id.terms);
        cross = (ImageView) findViewById(R.id.cross);
        fname = (EditText) findViewById(R.id.full_name);
        nick_name = (EditText) findViewById(R.id.nick_name);
        email = (EditText) findViewById(R.id.acct_email);
        password = (EditText) findViewById(R.id.acct_password);
        repass = (EditText) findViewById(R.id.acct_re_password);
        c1 = (AppCompatRadioButton) findViewById(R.id.radio_rider);
        c2 = (AppCompatRadioButton) findViewById(R.id.radio_club);
        Button submit = (Button) findViewById(R.id.acct_submit);
        String myString = terms.getText().toString();
        terms.setMovementMethod(LinkMovementMethod.getInstance());
        terms.setText(Html.fromHtml(myString), TextView.BufferType.SPANNABLE);
        myString = terms.getText().toString();
        int i1 = myString.indexOf(".");
        int i2 = myString.indexOf("ck ");
        Spannable mySpannable = (Spannable) terms.getText();


        ClickableSpan myClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent i = new Intent(CreateAccount.this, Terms.class);

                startActivity(i);
            }

            @Override
            public void updateDrawState(final TextPaint textPaint) {
                textPaint.setUnderlineText(true);
            }
        };
        mySpannable.setSpan(myClickableSpan, i1 + 2, i2 + 2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mySpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#70b5eb")), i1 + 2, i2 + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccount.this, Welcome.class));
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_rider) {
                     PrefMangr.getInstance().setuserType("0");
                    AccountType = "0";
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_club) {
                    AccountType = "1";
                    PrefMangr.getInstance().setuserType("1");
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_page) {
                    PrefMangr.getInstance().setuserType("2");
                    AccountType = "2";
                } else {
                    AccountType = null;
                    // one of the radio buttons is checked
                }
                // startActivity(new Intent(getApplicationContext(), Wall.class));
                String full_name = fname.getText().toString().trim();
                String nick = nick_name.getText().toString().trim();
                String e = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String re_pass = repass.getText().toString().trim();
                String ride = c1.getText().toString();
                String club = c2.getText().toString();

                if (TextUtils.isEmpty(full_name)) {
                    //fname.setError("Enter Full Name");
                    Toast.makeText(CreateAccount.this, "Enter Full Name", Toast.LENGTH_LONG).show();
                    fname.requestFocus();
                    return;

                } else if (TextUtils.isEmpty(nick)) {
                    //   nick_name.setError("Enter nick Name");
                    Toast.makeText(CreateAccount.this, "Enter nick Name", Toast.LENGTH_LONG).show();
                    nick_name.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(e) || !Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                    //email.setError("Invalid Email");
                    Toast.makeText(CreateAccount.this, " Enter valid Email", Toast.LENGTH_LONG).show();
                    email.requestFocus();
                    return;
                } else if (!pass.equals(re_pass) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(CreateAccount.this, " Please Enter Password", Toast.LENGTH_LONG).show();

                    password.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(re_pass)) {
                    // repass.setError("Please Enter Same Password");
                    Toast.makeText(CreateAccount.this, " Please Enter Same Password", Toast.LENGTH_LONG).show();
                    repass.requestFocus();
                    return;
                } else if (AccountType == null) {
                    Toast.makeText(CreateAccount.this, " Please select Account Type", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    //startActivity(new Intent(getApplicationContext(), Wall.class));
                    createAccount(full_name, nick, e, pass);
                }
            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Welcome.class));
            }
        });


    }

    private void showProgDialog() {
        // TODO Auto-generated method stub
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(CreateAccount.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(CreateAccount.this);
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

    private void showAlert(final String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateAccount.this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //   if (msg.equals("Registration successful.We have sent account verification link to your mail address.")) {

                PrefMangr.getInstance().setUSerId(userId);
                startActivity(new Intent(getApplicationContext(), Wall.class));

                dialog.dismiss();
                /*} else {
                    dialog.dismiss();
                }*/
            }

        });

       /* alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });*/
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createAccount(final String full_name, final String nick, final String e, final String pass) {
        HttpsTrustManager.allowAllSSL();
        showProgDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        hideProgDialog();
                        Log.d("CreateAccount", response.toString());
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            String msg = obj.getString("msg");
                            if (msg.equals("Registration successfully.")) {
                                userId = obj.getString("user_id");
                                PrefMangr.getInstance().setProfilepic("");
                                showAlert(msg);
                                } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateAccount.this);
                                alertDialogBuilder.setMessage(msg);
                                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        dialog.dismiss();

                                    }

                                });


                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // tvaddress.setText(location);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        hideProgDialog();


                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(CreateAccount.this, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(CreateAccount.this, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Log.d("error", String.valueOf(error));
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(CreateAccount.this, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(CreateAccount.this, "Network Error", Toast.LENGTH_LONG).show();
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
                params.put("action", "Createaccount");
                params.put("full_name", full_name);
                params.put("nick_name", nick);
                params.put("email_address", e);
                params.put("password", pass);
                params.put("gmtvalue", String.valueOf(gmtvalue));
                params.put("account_type", AccountType);
                Log.d("CreateAccount", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(CreateAccount.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 150, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


}

