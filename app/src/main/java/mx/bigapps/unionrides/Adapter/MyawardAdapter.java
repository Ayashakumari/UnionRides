package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Activity.Add_Award;
import mx.bigapps.unionrides.Activity.GPSTracker;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Fragment.Awards;
import mx.bigapps.unionrides.Model.Award_List_Entity;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;

/**
 * Created by seemtech2 on 05-10-2017.
 */

public class MyawardAdapter extends RecyclerView.Adapter<MyawardAdapter.ItemViewHolder> {

    Context context;
    Activity activity;

    ArrayList<Award_List_Entity> employer_list = new ArrayList<Award_List_Entity>();
    int mCurrentPlayingPosition = -1;
    GPSTracker gpsTracker;


    public MyawardAdapter(Context context, ArrayList<Award_List_Entity> employer_list) {
        this.context = context;
        this.employer_list = employer_list;
        gpsTracker = new GPSTracker(context);


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView btdelte, btedit;
        TextView awarddname, awarddate, date, time, location, description, follower;
        RelativeLayout container2_offer;
        ImageView imgEvent;
        LinearLayout edit_layout, join_layout;

        Button join;

        public ItemViewHolder(View itemView) {
            super(itemView);
            btdelte = (ImageView) itemView.findViewById(R.id.evntdelete);
            btedit = (ImageView) itemView.findViewById(R.id.evntedit);
            imgEvent = (ImageView) itemView.findViewById(R.id.imgEvent);
            awarddate = (TextView) itemView.findViewById(R.id.awarddate);
            awarddname = (TextView) itemView.findViewById(R.id.awardname);

        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {
        //  AlleventData item = employer_list.get(position);

        itemViewHolder.awarddname.setText(employer_list.get(position).getAward_name());
        itemViewHolder.awarddate.setText(employer_list.get(position).getAward_date());
        if (helper.status.equals("otherprofile_ride")) {
            itemViewHolder.btdelte.setVisibility(View.GONE);
            itemViewHolder.btedit.setVisibility(View.GONE);
        }
        itemViewHolder.imgEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Awards.setcoverPic(context, employer_list.get(position).getImage640_640(), employer_list.get(position).getAward_picture());

            }
        });

        try {
            String image = "" + employer_list.get(position);
            if (!TextUtils.isEmpty(employer_list.get(position).getImage640_640())) {
                Picasso.with(context)
                        .load(employer_list.get(position).getImage640_640())
                        .noFade()
                        .placeholder(R.drawable.loadinguni)
                        .error(R.drawable.loadinguni)
                        .into(itemViewHolder.imgEvent);
                //  UrlImageViewHelper.setUrlDrawable(holder.find_people_img,find_peoplelist.get(position).getImage());
            } else {
                itemViewHolder.imgEvent.setBackgroundResource(R.drawable.loadinguni);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        itemViewHolder.btdelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  employer_list.remove(position);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                alertDialogBuilder.setMessage("Would you like to delete this award");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        if (employer_list.size() == 1) {
                            Awards.setaddAward(context);


                        }
                        deleteAward(employer_list.get(position).getAward_id(), position);

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
               /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                alertDialogBuilder.setMessage("Would you like to edit this Award");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {


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

            }*/
                Intent intent = new Intent(context, Add_Award.class);

                intent.putExtra("flag", "1");
                intent.putExtra("award_name", employer_list.get(position).getAward_name());
                intent.putExtra("award_id", employer_list.get(position).getAward_id());
                intent.putExtra("award_date", employer_list.get(position).getAward_date());
                intent.putExtra("award_pic", employer_list.get(position).getAward_picture());
                context.startActivity(intent);
            }
        });

    }

    private void deleteAward(final String event_id, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;

                        JSONObject obj = null;
                        Log.d("Deleteaward", response.toString());
                        try {
                            obj = new JSONObject(response.toString());
                            msg = obj.getString("msg");
                            employer_list.remove(position);
                            notifyDataSetChanged();

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

                params.put("action", "Deleted_ride_award");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("award_id", event_id);
                Log.d("Deleteaward", params.toString());
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


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.award, parent, false);

        return new ItemViewHolder(v);


    }


}
