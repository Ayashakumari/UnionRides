package mx.bigapps.unionrides.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
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

import mx.bigapps.unionrides.Activity.Add_specification;
import mx.bigapps.unionrides.Activity.PostPreviewimage;
import mx.bigapps.unionrides.Activity.Rides;
import mx.bigapps.unionrides.Activity.SquareImageView;
import mx.bigapps.unionrides.Adapter.ridesspec_Adapter;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.Specification_Model;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;


public class SpecRides extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    SquareImageView photos;
    RecyclerView photo_recycle;
    RecyclerView.LayoutManager layoutManager;
    ridesspec_Adapter photo_adapter;
    ArrayList photolist;
    static ImageView cover_pic;
    static Button addspecification;
    static RelativeLayout container_video;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    JSONObject jobj;
    String msg;
    static TextView error;
    static TextView brand_name_txt;
    static TextView model_txt;
    static TextView year_txt;
    static TextView color_txt;
    static TextView hp_txt;
    static TextView torque_txt;
    static TextView speed_txt;
    static TextView price_txt;
    static ArrayList<Specification_Model> spec_list = new ArrayList<>();
    static Button deletespecification;

    public static SpecRides newInstance(String param1, String param2) {
        SpecRides fragment = new SpecRides();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spec_rides, container, false);
        photos = (SquareImageView) view.findViewById(R.id.photos);
        helper.brand_name = "";
        helper.model_name = "";
        helper.year = "";
        helper.color = "";
        helper.hp = "";
        helper.torque = "";
        helper.speed = "";
        helper.id = "";
        helper.pic_url = "";
        helper.ridePrice = "";
        deletespecification = (Button) view.findViewById(R.id.deletespecification);
        addspecification = (Button) view.findViewById(R.id.addspecification);
        brand_name_txt = (TextView) view.findViewById(R.id.brand_name_txt);
        model_txt = (TextView) view.findViewById(R.id.model_txt);
        year_txt = (TextView) view.findViewById(R.id.year_txt);
        color_txt = (TextView) view.findViewById(R.id.color_txt);
        container_video = (RelativeLayout) view.findViewById(R.id.container_video);
        hp_txt = (TextView) view.findViewById(R.id.hp_txt);
        torque_txt = (TextView) view.findViewById(R.id.torque_txt);
        speed_txt = (TextView) view.findViewById(R.id.speed_txt);
        cover_pic = (ImageView) view.findViewById(R.id.photos);
        error = (TextView) view.findViewById(R.id.error);
        price_txt = (TextView) view.findViewById(R.id.price_txt);
        //photo_recycle= (RecyclerView)view. findViewById(R.id.photolist);
        if (helper.status.equals("otherprofile_ride")) {
            addspecification.setVisibility(View.GONE);
            deletespecification.setVisibility(View.GONE);
        }
        cover_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostPreviewimage.class);
                intent.putExtra("image", spec_list.get(0).getSpec_picture());
                startActivity(intent);
            }
        });
        Rides.events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.riderStatus.equals("2")) {
                    if (spec_list.size() > 0) {
                        Intent intent = new Intent(getActivity(), Add_specification.class);

                        helper.brand_name = spec_list.get(0).getBrand_name();
                        helper.model_name = spec_list.get(0).getModel_name();
                        helper.year = spec_list.get(0).getYear();
                        helper.color = spec_list.get(0).getColor();
                        helper.hp = spec_list.get(0).getHorsepower();
                        helper.torque = spec_list.get(0).getTouque();
                        helper.speed = spec_list.get(0).getMph();
                        helper.id = spec_list.get(0).getSpecification_id();
                        helper.pic_url = spec_list.get(0).getSpec_picture();
                        intent.putExtra("flag", 1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Log.d("DATA", "11111");
                        getActivity().startActivity(intent);
                    } else {
                        Log.d("DATA", "222222");
                        Intent intent = new Intent(getActivity(), Add_specification.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().startActivity(intent);
                    }
                }
            }
        });
        addspecification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spec_list.size() > 0) {
                    Intent intent = new Intent(getActivity(), Add_specification.class);

                    helper.brand_name = spec_list.get(0).getBrand_name();
                    helper.model_name = spec_list.get(0).getModel_name();
                    helper.year = spec_list.get(0).getYear();
                    helper.color = spec_list.get(0).getColor();
                    helper.hp = spec_list.get(0).getHorsepower();
                    helper.torque = spec_list.get(0).getTouque();
                    helper.speed = spec_list.get(0).getMph();
                    helper.id = spec_list.get(0).getSpecification_id();
                    helper.pic_url = spec_list.get(0).getSpec_picture();
                    helper.ridePrice = spec_list.get(0).getSpec_price();
                    intent.putExtra("flag", 1);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    Log.d("DATA", "11111");
                    getActivity().startActivity(intent);
                } else {
                    Log.d("DATA", "222222");
                    Intent intent = new Intent(getActivity(), Add_specification.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().startActivity(intent);
                }


            }
        });
        deletespecification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(getActivity());
                }

                alert.setMessage("Do you want to delete this specification ? ");


                alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.id = "";
                        deletespec();
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("no", new DialogInterface.OnClickListener() {

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
        });

        photo_adapter = new ridesspec_Adapter(getActivity(), photolist);
        //speclist.setAdapter(photo_adapter);

        return view;
    }

    private void deletespec() {
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
                                Log.d("deletspec", response.toString().substring(start1, end));
                            }
                            spec_list.clear();
                            spec_list = new ArrayList<>();
                            addspecification.setText("Add SPECEFICATION");
                            deletespecification.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                            cover_pic.setVisibility(View.GONE);
                            container_video.setVisibility(View.GONE);


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
                            Toast.makeText(getActivity(), "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getActivity(), "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
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

                params.put("action", "Deleted_ride_specification");
                params.put("specification_id", spec_list.get(0).getSpecification_id());
                params.put("user_id", PrefMangr.getInstance().getUserId());

                //   params.put("cover_image",image_aaray);


                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    private static String getHtmlText(String boldString, String restString) {

        String htmlString = "<b>" + boldString + "</b> " + restString;

        return htmlString;
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
                progress_dialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(getActivity());
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

    // TODO: Rename method, update argument and hook method into UI event
    private void makeStringReq() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "loading...";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        spec_list.clear();

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
                                Log.d("specificatiData", response.toString().substring(start1, end));
                            }
                            JSONArray jsonArray = obj.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Specification_Model model = new Specification_Model();
                                model.setSpecification_id(object.getString("specification_id"));
                                model.setUser_id(object.getString("user_id"));
                                model.setCover_photo_id(object.getString("cover_photo_id"));
                                model.setBrand_name(object.getString("brand_name"));
                                model.setModel_name(object.getString("model_name"));
                                model.setColor(object.getString("color"));
                                model.setHorsepower(object.getString("horsepower"));
                                model.setYear(object.getString("year"));
                                model.setTouque(object.getString("touque"));
                                model.setMph(object.getString("mph"));
                                model.setSpec_picture(object.getString("spec_picture"));
                                model.setDatetime(object.getString("datetime"));
                                model.setImge640_640(object.getString("640_640"));
                                model.setSpec_price(object.getString("spec_price"));
                                spec_list.add(model);

                            }
                            if (spec_list.size() > 0) {
                                brand_name_txt.setText(Html.fromHtml(getHtmlText("Brand:", spec_list.get(0).getBrand_name())));
                                model_txt.setText(Html.fromHtml(getHtmlText("Model:", spec_list.get(0).getModel_name())));
                                year_txt.setText(Html.fromHtml(getHtmlText("Year:", spec_list.get(0).getYear())));
                                color_txt.setText(Html.fromHtml(getHtmlText("Color:", spec_list.get(0).getColor())));
                                hp_txt.setText(Html.fromHtml(getHtmlText("HP:", spec_list.get(0).getHorsepower())));
                                price_txt.setText(Html.fromHtml(getHtmlText("Sales Price: $", spec_list.get(0).getSpec_price())));
                                torque_txt.setText(Html.fromHtml(getHtmlText("Torque:", spec_list.get(0).getTouque())));
                                speed_txt.setText(Html.fromHtml(getHtmlText("0-60:", spec_list.get(0).getMph())));
                                addspecification.setText("Edit Specification");
                                error.setVisibility(View.GONE);
                                Picasso.with(getActivity()).load(spec_list.get(0).getImge640_640()).placeholder(R.drawable.loading).into(cover_pic);
                            } else {
                                deletespecification.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);
                                cover_pic.setVisibility(View.GONE);
                                container_video.setVisibility(View.GONE);
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
                            Toast.makeText(getActivity(), "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getActivity(), "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
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

                params.put("action", "Showspecification");
                if (helper.status.equals("otherprofile_ride")) {
                    params.put("user_id", helper.user_id);
                } else {
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                }
                params.put("cover_photo_id", PrefMangr.getInstance().getCoverPicID());
                params.put("type", "video");

                //   params.put("cover_image",image_aaray);


                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(stringRequest,
//                tag_string_req);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

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

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeStringReq();
    }

    public static void refeshData(final Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        spec_list.clear();


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
                            JSONArray jsonArray = obj.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Specification_Model model = new Specification_Model();
                                model.setSpecification_id(object.getString("specification_id"));
                                model.setUser_id(object.getString("user_id"));
                                model.setCover_photo_id(object.getString("cover_photo_id"));
                                model.setBrand_name(object.getString("brand_name"));
                                model.setModel_name(object.getString("model_name"));
                                model.setColor(object.getString("color"));
                                model.setHorsepower(object.getString("horsepower"));
                                model.setYear(object.getString("year"));
                                model.setTouque(object.getString("touque"));
                                model.setMph(object.getString("mph"));
                                model.setSpec_picture(object.getString("spec_picture"));
                                model.setDatetime(object.getString("datetime"));
                                model.setImge640_640(object.getString("640_640"));
                                model.setSpec_price(object.getString("spec_price"));
                                spec_list.add(model);

                            }
                            if (spec_list.size() > 0) {
                                brand_name_txt.setText(Html.fromHtml(getHtmlText("Brand:", spec_list.get(0).getBrand_name())));
                                model_txt.setText(Html.fromHtml(getHtmlText("Model:", spec_list.get(0).getModel_name())));
                                year_txt.setText(Html.fromHtml(getHtmlText("Year:", spec_list.get(0).getYear())));
                                color_txt.setText(Html.fromHtml(getHtmlText("Color:", spec_list.get(0).getColor())));
                                hp_txt.setText(Html.fromHtml(getHtmlText("HP:", spec_list.get(0).getHorsepower())));
                                price_txt.setText(Html.fromHtml(getHtmlText("Sales Price: $", spec_list.get(0).getSpec_price())));

                                torque_txt.setText(Html.fromHtml(getHtmlText("Torque:", spec_list.get(0).getTouque())));
                                speed_txt.setText(Html.fromHtml(getHtmlText("0-60:", spec_list.get(0).getMph())));
                                addspecification.setText("Edit Specification");
                                error.setVisibility(View.GONE);
                                Picasso.with(context).load(spec_list.get(0).getImge640_640()).placeholder(R.drawable.loading).into(cover_pic);
                            } else {
                                deletespecification.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);
                                cover_pic.setVisibility(View.GONE);
                                container_video.setVisibility(View.GONE);
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

                params.put("action", "Showspecification");
                if (helper.status.equals("otherprofile_ride")) {
                    params.put("user_id", helper.user_id);
                } else {
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                }
                params.put("cover_photo_id", PrefMangr.getInstance().getCoverPicID());
                params.put("type", "video");

                //   params.put("cover_image",image_aaray);


                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
}
