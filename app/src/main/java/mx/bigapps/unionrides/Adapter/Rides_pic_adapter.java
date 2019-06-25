package mx.bigapps.unionrides.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Activity.PostPreviewimage;
import mx.bigapps.unionrides.Activity.Preview_firstride;
import mx.bigapps.unionrides.Activity.Rides;
import mx.bigapps.unionrides.Activity.Wall;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.Cover_Photo_List;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;

/**
 * Created by we on 10/10/2018.
 */

public class Rides_pic_adapter extends RecyclerView.Adapter<Rides_pic_adapter.MyViewHolder> {


    Context context;
    Activity activity;
    ArrayList<Cover_Photo_List> employer_list = new ArrayList<>();
    int mCurrentPlayingPosition = -1;
    LayoutInflater mInflater;

    int page = 0, curSize;
    ArrayList<Cover_Photo_List> list = new ArrayList<>();

    int flag = 0;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String msg, cover_id, class_status;
    int flagValue = 0;
    boolean bool = true;
    String type;
    String ride_id;

    ProgressBar progressBar;

    public Rides_pic_adapter(Context context, ArrayList<Cover_Photo_List> employer_list) {

        this.context = context;
        this.context = context;
        this.employer_list = employer_list;


        mInflater = LayoutInflater.from(context);

    }

    @NonNull

    @Override

    public Rides_pic_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.riderphotolist, parent, false);
        Rides_pic_adapter.MyViewHolder vh = new MyViewHolder(v);
        return vh;

    }

    @Override
    public int getItemCount() {
        return employer_list.size();
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        helper.pos = 0;

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (helper.status.equals("otherprofile_ride")) {
                    Log.d("Json_Array", "hhhjjb");
                    Intent intent = new Intent(context, Rides.class);
                    PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
                    intent.putExtra("cover_id", employer_list.get(position).getCover_photo_id());
                    PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
                    intent.putExtra("cover_pic", employer_list.get(position).getCover_photo());
                    context.startActivity(intent);


                } else if (helper.status.equals("otherprofile_ride")) {
                    Intent intent = new Intent(context, Preview_firstride.class);
                    intent.putExtra("flag", 1);
                    PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
                    intent.putExtra("cover_id", employer_list.get(position).getCover_photo_id());
                    PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
                    intent.putExtra("cover_pic", employer_list.get(position).getCover_photo());
                    context.startActivity(intent);

                } else {
                    Log.d("selected_Array", "hhhjjb");

                    if (helper.rides_status == 1) {

                        helper.pos = (position);

                        Log.d("positionstart", ">>>>>" + helper.pos);


                        Intent intent = new Intent(context, Preview_firstride.class);
                        PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
                        intent.putExtra("cover_id", employer_list.get(position).getCover_photo_id());
                        PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
                        intent.putExtra("cover_pic", employer_list.get(position).getCover_photo());


                        context.startActivity(intent);

                    } else if (helper.rides_status == 3) {
                        Intent intent = new Intent(context, Preview_firstride.class);
                        PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
                        intent.putExtra("cover_id", employer_list.get(position).getCover_photo_id());
                        PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
                        intent.putExtra("cover_pic", employer_list.get(position).getCover_photo());
                        context.startActivity(intent);

                    } else {

                        Intent intent = new Intent(context, Rides.class);
                        PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
                        intent.putExtra("cover_id", employer_list.get(position).getCover_photo_id());
                        PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
                        intent.putExtra("cover_pic", employer_list.get(position).getCover_photo());
                        context.startActivity(intent);
                    }
                }
            }

        });


        Glide.with(context)
                .load(employer_list.get(position).getCover_photo())
                .placeholder(R.drawable.loadinguni)
                .override(120, 80)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imageView);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("COVER_ID", "<><>" + cover_id);
                flagValue = 1;
                class_status = employer_list.get(position).getClass_status();

                showAlert("Do You Want to delete this Ride", employer_list.get(position).getCover_photo_id());

                if (helper.rides_status ==1){
                    makeStringReq2(employer_list.get(position).getRide_id());
                    showAlert("Do You Want to delete this Ride", employer_list.get(position).getCover_photo_id());

                }
                if (helper.rides_status ==3){
                    makeStringReq2(employer_list.get(position).getRide_id());
                    showAlert("Do You Want to delete this Ride", employer_list.get(position).getCover_photo_id());
                }
            }
        });

        if (employer_list.get(position).getClass_status().equals("1")) {
            // star.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        } else if (employer_list.get(position).getClass_status().equals("2")) {
            // star.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
//            holder.footer.setVisibility(View.GONE);

        } else {
            // star.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        }

        holder.star.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (employer_list.get(position).getMainPic().equals("0")) {
                    flagValue = 2;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Doy you want to make this ride favourite");
                    alertDialogBuilder.setTitle("Alert!");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            type = "1";
                            class_status = employer_list.get(position).getClass_status();
                            makeStringReq(employer_list.get(position).getCover_photo_id());
                            holder.star.setImageResource(R.drawable.star);

                            dialog.dismiss();
                        }

                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {
                    flagValue = 2;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Do you want to unfavourite this ride");
                    alertDialogBuilder.setTitle("Alert!");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            type = "0";
                            class_status = employer_list.get(position).getClass_status();
                            makeStringReq(employer_list.get(position).getCover_photo_id());
                            // showAlert("Do You Want unfavourite this ride favourite",employer_list.get(position).getCover_photo_id());
                            holder.star.setImageResource(R.drawable.star_empty);

                            dialog.dismiss();
                        }

                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }


            }
        });
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView delete, star;
        TextView moreinfo;
        ProgressBar progressBar;
        ImageView imageView;
        RelativeLayout first;


        public MyViewHolder(View itemView) {
            super(itemView);

            RelativeLayout footer = (RelativeLayout) itemView.findViewById(R.id.footer);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            first = itemView.findViewById(R.id.first);
            final float scale = context.getResources().getDisplayMetrics().density;
            int pixels = (int) (200 * scale + 0.5f);

            delete = (ImageView) itemView.findViewById(R.id.delete);
            star = (ImageView) itemView.findViewById(R.id.star);
            moreinfo = (TextView) itemView.findViewById(R.id.more_info);


        }


    }


    private void showAlert(String msg, final String cover_id) {
        Log.d("COVER_ID", "<><>" + cover_id);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setTitle("Alert!");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeStringReq(cover_id);

                dialog.dismiss();
            }

        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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


    private void makeStringReq(final String cover_id) {
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
                            if (flagValue == 1) {
                                if (msg.equals("Deleted successfully")) {


                                    AlertDialog.Builder alert;
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(context);
                                    }
                                    alert.setTitle("Successfull");
                                    alert.setMessage("Deleted successfully");


                                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
                                        dev=preferences2.edit();

                                        dev.putString("email_txt",email_login);
                                        dev.putString("password_txt",password_login);
                                        dev.commit();
                                        /
*/
                                            if (class_status.equals("0")) {
                                                Intent intent = new Intent(context, Wall.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                intent.putExtra("flag", 3);
                                                context.startActivity(intent);
                                                dialog.dismiss();
                                            } else {
                                                Intent intent = new Intent(context, Wall.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                intent.putExtra("flag", 5);
                                                context.startActivity(intent);
                                                dialog.dismiss();
                                            }

                                        }
                                    });


                                    try {
                                        Dialog dialog = alert.create();
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    AlertDialog.Builder alert;
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(context);
                                    }
                                    alert.setTitle("Alert!");
                                    alert.setMessage("Some thing Went Wrong");


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

                            }


                            if (flagValue == 2) {
                                if (msg.equals("Updated Successfully")) {


                                    AlertDialog.Builder alert;
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(context);
                                    }
                                    alert.setTitle("Successfull");
                                    alert.setMessage("Updated Successfully");


                                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
                                        dev=preferences2.edit();

                                        dev.putString("email_txt",email_login);
                                        dev.putString("password_txt",password_login);
                                        dev.commit();
*/
                                            if (class_status.equals("0")) {
                                                Intent intent = new Intent(context, Wall.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                intent.putExtra("flag", 3);
                                                context.startActivity(intent);
                                                dialog.dismiss();
                                            } else {
                                                Intent intent = new Intent(context, Wall.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                intent.putExtra("flag", 5);
                                                context.startActivity(intent);
                                                dialog.dismiss();
                                            }
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
                                } else {
                                    AlertDialog.Builder alert;
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(context);
                                    }
                                    alert.setTitle("Alert!");
                                    alert.setMessage("Some thing Went Wrong");


                                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
                                        dev=preferences2.edit();

                                        dev.putString("email_txt",email_login);
                                        dev.putString("password_txt",password_login);
                                        dev.commit();
*/
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

                if (flagValue == 1) {
                    params.put("action", "Deletedride");
                }
                if (flagValue == 2) {
                    params.put("action", "Makemainpic");
                    params.put("type", type);
                }

                if (helper.rides_status == 1) {
                    params.put("action", "Deleted_ride");
                    params.put("ride_id", employer_list.get(1).getRide_id());
                }

                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("cover_photo_id", cover_id);
                // params.put("type",type);

                Log.d("params", params.toString());
                return params;


            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    private void makeStringReq2(final String ride_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("params", response.toString());
                        int maxLogSize = 1000;
                        for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                            int start1 = i * maxLogSize;
                            int end = (i + 1) * maxLogSize;
                            end = end > response.length() ? response.toString().length() : end;
                            Log.d("RideData", ">>>>>>" + response.toString().substring(start1, end));

                        }
                        try {
                            JSONObject Object = new JSONObject(response.toString());


                            JSONArray jsonArray = Object.getJSONArray("result");
                            ArrayList<Cover_Photo_List> cover_photo = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Cover_Photo_List photo_entity = new Cover_Photo_List();


                                photo_entity.setCover_photo_id(jsonObject.getString("ride_id"));
//                                photo_entity.setCover_photo(jsonObject.getString("640_640"));
//                                photo_entity.setUser_id(jsonObject.getString("user_id"));
//                                photo_entity.setDatetime(jsonObject.getString("datetime"));
//                                photo_entity.setMainPic(jsonObject.getString("photo_url"));
//                                photo_entity.setDescription(jsonObject.getString("description"));

                                if (helper.status.equals("myprofile_ride")) {
                                    photo_entity.setClass_status("1");
                                } else if (helper.status.equals("otherprofile_ride")) {
                                    photo_entity.setClass_status("2");
                                } else {
                                    photo_entity.setClass_status("0");
                                }
                                list.add(photo_entity);
                            }
                            if (page == 0) {
                                curSize = 0;
                                Log.d("6666", "66666");
                                cover_photo.addAll(list);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
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


                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "Deleted_ride");
                params.put("ride_id", ride_id);
                Log.d("Deleted_ride", params.toString());

                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }
}