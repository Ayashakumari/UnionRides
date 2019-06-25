package mx.bigapps.unionrides.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Activity.Add_Award;
import mx.bigapps.unionrides.Activity.PostPreviewimage;
import mx.bigapps.unionrides.Activity.Rides;
import mx.bigapps.unionrides.Activity.SquareImageView;
import mx.bigapps.unionrides.Adapter.MyawardAdapter;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.Award_List_Entity;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;


public class Awards extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static Button addAward;
    static SquareImageView cover_pic;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    double diff;
    Context context;
    static RelativeLayout videolayout;
    static TextView error;
    String msg;
    static RecyclerView photo_recycle;
    static ArrayList<Award_List_Entity> award_list = new ArrayList<>();
    static String imageoriganalurl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    static MyawardAdapter award_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_awards, container, false);
        context = getActivity();
        addAward = (Button) view.findViewById(R.id.addaward);
        photo_recycle = (RecyclerView) view.findViewById(R.id.photolist);
        cover_pic = (SquareImageView) view.findViewById(R.id.images);
        videolayout = (RelativeLayout) view.findViewById(R.id.videolayout);
        error = (TextView) view.findViewById(R.id.error);
        cover_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostPreviewimage.class);
                intent.putExtra("image", imageoriganalurl);
                startActivity(intent);
            }
        });
        if (helper.status.equals("otherprofile_ride")) {
            addAward.setVisibility(View.GONE);
        }
        Rides.events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.riderStatus.equals("3")) {
                    Intent intent = new Intent(getActivity(), Add_Award.class);

                    if (award_list.size() > 0) {
                        intent.putExtra("flag", "1");
                        intent.putExtra("award_name", award_list.get(0).getAward_name());
                        intent.putExtra("award_id", award_list.get(0).getAward_id());
                        intent.putExtra("award_date", award_list.get(0).getAward_date());
                        intent.putExtra("award_pic", award_list.get(0).getAward_picture());
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().startActivity(intent);
                }
            }
        });


        addAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Add_Award.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().startActivity(intent);
            }
        });


        return view;
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

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(context);
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
                                Log.d("JsonAwarddata", response.toString().substring(start1, end));
                            }
                            award_list.clear();

                            JSONArray jsonArray = obj.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                Award_List_Entity entity = new Award_List_Entity();
                                entity.setAward_id(object.getString("award_id"));
                                entity.setAward_name(object.getString("award_name"));
                                entity.setAward_date(object.getString("award_date"));
                                entity.setAward_picture(object.getString("award_picture"));
                                entity.setImage640_640(object.getString("640_640"));
                                award_list.add(entity);


                            }
                            if (award_list.size() > 0) {
                                award_adapter = new MyawardAdapter(getActivity(), award_list);
                                videolayout.setVisibility(View.VISIBLE);
                                error.setVisibility(View.GONE);
                                addAward.setText("+Add Award");
                                try {
                                    imageoriganalurl = award_list.get(0).getAward_picture();
                                    Picasso.with(context).load(award_list.get(0).getImage640_640()).placeholder(R.drawable.loadinguni).into(cover_pic);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                photo_recycle.setAdapter(award_adapter);
                            } else {
                                addAward.setText("+Add Award");
                                videolayout.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);
                            }

//*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
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
                            Toast.makeText(context, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(context, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show();
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

                params.put("action", "Showawardlist");

                if (helper.status.equals("otherprofile_ride")) {
                    params.put("user_id", helper.user_id);
                } else {
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                }

                params.put("cover_photo_id", PrefMangr.getInstance().getCoverPicID());


                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(stringRequest,
//                tag_string_req);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    //
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
//            onResume();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }
    public static void makerestart(final Context context) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;

                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                Log.d("refreshdata", response.toString().substring(start1, end));
                            }
                            award_list = new ArrayList<>();

                            JSONArray jsonArray = obj.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Award_List_Entity entity = new Award_List_Entity();
                                entity.setAward_id(object.getString("award_id"));
                                entity.setAward_name(object.getString("award_name"));
                                entity.setAward_date(object.getString("award_date"));
                                entity.setAward_picture(object.getString("award_picture"));
                                entity.setImage640_640(object.getString("640_640"));
                                award_list.add(entity);


                            }
                            if (award_list.size() > 0) {
                                award_adapter = new MyawardAdapter(context, award_list);
                                videolayout.setVisibility(View.VISIBLE);
                                error.setVisibility(View.GONE);
                                addAward.setText("+Add Award");
                                try {
                                    imageoriganalurl = award_list.get(0).getAward_picture();
                                    Picasso.with(context).load(award_list.get(0).getImage640_640()).placeholder(R.drawable.loadinguni).into(cover_pic);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                photo_recycle.setAdapter(award_adapter);
                                photo_recycle.setVisibility(View.VISIBLE);
                                error.setVisibility(View.GONE);
                            } else {
                                addAward.setText("+Add Award");
                                videolayout.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);
                            }

//*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
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

                params.put("action", "Showawardlist");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("cover_photo_id", PrefMangr.getInstance().getCoverPicID());


                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeStringReq();
    }

    public static void setcoverPic(Context context, String img, String imageoriganal) {
        imageoriganalurl = imageoriganal;
        try {

            Picasso.with(context).load(img).placeholder(R.drawable.loadinguni).into(cover_pic);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void setaddAward(Context context) {
        addAward.setText("+Add Award");
        videolayout.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);

    }
}