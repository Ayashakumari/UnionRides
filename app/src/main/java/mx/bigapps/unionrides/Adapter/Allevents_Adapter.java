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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

import mx.bigapps.unionrides.Activity.GPSTracker;
import mx.bigapps.unionrides.Activity.PostPreviewimage;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.AlleventData;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;

/**
 * Created by seemtech2 on 05-10-2017.
 */

public class Allevents_Adapter extends RecyclerView.Adapter<Allevents_Adapter.ItemViewHolder> {

    Context context;
    Activity activity;

    ArrayList<AlleventData> employer_list = new ArrayList<AlleventData>();
    int mCurrentPlayingPosition = -1;
    GPSTracker gpsTracker;


    public Allevents_Adapter(Context context, ArrayList<AlleventData> employer_list) {
        this.context = context;
        this.employer_list = employer_list;
        gpsTracker = new GPSTracker(context);


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, postedby, date, time, location, description, follower;
        RelativeLayout container2_offer;
        ImageView imgEvent;
        Button join;

        public ItemViewHolder(View itemView) {
            super(itemView);
            join = (Button) itemView.findViewById(R.id.join);
            imgEvent = (ImageView) itemView.findViewById(R.id.imgEvent);
            eventName = (TextView) itemView.findViewById(R.id.eventName);
            postedby = (TextView) itemView.findViewById(R.id.postedby);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            location = (TextView) itemView.findViewById(R.id.location);
            description = (TextView) itemView.findViewById(R.id.description);
            follower = (TextView) itemView.findViewById(R.id.follower);


        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {
        itemViewHolder.setIsRecyclable(false);
        itemViewHolder.eventName.setText(employer_list.get(position).getEventTitle());
        if (employer_list.get(position).getPostedby().equals("null")) {
            itemViewHolder.postedby.setText("Posted by: Admin");
        } else {
            itemViewHolder.postedby.setText("Posted by: " + employer_list.get(position).getPostedby());
        }
        itemViewHolder.follower.setText(employer_list.get(position).getEventjoinCount() + " people Going");
        itemViewHolder.date.setText("Start Date: " + employer_list.get(position).getStartdate() + " " + employer_list.get(position).getStarttime());
        itemViewHolder.time.setText("End Date: " + employer_list.get(position).getEnddate() + " " + employer_list.get(position).getEndtime());


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


        itemViewHolder.description.setText(employer_list.get(position).getDecription());
        if (employer_list.get(position).getEvent_join_status().equals("yes")) {
            itemViewHolder.join.setText("joined");
            itemViewHolder.join.setBackgroundResource(R.drawable.fanbtn);
            itemViewHolder.join.setTextColor(Color.parseColor("#FFFFFF"));

        }
       /* itemViewHolder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/
        itemViewHolder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employer_list.get(position).getEvent_join_status().equals("yes")) {
                    showAlert(" You have already joined this Event");
                } else {

                    joinEvent(employer_list.get(position).getEvent_id());
                    itemViewHolder.join.setText("joined");
                    itemViewHolder.join.setBackgroundResource(R.drawable.fanbtn);
                    itemViewHolder.join.setTextColor(Color.parseColor("#FFFFFF"));


                }

            }

            // showAlertJoin("Do you want to join this Event", employer_list.get(position).getEvent_id(),position);


        });

        try {
            String image = "" + employer_list.get(position);
            if (!image.equals("")) {
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
        itemViewHolder.imgEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.status = "Event";
                Intent intent = new Intent(context, PostPreviewimage.class);
                intent.putExtra("image", employer_list.get(position).getEvent_image());
                context.startActivity(intent);
            }
        });
        itemViewHolder.description.setTag(position);

    }

   /* private void showAlertJoin(final String message, final String user_id, final int position) {
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
                    joinEvent(user_id, position);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                dialog.dismiss();
            }

        });


       *//* alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {


                } catch (Exception e) {
                    e.printStackTrace();
                }


                dialog.dismiss();

            }
        });*//*
        try {
            Dialog dialog = alert.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

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

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return employer_list.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alleventslist, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);

        return viewHolder;
    }

}
