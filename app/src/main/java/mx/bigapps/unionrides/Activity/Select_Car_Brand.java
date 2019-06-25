package mx.bigapps.unionrides.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Adapter.Brand_Adapter;
import mx.bigapps.unionrides.Adapter.Car_Model_Adapter;
import mx.bigapps.unionrides.Model.Car_Brand_Entity;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;

/**
 * Created by dell on 11/20/2017.
 */

public class Select_Car_Brand extends AppCompatActivity {
    RecyclerView brand_name_view;
    LinearLayoutManager linearLayout;
    ImageView award_back;
    TextView header_txt;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    Context context;
    int flagValue;
    Intent intent;
    Brand_Adapter adapter;
    Car_Model_Adapter model_adapter;
    ArrayList<Car_Brand_Entity> model_list=new ArrayList<>();
    ArrayList<Car_Brand_Entity> brand_list=new ArrayList<>();
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_brand);
        context=Select_Car_Brand.this;
        intent=getIntent();
        flagValue=intent.getIntExtra("flag",0);
        brand_name_view= (RecyclerView) findViewById(R.id.brand_name_view);
        linearLayout=new LinearLayoutManager(Select_Car_Brand.this);
        brand_name_view.setLayoutManager(linearLayout);
        award_back= (ImageView) findViewById(R.id.award_back);
        award_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        header_txt= (TextView) findViewById(R.id.header_txt);
        if (flagValue==1){
            header_txt.setText("Brand Name");
            makeStringReq();
        }
        else if (flagValue==2){
            header_txt.setText("Model Name");
            makeStringReq();
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
    @SuppressLint("InlinedApi") private void showProgDialog() {
        progress_dialog = null;
        try{
            if (Build.VERSION.SDK_INT >= 11 ) {
                progress_dialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT );
            } else {
                progress_dialog = new ProgressDialog(context);
            }
            progress_dialog.setMessage(progress_dialog_msg);
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    // hide progress
    private void hideProgDialog() {
        try{
            if (progress_dialog != null && progress_dialog.isShowing())
                progress_dialog.dismiss();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void makeStringReq() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "loading...";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj=null;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;

                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                Log.d("Json Data", response.toString().substring(start1, end));
                            }
                            if (flagValue==1) {
                                JSONArray jsonArray = obj.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    Car_Brand_Entity entity = new Car_Brand_Entity();
                                    entity.setBrand_name(object.getString("brand_name"));
                                    entity.setId(object.getString("id"));
                                   // Select.add(object.getString("brand_name"));
                                    brand_list.add(entity);
                                }
                                if (brand_list.size()>0){
                                adapter=new Brand_Adapter(Select_Car_Brand.this,brand_list);
                                    brand_name_view.setAdapter(adapter);
                                }

                            }
                            if (flagValue==2){
                                JSONArray jsonArray = obj.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    Car_Brand_Entity entity = new Car_Brand_Entity();
                                    entity.setBrand_name(object.getString("brand_name"));
                                    entity.setId(object.getString("id"));
                                    entity.setModel(object.getString("model"));
                                    //  Select.add(object.getString("brand_name"));
                                    model_list.add(entity);
                                    if (model_list.size()>0){
                                        model_adapter=new Car_Model_Adapter(Select_Car_Brand.this,model_list);
                                        brand_name_view.setAdapter(model_adapter);
                                    }

                                }

                            }

//*/
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        catch (Exception e) {
                            // TODO Auto-generated catch block
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
                            Toast.makeText(context,"Time Out Error",Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof AuthFailureError) {
                            Toast.makeText(context,"Authentication Error",Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(context,"Server Error",Toast.LENGTH_LONG).show();
                            //TODO
                        }
                        else if (error instanceof NetworkError) {
                            Toast.makeText(context,"Network Error",Toast.LENGTH_LONG).show();
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            //TODO

                        }
                        else if (error instanceof ParseError) {

//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            //TODO
                        }

                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                if (flagValue==1) {
                    params.put("action", "Carbrandlist");
                }
                else if (flagValue==2){
                    params.put("action", "Carmodellist");
                    params.put("brand_name", helper.brand_name);
                    helper.name_brand= helper.brand_name;

                }


                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Select_Car_Brand.this);
        requestQueue.add(stringRequest);
        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(stringRequest,
//                tag_string_req);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
}
