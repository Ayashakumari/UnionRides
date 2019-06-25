package mx.bigapps.unionrides.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.LoadApi;

/**
 * Created by admin on 14-11-2017.
 */
public class PreviewIamge extends AppCompatActivity {
    Zoom img;
    Intent intent;
    Bitmap bitmap;
    Button upload_image;
    ImageView btnclose;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String Stringimage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview);
        img = (Zoom) findViewById(R.id.pager);
        upload_image = (Button) findViewById(R.id.upload_image);
        intent = getIntent();
        Stringimage = intent.getStringExtra("Stringimage");
      /*  try {

            if (!Stringimage.equals("")) {
                Picasso.with(this)
                        .load(Stringimage)
                        .noFade()
                        .error(R.drawable.userimages)
                        .into(img);
                //  UrlImageViewHelper.setUrlDrawable(holder.find_people_img,find_peoplelist.get(position).getImage());
            } else {
                img.setBackgroundResource(R.drawable.userimages);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        if (getIntent().hasExtra("byteArray")) {
            Bundle extras = getIntent().getExtras();
            byte[] byteArray = extras.getByteArray("byteArray");

            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


            img.setImageBitmap(bmp);
         /*   bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
             img.setImageBitmap(bitmap);*/
        }
        //  img.setImageResource(R.drawable.imagecar);

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        btnclose = (ImageView) findViewById(R.id.btnClose);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewIamge.this, Wall.class);
                startActivity(intent);
                finish();
            }
        });

    }

    Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

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
                progress_dialog = new ProgressDialog(PreviewIamge.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(PreviewIamge.this);
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

    private AsyncLoadData asyncLoad;

    private void loadData() {
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData();
            asyncLoad.execute();
        }

    }

    private class AsyncLoadData extends AsyncTask<String, Void, Void> {
        boolean flag = false;
        String email, mobile_number, name;

        public AsyncLoadData() {
            /*this.email = email;
            this.name = name;
            this.mobile_number = moNo;*/

        }

        @Override
        protected Void doInBackground(String... strings) {

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            LoadApi api = new LoadApi();
            try {


                JSONObject jobj = api.Action_profileSetting_Image("Postnews", PrefMangr.getInstance().getUserId(), imageBytes,"");

                //  jobj = api.Action_profileSetting_noImage(action, user_id, first_name, last_name, email_address, mobile_number, date_of_birth, byte_arr);

                JSONObject object = api.getResult1();
                Log.d("Profile_Setting", ">>" + object);
                String msg = object.getString("msg");
                Log.d("Meassage", "  " + msg);
               /* JSONArray jsonArray = object.getJSONArray("portfolio");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String added_date = jsonObject.getString("added_date");
                    Log.d("User_id", "  " + added_date);
                    String user_id = jsonObject.getString("user_id");
                    String portfolio_id = jsonObject.getString("portfolio_id");
                    String portfolio_thumb_url = jsonObject.getString("portfolio_thumb_url");
                    Log.d("portfolio_thumb_url", "  " + portfolio_thumb_url);
                    String portfolio_pic_url = jsonObject.getString("portfolio_pic_url");
                    Log.d("portfolio_pic_url", "  " + portfolio_pic_url);
                    portfoliomodel = new Portfoliomodel(portfolio_id, portfolio_thumb_url, portfolio_pic_url);
                    bitmapArray.add(portfoliomodel);

                }*/


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);

            hideProgDialog();
            startActivity(new Intent(new Intent(PreviewIamge.this, Wall.class)));

           /* try {

                if (msg.equals("Your experience has been added successfully")) {
                  *//*  noPicfound.setVisibility(View.GONE);
                    my_portfolio_recycle.setVisibility(View.VISIBLE);
                    albumAdapter = new AlbumAdapter(Portfolio.this, bitmapArray);
                    my_portfolio_recycle.setAdapter(albumAdapter);
              *//*   *//*
                    Intent intent = new Intent(Portfolio.this, .class);
                    startActivity(intent);*//*

                } else {
                    AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(Portfolio.this, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(Portfolio.this);
                    }
                    alert.setTitle("Alert!");
                    alert.setMessage("Please update all the detail");


                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {


                            } catch (Exception e) {
                                e.printStackTrace();
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


                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }
*/
        }
    }
}
