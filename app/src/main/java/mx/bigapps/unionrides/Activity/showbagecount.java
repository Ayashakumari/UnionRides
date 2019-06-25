package mx.bigapps.unionrides.Activity;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.utils.Apis;

public class showbagecount extends AppCompatActivity implements Runnable {
    Wall wall = new Wall();

    @Override
    public void run() {

            Showbadgecount();


        // Clear current saved token


        // Check for success of empty token


    }


    private void Showbadgecount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        // hideProgDialog();

                        JSONObject obj = null;
                        Log.d("Showbadgecount", response.toString());

                        try {
                            obj = new JSONObject(response.toString());
                            String chatcount = obj.getString("chatCount");

                            if (chatcount.equals("0")) {
                                wall.txt_chatcount.setVisibility(View.GONE);
                            } else {
                                wall.txt_chatcount.setVisibility(View.VISIBLE);
                                wall.txt_chatcount.setText(chatcount);
                            }


                        } catch (JSONException e1) {

                            e1.printStackTrace();
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

                params.put("action", "Showbadgecount");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                Log.d("Showfriendrequest", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}