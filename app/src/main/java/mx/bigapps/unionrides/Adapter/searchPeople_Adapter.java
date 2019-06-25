package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.bigapps.unionrides.Activity.PublicProfile;
import mx.bigapps.unionrides.Activity.Search;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.RequstedUserList;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;

/**
 * Created by seemtech2 on 05-10-2017.
 */

public class searchPeople_Adapter extends RecyclerView.Adapter<searchPeople_Adapter.ItemViewHolder> {

    Context context;
    Activity activity;

    ArrayList<RequstedUserList> employer_list = new ArrayList<RequstedUserList>();
    int mCurrentPlayingPosition = -1;
    Search search= new Search();


    public searchPeople_Adapter(Context context, ArrayList<RequstedUserList> employer_list) {
        this.context = context;
        this.employer_list = employer_list;


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvname, custom_employee_price, custom_employee_status;
        RelativeLayout relative_layout;
        CircleImageView profileimage;
        Button btnaccpt;
        ImageView clubimage;


        public ItemViewHolder(View itemView) {
            super(itemView);
            tvname = (TextView) itemView.findViewById(R.id.tvname);
            relative_layout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
            profileimage = (CircleImageView) itemView.findViewById(R.id.profile_image);
            btnaccpt = (Button) itemView.findViewById(R.id.btaccept);
            clubimage = (ImageView) itemView.findViewById(R.id.clubimage);

        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {
        itemViewHolder.tvname.setText(employer_list.get(position).getFullname());
        itemViewHolder.clubimage.setVisibility(View.GONE);
        try {
            String image = "" + employer_list.get(position).getProfileimage();
            if (!image.equals("")) {
                Picasso.with(context)
                        .load(employer_list.get(position).getProfileimage())
                        .noFade()
                        .error(R.drawable.userimages)
                        .into(itemViewHolder.profileimage);
                //  UrlImageViewHelper.setUrlDrawable(holder.find_people_img,find_peoplelist.get(position).getImage());
            } else {
                itemViewHolder.profileimage.setBackgroundResource(R.drawable.userimages);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        itemViewHolder.relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PublicProfile.class);
                context.startActivity(intent);

            }
        });
        itemViewHolder.tvname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PublicProfile.class);
                context.startActivity(intent);
            }
        });
        itemViewHolder.btnaccpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptrequest(employer_list.get(position).getUser_id());
                employer_list.remove(position);
                // showAlert("Do you want to accept request of " + employer_list.get(position).getFullname(), employer_list.get(position).getUser_id());

            }


        });

    }

    private void acceptrequest(final String user_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg = null;

                        JSONObject obj = null;
                        Log.d("Acceptdeclinerequest", response.toString());
                        try {
                            obj = new JSONObject(response.toString());
                            msg = obj.getString("msg");
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                            alertDialogBuilder.setMessage(msg);

                            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    search.repostFriendlist(context);
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

                params.put("action", "Acceptdeclinerequest");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("other_user_id", user_id);
                params.put("type", "accept");

                Log.d("Acceptdeclinerequest", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    private void showAlert(final String message, final String user_id) {
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
                    acceptrequest(user_id);
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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchitemlist, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);
        return viewHolder;
    }

}
