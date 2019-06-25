package mx.bigapps.unionrides.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Fragment.Awards;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.LoadApi;
import mx.bigapps.unionrides.utils.helper;

/**
 * Created by dev on 09-11-2017.
 */

public class Add_Award extends AppCompatActivity {
    int mYear, mMonth, mDay;
    TextView e_date, a_date;
    int flag = 0;
    CircleImageView upload_image;
    EditText award_name;
    Bitmap bitmap;
    private AsyncLoadData asyncLoad;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    ProgressDialog progress_dialog;
    String msg;
    String awardname, awardpic, awarddate, flg, award_id;
    private static int LOAD_IMAGE_RESULTS = 1000;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.add_award);
        upload_image = (CircleImageView) findViewById(R.id.upload_image);
        award_name = (EditText) findViewById(R.id.award_name);
        a_date = (TextView) findViewById(R.id.award_dt);
        e_date = (TextView) findViewById(R.id.earn_dt);
        ImageView m = (ImageView) findViewById(R.id.award_back);


        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button b = (Button) findViewById(R.id.award_sumit);
        Intent intent = getIntent();
        award_id = intent.getStringExtra("award_id");
        awardname = intent.getStringExtra("award_name");
        awardpic = intent.getStringExtra("award_pic");
        awarddate = intent.getStringExtra("award_date");
        flg = intent.getStringExtra("flag");
        if (flg != null) {
            award_name.setText(awardname);
            a_date.setText(awarddate);
            if (awardpic != null) {


                try {

                    if (!awardpic.equals("")) {
                        Picasso.with(this).load(awardpic)
                                .placeholder(R.drawable.loadinguni)
                                .error(R.drawable.loadinguni)

                                // .networkPolicy(NetworkPolicy.NO_CACHE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(upload_image);
                        // .noFade()

                        //  UrlImageViewHelper.setUrlDrawable(holder.find_people_img,find_peoplelist.get(position).getImage());
                    } else {
                        upload_image.setBackgroundResource(R.drawable.default_user);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date startDate = null;
                String awardname = award_name.getText().toString();
                String newDateString = a_date.getText().toString();
                if (newDateString.equals(awarddate)) {
                    newDateString = "";
                }
               /* String startDateString = "05 dec 2027";
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String newDateString = null;
                try {
                    startDate = df.parse(startDateString);
                    newDateString = df.format(startDate);
                    System.out.println(newDateString);
                    Log.d("newstartdate", newDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/

                if (TextUtils.isEmpty(awardname)) {

                    Toast.makeText(Add_Award.this, "Enter Award Name", Toast.LENGTH_LONG).show();
                    award_name.requestFocus();
                    return;

                } /*else if (TextUtils.isEmpty(newDateString)) {
                    //   nick_name.setError("Enter nick Name");
                    Toast.makeText(Add_Award.this, "Enter Start Date", Toast.LENGTH_LONG).show();
                    a_date.requestFocus();
                    return;
                } */ else {
                    if (flg != null) {
                        editAward(awardname, newDateString);
                    } else {
                        if (bitmap == null) {
                            Toast.makeText(Add_Award.this, "select Award Picture", Toast.LENGTH_LONG).show();
                        } else {
                            if (asyncLoad == null
                                    || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
                                asyncLoad = new AsyncLoadData(awardname, newDateString);
                                asyncLoad.execute();
                            }
                        }
                    }
                }  // startActivity(new Intent(getApplicationContext(), Add_Award.class));
            }
        });

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_RESULTS);

                // Crop.pickImage(Add_Award.this);
            }
        });


        a_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                date();
            }
        });

        e_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;

                date();

            }
        });

    }

    private void editAward(final String awardname, final String date) {
        showProgDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        hideProgDialog();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                Log.d("Editaward", response.toString().substring(start1, end));
                            }
                            msg = obj.getString("msg");
                            if (msg.equals("Edited successfully")) {

                                AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(Add_Award.this, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(Add_Award.this);
                                }
                                alert.setTitle("Successful");
                                alert.setMessage("Award Edited successfully");


                                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        try {
                                          /*  Intent intent = new Intent(Add_Award.this, Rides.class);
                                            intent.putExtra("Award", "Award");
                                            startActivity(intent);*/

                                            Awards.makerestart(Add_Award.this);
                                            finish();

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
                        //   mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Editaward");


                params.put("award_id", award_id);

                params.put("award_name", awardname);

                if (date.equals("")) {

                } else {
                    params.put("award_date", date);
                }

                Log.d("Editaward", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Add_Award.this);
        requestQueue.add(stringRequest);

    }
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        }
        if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == -1 && data != null) {

            Uri selectedImageUri = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                bitmap = rotateImageIfRequired(Add_Award.this, bitmap, selectedImageUri);
                upload_image.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }


            // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (null != selectedImageUri) {
                // Get the path from the Uri
                // String path = getPathFromURI(selectedImageUri);

                // Set the image in ImageView

                // bitmap = getBitmapFromURL(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

            }
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            //  resultView.setImageURI(Crop.getOutput(result));

            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
                upload_image.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(android.os.Message msg) {
            // switch case
            switch (msg.what) {
                case SHOW_PROG_DIALOG:
                    showProgDialog();
                    break;

                case HIDE_PROG_DIALOG:
                    hideProgDialog();
                    break;


                default:
                    break;
            }

            return false;
        }


    });

    private void showProgDialog() {
        // TODO Auto-generated method stub
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(Add_Award.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(Add_Award.this);
            }
            progress_dialog.setMessage("loading");
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void hideProgDialog() {
        // TODO Auto-generated method stub
        try {
            if (progress_dialog != null && progress_dialog.isShowing())
                progress_dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AsyncLoadData extends AsyncTask<String, Void, Void> {
        boolean flag = false;
        String awardname, date;


        public AsyncLoadData(String awardname, String date) {

            this.awardname = awardname;
            this.date = date;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();

            LoadApi api = new LoadApi();
            try {


                JSONObject jobj = api.Action_Awards("Addrideaward", PrefMangr.getInstance().getUserId(), date, awardname, imageBytes);
                Log.d("data", String.valueOf(jobj));
                //jobj = api.Action_profileSetting_noImage(action, user_id, first_name, last_name, email_address, mobile_number, date_of_birth, byte_arr);

                JSONObject object = api.getResult1();
                Log.d("AddAwrd", ">>" + object);

                msg = object.getString("msg");
                Log.d("Meassage", "  " + msg);
               /* JSONObject jarray = object.getJSONObject("show");
                String profileimage = jarray.getString("profileimage");
                PrefMangr.getInstance().setProfilepic(profileimage);*/


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            try {

                if (msg.equals("Award Add successfully")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Add_Award.this);
                    alertDialogBuilder.setMessage("Award Added successfully");
                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            /*Awards.makerestart(Add_Award.this);
                            finish();*/
                            helper.riderfrag = "3";
                            startActivity(new Intent(Add_Award.this, Rides.class));
                            dialog1.cancel();

                        }

                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }

    }

    public void date() {


        // TODO Auto-generated method stub
        // To show current date in the datepicker
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog mDatePicker = new DatePickerDialog(Add_Award.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "yyyy-MM-dd"; //Change as you need
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                if (flag == 0) {
                    a_date.setText(sdf.format(myCalendar.getTime()));

                } else {
                    e_date.setText(sdf.format(myCalendar.getTime()));
                }

                mDay = selectedday;
                mMonth = selectedmonth;
                mYear = selectedyear;
            }
        }, mYear, mMonth, mDay);
        //mDatePicker.setTitle("Select date");
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDatePicker.show();
    }

}
