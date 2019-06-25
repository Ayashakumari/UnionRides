package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import mx.bigapps.unionrides.Activity.BlockList;
import mx.bigapps.unionrides.Activity.FollowingList;
import mx.bigapps.unionrides.Activity.MyProfile;
import mx.bigapps.unionrides.Activity.PublicProfile;
import mx.bigapps.unionrides.Activity.Search;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.NetWorkUserList;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;

/**
 * Created by seemtech2 on 05-10-2017.
 */

public class Blocklist_Adapter extends RecyclerView.Adapter<Blocklist_Adapter.ItemViewHolder> {

    Context context;
    Activity activity;

    ArrayList<NetWorkUserList> employer_list = new ArrayList<NetWorkUserList>();
    int mCurrentPlayingPosition = -1;


    public Blocklist_Adapter(Context context, ArrayList<NetWorkUserList> employer_list) {
        this.context = context;
        this.employer_list = employer_list;


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvname, custom_employee_price, custom_employee_status;
        RelativeLayout container2_offer;
        Button btnaccpt;
        CircleImageView profile_image;
        ImageView clubimage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            btnaccpt = (Button) itemView.findViewById(R.id.btaccept);
            tvname = (TextView) itemView.findViewById(R.id.tvname);
            profile_image = (CircleImageView) itemView.findViewById(R.id.profile_image);
            clubimage = (ImageView) itemView.findViewById(R.id.clubimage);

        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder viewHolder, final int position) {

        viewHolder.tvname.setText(employer_list.get(position).getFullname());
        try {
            String image = "" + employer_list.get(position).getProfileimage();
            if (!image.equals("")) {
                Picasso.with(context)
                        .load(employer_list.get(position).getProfileimage())
                        .noFade()
                        .error(R.drawable.loadinguni)

                        .into(viewHolder.profile_image);

                //  UrlImageViewHelper.setUrlDrawable(holder.find_people_img,find_peoplelist.get(position).getImage());
            } else {
                viewHolder.profile_image.setBackgroundResource(R.drawable.loadinguni);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PublicProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                helper.user_id = employer_list.get(position).getUser_id();
                helper.back_status = "search";
                context.startActivity(intent);
            }
        });
        if (employer_list.get(position).getType().equals("rider")) {
            viewHolder.clubimage.setVisibility(View.GONE);
        }

        viewHolder.btnaccpt.setText("Unblock");
        viewHolder.btnaccpt.setBackgroundResource(R.drawable.fanbtn);
        viewHolder.btnaccpt.setTextColor(Color.parseColor("#FFFFFF"));

        viewHolder.btnaccpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(context);
                }
                alert.setMessage("Do you want to Unblock " + employer_list.get(position).getFullname());


                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Blockuser(employer_list.get(position).getUser_id());

                            if (employer_list.size() == 1) {
                                BlockList.notFound();
                            }
                            employer_list.remove(position);
                            notifyDataSetChanged();


                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        dialog.dismiss();
                    }

                });


                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
                // showAlertdecline("Do you want to unfriend " + employer_list.get(position).getFullname(), employer_list.get(position).getUser_id(),position);


            }
        });
    }

    private void Blockuser(final String user_id) {

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
                                Log.d("Blockuser", response.toString().substring(start1, end));
                            }
                            if (obj.has("msg")) {
                                String msg = obj.getString("msg");
                            }
                            if (obj.has("status")) {
                                String status = obj.getString("status");
                                if (status.equals("Unblocked")) {
                                    //  block_status = "No";

                                }
                                if (status.equals("Blocked")) {
                                    // block_status = "Yes";
                                }
                            }

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
                            Toast.makeText(activity, "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(activity, "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(activity, "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show();
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

//


                params.put("action", "Blockuser");
                params.put("other_user_id", user_id);
                params.put("user_id", PrefMangr.getInstance().getUserId());

                Log.d("Blockuser", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }


    @Override
    public int getItemCount() {
        return employer_list.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchitemlist, parent, false);
        final ItemViewHolder viewHolder = new ItemViewHolder(v);

        return viewHolder;
    }

}
