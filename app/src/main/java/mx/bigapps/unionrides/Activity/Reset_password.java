package mx.bigapps.unionrides.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;


/**
 * Created by dev on 01-11-2017.
 */

public class Reset_password extends Activity {
    EditText email;
    String e;

    @Override
    protected void onCreate(Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        setContentView(R.layout.reset_password);

        ImageView m = (ImageView) findViewById(R.id.cross_reset);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MemberLogin.class));
            }
        });

        email = (EditText) findViewById(R.id.reset_email);
        Button submit = (Button) findViewById(R.id.reset_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }


    private void resetPassword() {
        e = email.getText().toString().trim();

        if (TextUtils.isEmpty(e) || !Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            Toast.makeText(Reset_password.this, " Enter valid Email", Toast.LENGTH_LONG).show();
            email.requestFocus();
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d("Data", "<><>" + response);
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            Log.d("Data", "<><>" + obj.toString());
                            String message = obj.getString("msg");
                            Log.d("msg", "==" + message);

                            if (message.equals("The email not found in our database"))

                                Toast.makeText(Reset_password.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            else if (message.equals("Your login credentials has been sent to your registered email address")) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Reset_password.this);


                                alertDialogBuilder.setMessage("Your login credentials has been sent to your registered email address");
                                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog1, int which) {


                                        startActivity(new Intent(getApplicationContext(), MemberLogin.class));
                                        dialog1.cancel();

                                    }

                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "Forgotpassword");

                params.put("email_address", e);
                Log.d("params", "===" + params.toString());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Reset_password.this);
        requestQueue.add(stringRequest);


    }


}




