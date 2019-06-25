package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Activity.Edit_Events;
import mx.bigapps.unionrides.Activity.GPSTracker;
import mx.bigapps.unionrides.Activity.PostPreviewimage;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.AlleventData;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;

/**
 * Created by seemtech2 on 05-10-2017.
 */

public class


Myevents_Adapter extends RecyclerView.Adapter<Myevents_Adapter.ItemViewHolder> {

    Context context;
    Activity activity;

    ArrayList<AlleventData> employer_list = new ArrayList<AlleventData>();
    int mCurrentPlayingPosition = -1;
    GPSTracker gpsTracker;


    public Myevents_Adapter(Context context, ArrayList<AlleventData> employer_list) {
        this.context = context;
        this.employer_list = employer_list;
        gpsTracker = new GPSTracker(context);


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView btdelte, btedit;
        TextView eventName, postedby, date, time, location, description, follower;
        RelativeLayout container2_offer;
        ImageView imgEvent;
        LinearLayout edit_layout, join_layout;

        Button join;

        public ItemViewHolder(View itemView) {
            super(itemView);
            btdelte = (ImageView) itemView.findViewById(R.id.evntdelete);
            btedit = (ImageView) itemView.findViewById(R.id.evntedit);
            imgEvent = (ImageView) itemView.findViewById(R.id.imgEvent);
            eventName = (TextView) itemView.findViewById(R.id.eventName);
            postedby = (TextView) itemView.findViewById(R.id.postedby);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            location = (TextView) itemView.findViewById(R.id.location);
            description = (TextView) itemView.findViewById(R.id.description);
            edit_layout = (LinearLayout) itemView.findViewById(R.id.edit_layout);
            join_layout = (LinearLayout) itemView.findViewById(R.id.join_layout);
            join = (Button) itemView.findViewById(R.id.join);
            follower = (TextView) itemView.findViewById(R.id.following);
        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {
        AlleventData item = employer_list.get(position);

        itemViewHolder.eventName.setText(employer_list.get(position).getEventTitle());
        itemViewHolder.postedby.setText("Posted By: " + employer_list.get(position).getPostedby());
        itemViewHolder.follower.setText(employer_list.get(position).getEventjoinCount() + " people Going");
        itemViewHolder.date.setText("Start Date: " + employer_list.get(position).getStartdate() + " " + employer_list.get(position).getStarttime());
        itemViewHolder.time.setText("End Date: " + employer_list.get(position).getEnddate() + " " + employer_list.get(position).getEndtime());
        itemViewHolder.location.setText("Location: " + employer_list.get(position).getEventLocation());
        itemViewHolder.description.setText(employer_list.get(position).getDecription());
        String text = "<font color='#808080'>Location: </font><font color='#70b5eb'>" + employer_list.get(position).getEventLocation() + "." + "</font>";

        itemViewHolder.location.setMovementMethod(LinkMovementMethod.getInstance());
        itemViewHolder.location.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        String myString = itemViewHolder.location.getText().toString();
        int i1 = myString.indexOf(":");
        int i2 = myString.indexOf(".");
        Spannable mySpannable = (Spannable) itemViewHolder.location.getText();
        ClickableSpan myClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                alertDialogBuilder.setMessage("Do you want to redirect to Google map");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        String lat = employer_list.get(position).getLatitude();
                        String lang = employer_list.get(position).getLongitude();
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" + lat + "," + lang + "&daddr=" + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude()));
                        context.startActivity(intent);

                    }

                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.cancel();

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }

            @Override
            public void updateDrawState(final TextPaint textPaint) {
                textPaint.setUnderlineText(true);
            }
        };

        mySpannable.setSpan(myClickableSpan, i1 + 2, i2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mySpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#70b5eb")), i1 + 2, i2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        itemViewHolder.description.setTag(position);

        itemViewHolder.itemView.setTag(item);
        itemViewHolder.imgEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PostPreviewimage.class);
                intent.putExtra("image", employer_list.get(position).getEvent_image());
                context.startActivity(intent);
            }
        });

        try {
            String image = "" + employer_list.get(position);
            if (!TextUtils.isEmpty(employer_list.get(position).getEvent_image())) {
                Glide.with(context)
                        .load(employer_list.get(position).getImage320())
                        .placeholder(R.drawable.loadinguni)
                        .error(R.drawable.loadinguni)
                        .override(320, 320)

                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(itemViewHolder.imgEvent);
                //  UrlImageViewHelper.setUrlDrawable(holder.find_people_img,find_peoplelist.get(position).getImage());
            } else {
                itemViewHolder.imgEvent.setBackgroundResource(R.drawable.loadinguni);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (employer_list.get(position).getUser_profile_status().equals("1")) {
            itemViewHolder.edit_layout.setVisibility(View.GONE);
            itemViewHolder.join_layout.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(employer_list.get(position).getEventjoinCount()) && employer_list.get(position).getEventjoinCount().equals("0")) {
                itemViewHolder.follower.setVisibility(View.GONE);
            } else {
                itemViewHolder.follower.setText(employer_list.get(position).getEventjoinCount() + " PEOPLE GOING");
            }
            if (employer_list.get(position).getEvent_join_status().equals("yes")) {
                itemViewHolder.join.setText("joined");
                itemViewHolder.join.setBackgroundResource(R.drawable.fanbtn);
                itemViewHolder.join.setTextColor(Color.parseColor("#FFFFFF"));

            }
            itemViewHolder.join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (employer_list.get(position).getEvent_join_status().equals("yes")) {
                        showAlert(" You have already joined this Event");
                    } else {

                        joinEvent(employer_list.get(position).getEvent_id());
                        itemViewHolder.join.setText("joined");
                        itemViewHolder.join.setBackgroundResource(R.drawable.fanbtn);
                        itemViewHolder.join.setTextColor(Color.parseColor("#FFFFFF"));
                        itemViewHolder.follower.setText(Integer.parseInt(employer_list.get(position).getEventjoinCount()) + 1 + " PEOPLE GOING");
                    }


                }
            });
        } else {
            itemViewHolder.edit_layout.setVisibility(View.VISIBLE);
            itemViewHolder.join_layout.setVisibility(View.GONE);

        }
       /* itemViewHolder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                alertDialogBuilder.setMessage("Do you want to redirect to Google map");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        String lat = employer_list.get(position).getLatitude();
                        String lang = employer_list.get(position).getLongitude();
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" + lat + "," + lang + "&daddr=" + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude()));
                        context.startActivity(intent);

                    }

                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.cancel();

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });*/

        itemViewHolder.btdelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  employer_list.remove(position);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                alertDialogBuilder.setMessage("Would you like to delete this post");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        deleteEvent(employer_list.get(position).getEvent_id(), position);

                        notifyDataSetChanged();
                        dialog1.cancel();

                    }

                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.cancel();

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });
        itemViewHolder.btedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                alertDialogBuilder.setMessage("Would you like to edit this post");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        Intent intent = new Intent(context, Edit_Events.class);

                        intent.putExtra("event_title", employer_list.get(position).getEventTitle());
                        intent.putExtra("event_id", employer_list.get(position).getEvent_id());
                        intent.putExtra("start_date", employer_list.get(position).getStartdate());
                        intent.putExtra("start_time", employer_list.get(position).getStarttime());
                        intent.putExtra("end_date", employer_list.get(position).getEnddate());
                        intent.putExtra("end_time", employer_list.get(position).getEndtime());
                        intent.putExtra("event_location", employer_list.get(position).getEventLocation());
                        intent.putExtra("latitude", employer_list.get(position).getLatitude());
                        intent.putExtra("longitude", employer_list.get(position).getLongitude());
                        intent.putExtra("publice", employer_list.get(position).getProfiletype());
                        intent.putExtra("description", employer_list.get(position).getDecription());
                        intent.putExtra("image", employer_list.get(position).getEvent_image());
                        // intent.putExtra("invite_friend", employer_list.get(position).geti);
                        context.startActivity(intent);

                        dialog1.cancel();

                    }

                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.cancel();

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

    }

    private void deleteEvent(final String event_id, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;

                        JSONObject obj = null;
                        Log.d("Deletedevent", response.toString());
                        try {
                            obj = new JSONObject(response.toString());
                            msg = obj.getString("msg");
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                            alertDialogBuilder.setMessage(msg);

                            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    employer_list.remove(position);
                                    notifyDataSetChanged();

                                }

                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();


                        } catch (JSONException e1) {

                            e1.printStackTrace();
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

                params.put("action", "Deletedevent");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("event_id", event_id);
                Log.d("Deletedevent", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    private void showAlert(final String message) {
        final AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= 11) {
            alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        } else {
            alert = new AlertDialog.Builder(context);
        }
        alert.setMessage(message);


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {

                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                dialog.dismiss();
            }

        });


       /* alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {


                } catch (Exception e) {
                    e.printStackTrace();
                }


                dialog.dismiss();

            }
        });*/
        try {
            Dialog dialog = alert.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return employer_list.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /*View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myeventslist, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);*/
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myeventslist, parent, false);

        return new ItemViewHolder(v);


    }


    private void joinEvent(final String user_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;

                        JSONObject obj = null;
                        Log.d("Joinevent", response.toString());
                        try {
                            obj = new JSONObject(response.toString());
                            msg = obj.getString("msg");


                        } catch (JSONException e1) {

                            e1.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                      /*  if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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
                        }*/

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Joinevent");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("event_id", user_id);
                Log.d("Joinevent", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }


}
