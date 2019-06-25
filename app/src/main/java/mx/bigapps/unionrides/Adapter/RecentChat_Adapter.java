package mx.bigapps.unionrides.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.RecentChatEntity;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;

/**
 * Created by seemtech2 on 26-09-2017.
 */

public class RecentChat_Adapter extends BaseAdapter {
    public ArrayList mlist_store;
    public LayoutInflater inflater;
    Context context;

    ArrayList<RecentChatEntity> chat_list = new ArrayList<RecentChatEntity>();
    String project_id, friend_id;

    Map<String, String> paramsCount;
    int productquanity = 0;

    public RecentChat_Adapter(Context applicationContext, ArrayList<RecentChatEntity> chat_list) {

        this.chat_list = chat_list;
        this.context = applicationContext;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chat_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ItemViewHolder itemViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_recentchat, parent, false);
            itemViewHolder = new ItemViewHolder(convertView);
            convertView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) convertView.getTag();
        }

        String newstr = chat_list.get(position).getTimee().replaceAll("[^A-Za-z]+", "");
//        Log.d("ALPHA",""+newstr);

        String numberOnly = chat_list.get(position).getTimee().replaceAll("[^0-9]", "");
        Log.d("ALPHA", "" + numberOnly);

        itemViewHolder.chat_name.setText(chat_list.get(position).getFull_name());
        itemViewHolder.message_txt.setText(chat_list.get(position).getMessage());
        itemViewHolder.day_txt.setText(chat_list.get(position).getTimee());
        // itemViewHolder.project_name.setText(chat_list.get(position).getProject_title());

        if (chat_list.get(position).getUnreade_count().equals("0")) {
            itemViewHolder.txt_chatcount.setVisibility(View.GONE);
        } else {
            itemViewHolder.txt_chatcount.setVisibility(View.VISIBLE);
            itemViewHolder.txt_chatcount.setText(chat_list.get(position).getUnreade_count());
        }


        if (chat_list.get(position).getPicture().equals("")) {
            Drawable d = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                d = context.getDrawable(R.drawable.default_user);
            }
            itemViewHolder.chat_img.setImageDrawable(d);

        } else {


            Picasso.with(context)
                    .load(chat_list.get(position).getPicture())
                    .noFade()
                    .error(R.drawable.default_user)
                    .into(itemViewHolder.chat_img);
        }

        /*itemViewHolder.chat_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,Chat_window.class);
                i.putExtra("firstname",chat_list.get(position).getFull_name());
                i.putExtra("friend_id",chat_list.get(position).getFriend_id());
                i.putExtra("project_id",chat_list.get(position).getProject_id());
                i.putExtra("status","chat");
                context.startActivity(i);
            }
        });*/
//        itemViewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                friend_id =chat_list.get(position).getFriend_id();
//                project_id =chat_list.get(position).getProject_id();
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                alertDialogBuilder.setMessage("Do you want to delete chat history ?");
//                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        if (exp_list.size() > 0) {
//
//                            deleteChat(chat_list.get(position).getFriend_id());
//                            chat_list.remove(position);
//                            notifyDataSetChanged();
////                        }
//                        dialog.dismiss();
//                    }
//
//                });
//
//                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            }
//        });

        return convertView;

    }

 /*   private void deleteChat(final String friend_id) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Deleteexperience", response.toString());
                        JSONObject obj = null;

                        try {
                            obj = new JSONObject(response.toString());
                            // JSONArray jsonArray = obj.getJSONArray("skill_categories");
                               *//* for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String sub_category_id = jsonObject.getString("sub_category_id");
                                    Log.d("skill_id", "  " + sub_category_id);
                                    String category_id = jsonObject.getString("category_id");
                                    Log.d("skill_category_id", "  " + category_id);
                                    String sub_category_name = jsonObject.getString("sub_category_name");
                                    Log.d("sub_category_name", "  " + sub_category_name);


                                }
*//*


                            // }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Deletechathistory");
                params.put("friend_id", "2");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("project_id", "45");

                Log.d("Deletechathistory", String.valueOf(params));
                return params;
            }
//            D/Deleteexperience: {"show":"Deleted successfully"}
        };
// ,  , project_id (All required)
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }*/


    public class ItemViewHolder {
        //ImageView project_img,custom_star1,custom_star2,custom_star3,custom_star4,custom_star5,default_img;
        TextView day_txt, message_txt, chat_name, txt_chatcount, project_name, project_free_txt, active_txt, expire_txt;
        RelativeLayout container_offer;
        CircleImageView chat_img;
        RelativeLayout chat_relative;

        Button buttonDelete;

        public ItemViewHolder(View itemView) {

            day_txt = (TextView) itemView.findViewById(R.id.day_txt);
            // project_name= (TextView) itemView.findViewById(R.id.project_name);
            txt_chatcount = (TextView) itemView.findViewById(R.id.txt_chatcount);
            message_txt = (TextView) itemView.findViewById(R.id.message_txt);
            chat_name = (TextView) itemView.findViewById(R.id.chat_name);
            chat_img = (CircleImageView) itemView.findViewById(R.id.chat_img);
            chat_relative = (RelativeLayout) itemView.findViewById(R.id.chat_relative);
//            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
//            buttonDelete = (Button) itemView.findViewById(R.id.delete);


        }
    }


    private void deleteChat(final String friend_id) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Deleteexperience", response.toString());
                        JSONObject obj = null;

                        try {
                            obj = new JSONObject(response.toString());
                            // JSONArray jsonArray = obj.getJSONArray("skill_categories");
                               /* for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String sub_category_id = jsonObject.getString("sub_category_id");
                                    Log.d("skill_id", "  " + sub_category_id);
                                    String category_id = jsonObject.getString("category_id");
                                    Log.d("skill_category_id", "  " + category_id);
                                    String sub_category_name = jsonObject.getString("sub_category_name");
                                    Log.d("sub_category_name", "  " + sub_category_name);


                                }
*/


                            // }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Deletechathistory");
                params.put("friend_id", "2");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("project_id", "45");

                Log.d("Deletechathistory", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


}