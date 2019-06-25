package mx.bigapps.unionrides.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.Cover_Photo_List;
import mx.bigapps.unionrides.Model.Demo;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.helper;

/**
 * Created by we on 10/12/2018.
 */

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.MyViewHolder> {

    ArrayList<Cover_Photo_List> employer_list = new ArrayList<>();
    public LayoutInflater inflater;
    private String comment;
    Context context;
    LinearLayoutManager layoutManager;
    CommentAdapter commentadapter;

    RecyclerView recycling;


    ProgressBar progressBar;
    Message msg;


    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    private ProgressDialog progress_dialog;
    ArrayList<Cover_Photo_List> listData = new ArrayList<>();
//    ArrayList <Cover_Photo_List> cover_photo=new ArrayList<>();


    //LinearLayoutManager manager;
    public PreviewAdapter(Context context, ArrayList<Cover_Photo_List> employer_list) {
        this.context = context;
        this.employer_list = employer_list;

        makeStringReq("", "");


//        listData.clear();
//        for (int count = 0; count < 1; count++) {
//
//            Cover_Photo_List cvModel = new Cover_Photo_List();
//            cvModel.setComment("Hi welcome");
//            cvModel.setComment_id("123" + count);
//
//            listData.add(cvModel);
//        }


        inflater = LayoutInflater.from(context);

    }


    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_previewfirstride, parent, false);
        PreviewAdapter.MyViewHolder vh = new PreviewAdapter.MyViewHolder(v);
        return vh;


    }

    @Override
    public void onBindViewHolder(@NonNull final PreviewAdapter.MyViewHolder holder, final int position) {
        holder.pinchas_cohen.setText(employer_list.get(position).getPosted_by());
        holder.time.setText(employer_list.get(position).getDatetime());
        holder.description.setText(employer_list.get(position).getDescription());
        if (!TextUtils.isEmpty(employer_list.get(position).getProfile_picture())) {


            Picasso.with(context).load(employer_list.get(position).getProfile_picture()).error(R.drawable.default_user).into(holder.default_user, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {


                }
            });
        }


        Log.d("positionhelp", ">>>>>" + helper.pos);

        Log.d("list size", ">>>>>" + employer_list.size());


        Glide.with(context)
                .load(employer_list.get(position).getCover_photo())
                .placeholder(R.drawable.loadinguni)
                .override(120, 80)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.dirst_images);


        holder.preview_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String comments = holder.typecomments.getText().toString();


                makeStringReq(employer_list.get(position).getRide_id(), comments);

                holder.typecomments.setText("");

//                 manager= new LinearLayoutManager(context);

            }
        });


    }


    @Override
    public int getItemCount() {

        return employer_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView default_user, dirst_images;
        TextView pinchas_cohen, time, comments_user, preview_comment;
        EditText typecomments;
        TextView description;

        LinearLayout linear_one;
        ProgressBar progress_dialog;


        public MyViewHolder(View itemView) {
            super(itemView);
            linear_one = itemView.findViewById(R.id.linear_one);

            linear_one = itemView.findViewById(R.id.linear_one);

            recycling = itemView.findViewById(R.id.commentlist);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            description = (TextView) itemView.findViewById(R.id.description);
            comments_user = (TextView) itemView.findViewById(R.id.comments_user);


            LinearLayoutManager manager1 = new LinearLayoutManager(context);
            manager1.setOrientation(LinearLayoutManager.VERTICAL);
            recycling.setLayoutManager(manager1);

//            commentadapter = new CommentAdapter(context.);
//            recycling.setAdapter(commentadapter);



//            recyclerView.setLayoutManager(manager);
//            commentadapter = new CommentAdapter(context,);
//            recycling.setAdapter(commentadapter);LinearLayoutManager manager=new LinearLayoutManager(getActivity());

            default_user = (ImageView) itemView.findViewById(R.id.default_user);
            dirst_images = (ImageView) itemView.findViewById(R.id.dirst_images);

//            final float scale = context.getResources().getDisplayMetrics().density;
            //          int pixels = (int) (200 * scale + 0.5f);

            pinchas_cohen = (TextView) itemView.findViewById(R.id.pinchas_cohen);
            time = (TextView) itemView.findViewById(R.id.time);
            comments_user = (TextView) itemView.findViewById(R.id.comments_user);

            preview_comment = (TextView) itemView.findViewById(R.id.preview_comment);

            typecomments = (EditText) itemView.findViewById(R.id.typecomments);
            comment = typecomments.getText().toString();

//            Intent i = new Intent(context, CommentAdapter.class);
//            i.putExtra("comment", comment);

        }
    }

    private void getCommentsAdapter(ArrayList<Cover_Photo_List> listData) {
        commentadapter = new CommentAdapter(context, listData);
        recycling.setAdapter(commentadapter);
        commentadapter.notifyDataSetChanged();
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

    private void makeStringReq(final String ride_id, final String comments) {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "loading...";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Submit_ride_comment", response.toString());

                        listData.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            Gson gson = new Gson();
                            Demo demo = gson.fromJson(response, Demo.class);

                            for (int i = 0; i < demo.getResponse().size(); i++) {
//                            for (int i = 0; i < listData.size(); i++) {

                                Cover_Photo_List cover_photo_list = new Cover_Photo_List();

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                String comm = jsonObject1.getString("comments");
                                cover_photo_list.setComments(comm);

                                listData.add(cover_photo_list);

                            }

                            getCommentsAdapter(listData);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
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

                params.put("action", "Submit_ride_comment");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("comment", comments);
                params.put("ride_id", "" + ride_id);

                Log.d("Submit_ride_comment", params.toString());
                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}














