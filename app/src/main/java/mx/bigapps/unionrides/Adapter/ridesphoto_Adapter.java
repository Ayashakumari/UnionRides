//package mx.bigapps.unionrides.Adapter;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Handler;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkError;
//import com.android.volley.NoConnectionError;
//import com.android.volley.ParseError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.ServerError;
//import com.android.volley.TimeoutError;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import mx.bigapps.unionrides.Activity.Preview_firstride;
//import mx.bigapps.unionrides.Activity.Rides;
//import mx.bigapps.unionrides.Activity.Wall;
//import mx.bigapps.unionrides.Application.PrefMangr;
//import mx.bigapps.unionrides.Fragment.Posts;
//import mx.bigapps.unionrides.Fragment.PostsWall;
//import mx.bigapps.unionrides.Model.Cover_Photo_List;
//import mx.bigapps.unionrides.R;
//import mx.bigapps.unionrides.utils.Apis;
//import mx.bigapps.unionrides.utils.helper;
//
///**
// * Created by seemtech2 on 05-10-2017.
// */
//
//public class ridesphoto_Adapter extends BaseAdapter {
//
//    Context context;
//    Activity activity;
//
//    ProgressBar progressBar;
//    ArrayList<Cover_Photo_List> employer_list = new ArrayList<Cover_Photo_List>();
//    int mCurrentPlayingPosition = -1;
//    LayoutInflater mInflater;
//    private ProgressDialog progress_dialog;
//    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
//    private String progress_dialog_msg = "", tag_string_req = "string_req";
//    String msg, cover_id, class_status;
//    int flagValue = 0;
//    boolean bool = true;
//    String type;
//
//
//    public ridesphoto_Adapter(Activity context, ArrayList<Cover_Photo_List> employer_list) {
//        this.activity = context;
//        this.context = context;
//        this.employer_list = employer_list;
//
//
//        mInflater = LayoutInflater.from(context);
//
//    }
//
//    @Override
//    public int getCount() {
//        return employer_list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//
//            convertView = mInflater.inflate(R.layout.riderphotolist, null);
//            RelativeLayout footer = (RelativeLayout) convertView.findViewById(R.id.footer);
//            progressBar = (ProgressBar) convertView.findViewById(R.id.progress);
//            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
//            final float scale = context.getResources().getDisplayMetrics().density;
//            int pixels = (int) (200 * scale + 0.5f);
//           /* if (helper.status.equals("otherprofile_ride") & employer_list.size() == 1) {
//                RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT, pixels);
//                rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//                rlimage.setLayoutParams(rel_btn);
//                // imageView.getLayoutParams().height = 400;
//                //imageView.requestLayout();
//            }*/
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (helper.status.equals("otherprofile_ride")) {
//                        Log.d("Json_Array", "hhhjjb"  );
//                        Intent intent = new Intent(context, Rides.class);
//                        PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
//                        intent.putExtra("cover_id", employer_list.get(position).getCover_photo_id());
//                        PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
//                        intent.putExtra("cover_pic", employer_list.get(position).getCover_photo());
//                        context.startActivity(intent);
//
//
//                    }
//                    else if(helper.rides_status==1)
//
//                    {
//                        Log.d("Multiple_Array", "hhhjjb"  );
//                        Intent intent = new Intent(context, Preview_firstride.class);
//                        PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
//                        intent.putExtra("cover_id", employer_list.get(position).getCover_photo_id());
//                        PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
//                        intent.putExtra("cover_pic", employer_list.get(position).getCover_photo());
//                        context.startActivity(intent);
//                    }
//
//                    else  {
//                    Log.d("selected_Array", "hhhjjb"  );
//
//
//
//                        Intent intent = new Intent(context, Rides.class);
//                        PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
//                        intent.putExtra("cover_id", employer_list.get(position).getCover_photo_id());
//                        PrefMangr.getInstance().setCoverPicID(employer_list.get(position).getCover_photo_id());
//                        intent.putExtra("cover_pic", employer_list.get(position).getCover_photo());
//                        context.startActivity(intent);
//
//                    }
//                }
//            });
//         /*   Picasso.with(context)
//                    .load(employer_list.get(position).getCover_photo())
//                    .error(R.drawable.loadinguni)
//                    .placeholder(R.drawable.loadinguni)
//
//                    .into(imageView, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            progressBar.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onError() {
//
//                        }
//                    });*/
//
//            Glide.with(context)
//                    .load(employer_list.get(position).getCover_photo())
//                    .placeholder(R.drawable.loadinguni)
//                    .override(120, 80)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
//                    .into(imageView);
//
//            ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
//            final ImageView star = (ImageView) convertView.findViewById(R.id.star);
////            if (employer_list.get(position).getClass_status().equals("1")){
////               RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////              layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
////                layoutParams.setMargins(0,0,0,0);
////                delete.setLayoutParams(layoutParams);
////            }
////            else {
////                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
////                layoutParams.setMargins(0,0,0,0);
////                delete.setLayoutParams(layoutParams);
////            }
//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("COVER_ID", "<><>" + cover_id);
//                    flagValue = 1;
//                    class_status = employer_list.get(position).getClass_status();
//                    showAlert("Do You Want to delete this Ride", employer_list.get(position).getCover_photo_id());
//                }
//            });
//          /*  if (employer_list.get(position).getMainPic().equals("0")) {
//                star.setImageResource(R.drawable.star_empty);
//                // bool=true;
//            } else {
//                //  bool=false;
//                star.setImageResource(R.drawable.star);
//            }*/
//            if (employer_list.get(position).getClass_status().equals("1")) {
//                // star.setVisibility(View.VISIBLE);
//                delete.setVisibility(View.VISIBLE);
//            } else if (employer_list.get(position).getClass_status().equals("2")) {
//                // star.setVisibility(View.GONE);
//                delete.setVisibility(View.GONE);
//                footer.setVisibility(View.GONE);
//            } else {
//                // star.setVisibility(View.VISIBLE);
//                delete.setVisibility(View.VISIBLE);
//            }
//            star.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (employer_list.get(position).getMainPic().equals("0")) {
//                        flagValue = 2;
//                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                        alertDialogBuilder.setMessage("Doy you want to make this ride favourite");
//                        alertDialogBuilder.setTitle("Alert!");
//                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                type = "1";
//                                class_status = employer_list.get(position).getClass_status();
//                                makeStringReq(employer_list.get(position).getCover_photo_id());
//                                star.setImageResource(R.drawable.star);
//
//                                dialog.dismiss();
//                            }
//
//                        });
//
//                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();
//                        ;
//                    } else {
//                        flagValue = 2;
//                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                        alertDialogBuilder.setMessage("Do you want to unfavourite this ride");
//                        alertDialogBuilder.setTitle("Alert!");
//                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                type = "0";
//                                class_status = employer_list.get(position).getClass_status();
//                                makeStringReq(employer_list.get(position).getCover_photo_id());
//                                // showAlert("Do You Want unfavourite this ride favourite",employer_list.get(position).getCover_photo_id());
//                                star.setImageResource(R.drawable.star_empty);
//
//                                dialog.dismiss();
//                            }
//
//                        });
//
//                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();
//
//                    }
//
//
//                }
//            });
//        }
//
//        return convertView;
//    }
//
//    private void showAlert(String msg, final String cover_id) {
//        Log.d("COVER_ID", "<><>" + cover_id);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setMessage(msg);
//        alertDialogBuilder.setTitle("Alert!");
//        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                makeStringReq(cover_id);
//
//                dialog.dismiss();
//            }
//
//        });
//
//        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }
//
//    public class ItemViewHolder extends RecyclerView.ViewHolder {
//        ImageView delete, star;
//
//        public ItemViewHolder(View itemView) {
//            super(itemView);
//            delete = (ImageView) itemView.findViewById(R.id.delete);
//            star = (ImageView) itemView.findViewById(R.id.star);
//
//        }
//    }
//
//
//    Handler mHandler = new Handler(new Handler.Callback() {
//
//        @Override
//        public boolean handleMessage(android.os.Message msg) {
//
//            switch (msg.what) {
//                case SHOW_PROG_DIALOG:
//                    showProgDialog();
//                    break;
//
//                case HIDE_PROG_DIALOG:
//                    hideProgDialog();
//                    break;
//
//                case LOAD_QUESTION_SUCCESS:
//
//                    break;
//
//                default:
//                    break;
//            }
//
//            return false;
//        }
//
//    });
//
//    @SuppressLint("InlinedApi")
//    private void showProgDialog() {
//        progress_dialog = null;
//        try {
//            if (Build.VERSION.SDK_INT >= 11) {
//                progress_dialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
//            } else {
//                progress_dialog = new ProgressDialog(context);
//            }
//            progress_dialog.setMessage(progress_dialog_msg);
//            progress_dialog.setCancelable(false);
//            progress_dialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // hide progress
//    private void hideProgDialog() {
//        try {
//            if (progress_dialog != null && progress_dialog.isShowing())
//                progress_dialog.dismiss();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void makeStringReq(final String cover_id) {
//        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
//        progress_dialog_msg = "loading...";
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//
//                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
//                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
//                        JSONObject obj = null;
//                        try {
//                            obj = new JSONObject(response.toString());
//                            int maxLogSize = 1000;
//                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
//                                int start1 = i * maxLogSize;
//                                int end = (i + 1) * maxLogSize;
//                                end = end > response.length() ? response.toString().length() : end;
//                                Log.d("Json Data", response.toString().substring(start1, end));
//                            }
//                            msg = obj.getString("msg");
//                            if (flagValue == 1) {
//                                if (msg.equals("Deleted successfully")) {
//
//
//                                    AlertDialog.Builder alert;
//                                    if (Build.VERSION.SDK_INT >= 11) {
//                                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
//                                    } else {
//                                        alert = new AlertDialog.Builder(context);
//                                    }
//                                    alert.setTitle("Successfull");
//                                    alert.setMessage("Deleted successfully");
//
//
//                                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
//                                        dev=preferences2.edit();
//
//                                        dev.putString("email_txt",email_login);
//                                        dev.putString("password_txt",password_login);
//                                        dev.commit();
//                                        /
//*/
//                                            if (class_status.equals("0")) {
//                                                Intent intent = new Intent(context, Wall.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                                intent.putExtra("flag", 3);
//                                                context.startActivity(intent);
//                                                dialog.dismiss();
//                                            } else {
//                                                Intent intent = new Intent(context, Wall.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                                intent.putExtra("flag", 5);
//                                                context.startActivity(intent);
//                                                dialog.dismiss();
//                                            }
//
//                                        }
//                                    });
//
//
//                                    try {
//                                        Dialog dialog = alert.create();
//                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                        dialog.show();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                } else {
//                                    AlertDialog.Builder alert;
//                                    if (Build.VERSION.SDK_INT >= 11) {
//                                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
//                                    } else {
//                                        alert = new AlertDialog.Builder(context);
//                                    }
//                                    alert.setTitle("Alert!");
//                                    alert.setMessage("Some thing Went Wrong");
//
//
//                                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
//                                        dev=preferences2.edit();
//
//                                        dev.putString("email_txt",email_login);
//                                        dev.putString("password_txt",password_login);
//                                        dev.commit();
//*/
//                                            dialog.dismiss();
//
//                                        }
//                                    });
//
//
//                                    try {
//                                        Dialog dialog = alert.create();
//                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                        dialog.show();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                            }
//
//
//                            if (flagValue == 2) {
//                                if (msg.equals("Updated Successfully")) {
//
//
//                                    AlertDialog.Builder alert;
//                                    if (Build.VERSION.SDK_INT >= 11) {
//                                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
//                                    } else {
//                                        alert = new AlertDialog.Builder(context);
//                                    }
//                                    alert.setTitle("Successfull");
//                                    alert.setMessage("Updated Successfully");
//
//
//                                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
//                                        dev=preferences2.edit();
//
//                                        dev.putString("email_txt",email_login);
//                                        dev.putString("password_txt",password_login);
//                                        dev.commit();
//*/
//                                            if (class_status.equals("0")) {
//                                                Intent intent = new Intent(context, Wall.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                                intent.putExtra("flag", 3);
//                                                context.startActivity(intent);
//                                                dialog.dismiss();
//                                            } else {
//                                                Intent intent = new Intent(context, Wall.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                                intent.putExtra("flag", 5);
//                                                context.startActivity(intent);
//                                                dialog.dismiss();
//                                            }
//                                            dialog.dismiss();
//
//                                        }
//                                    });
//
//
//                                    try {
//                                        Dialog dialog = alert.create();
//                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                        dialog.show();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                } else {
//                                    AlertDialog.Builder alert;
//                                    if (Build.VERSION.SDK_INT >= 11) {
//                                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
//                                    } else {
//                                        alert = new AlertDialog.Builder(context);
//                                    }
//                                    alert.setTitle("Alert!");
//                                    alert.setMessage("Some thing Went Wrong");
//
//
//                                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
//                                        dev=preferences2.edit();
//
//                                        dev.putString("email_txt",email_login);
//                                        dev.putString("password_txt",password_login);
//                                        dev.commit();
//*/
//                                            dialog.dismiss();
//
//                                        }
//                                    });
//
//
//                                    try {
//                                        Dialog dialog = alert.create();
//                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                        dialog.show();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                            }
//
////*/
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } catch (NullPointerException e) {
//                            e.printStackTrace();
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
//                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
////							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                            Toast.makeText(context, "Time Out Error", Toast.LENGTH_LONG).show();
//                        } else if (error instanceof AuthFailureError) {
//                            Toast.makeText(context, "Authentication Error", Toast.LENGTH_LONG).show();
//                            //TODO
//                        } else if (error instanceof ServerError) {
////							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                            Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
//                            //TODO
//                        } else if (error instanceof NetworkError) {
//                            Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show();
////							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                            //TODO
//
//                        } else if (error instanceof ParseError) {
//
////							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                            //TODO
//                        }
//
//                    }
//                }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                if (flagValue == 1) {
//                    params.put("action", "Deletedride");
//                }
//                if (flagValue == 2) {
//                    params.put("action", "Makemainpic");
//                    params.put("type", type);
//                }
//
//                params.put("user_id", PrefMangr.getInstance().getUserId());
//
//                params.put("cover_photo_id", cover_id);
//                // params.put("type",type);
//
//
//                Log.d("params", params.toString());
//                return params;
//            }
//
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(stringRequest);
//        // Adding request to request queue
////        AppController.getInstance().addToRequestQueue(stringRequest,
////                tag_string_req);
//
//        // Cancelling request
//        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
//    }
//}
//
