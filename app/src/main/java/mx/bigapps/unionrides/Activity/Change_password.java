package mx.bigapps.unionrides.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;

/**
 * Created by dev on 01-11-2017.
 */

public class Change_password extends Activity {
    EditText old_pass, new_pass, cnf_pass;
    String u;

    @Override
    protected void onCreate(Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        setContentView(R.layout.change_password);

        ImageView m = (ImageView) findViewById(R.id.cp_back);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        old_pass = (EditText) findViewById(R.id.old_password);
        new_pass = (EditText) findViewById(R.id.new_password);
        cnf_pass = (EditText) findViewById(R.id.cnfirm_password);
        Button submit = (Button) findViewById(R.id.cp_sumit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword();

            }
        });


    }


    public void ChangePassword() {


        final String op = old_pass.getText().toString().trim();
        final String np = new_pass.getText().toString().trim();
        final String cnf = cnf_pass.getText().toString().trim();

        if (TextUtils.isEmpty(op)) {
            Toast.makeText(Change_password.this, " Enter old Password", Toast.LENGTH_LONG).show();
            old_pass.requestFocus();
            return;
        } else if (TextUtils.isEmpty(np)) {
            Toast.makeText(Change_password.this, " Enter new Password", Toast.LENGTH_LONG).show();
            new_pass.requestFocus();
        } else if (TextUtils.isEmpty(cnf)) {
            Toast.makeText(Change_password.this, " Enter confirm Password", Toast.LENGTH_LONG).show();
            cnf_pass.requestFocus();
            return;
        } else if (!np.equals(cnf)) {
            Toast.makeText(Change_password.this, " Enter same password", Toast.LENGTH_LONG).show();
            new_pass.requestFocus();
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

                            if (message.equals("Old password is wrong.")) {

                                Toast.makeText(Change_password.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Password changed successfully")) {

                                // Toast.makeText(Change_password.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Change_password.this);


                                alertDialogBuilder.setMessage("Password changed successfully");
                                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog1, int which) {
                                        startActivity(new Intent(getApplicationContext(), Wall.class));
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
                params.put("action", "Changepassword");

                params.put("old_password", op);
                params.put("new_password", np);
                params.put(" confirm_new_password", cnf);
                params.put("user_id", PrefMangr.getInstance().getUserId());

                Log.d("params", "===" + params.toString());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Change_password.this);
        requestQueue.add(stringRequest);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, PublicProfile.class);
        startActivity(intent);
    }

}
