package mx.bigapps.unionrides.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.Commom_Method;
import mx.bigapps.unionrides.utils.LoadApi;


/**
 * Created by dev on 01-11-2017.
 */

public class Account_Setting extends AppCompatActivity {
    CircleImageView upload;
    private static final int SELECT_PICTURE = 1;
    private static final String TAG = "Add_Specifiction";
    EditText fname, nick_name;
    TextView select_interest;
    Spinner spnr;
    String f, n, nick, full, msg;
    AppCompatRadioButton ride, club;
    String type, profile, radio;
    Bitmap bitmaps = null;
    String AccountType = null;
    RadioGroup radioGroup;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    ProgressDialog progress_dialog;
    String[] Select = new String[]{};
    TextView userType;
    Uri imageUri;
    private static int LOAD_IMAGE_RESULTS = 1000;

    @Override
    protected void onCreate(Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        setContentView(R.layout.account_setting);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        userType = (TextView) findViewById(R.id.accountType);
        upload = (CircleImageView) findViewById(R.id.user_profile_pic);
        ImageView m = (ImageView) findViewById(R.id.acctset_back);


        spnr = (Spinner) findViewById(R.id.as_select_interest);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_text_view, R.id.textView1, Select);
        adapter.setDropDownViewResource(R.layout.spinner_text_view);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });

        ride = (AppCompatRadioButton) findViewById(R.id.radio_rider);
        club = (AppCompatRadioButton) findViewById(R.id.radio_club);
        fname = (EditText) findViewById(R.id.as_fullname);
        nick_name = (EditText) findViewById(R.id.as_Nicname);
        final Button submit = (Button) findViewById(R.id.as_submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Submit();

            }
        });


        type = PrefMangr.getInstance().getuserType();
        if (PrefMangr.getInstance().getuserType().equals("0")) {
            userType.setText(" User Type: Rider");
        } else {
            userType.setText(" User Type: Club");
        }
        if (type.equals("0")) {
            ride.setChecked(true);
        } else {
            club.setChecked(true);
        }
        makeStringReq();




      /*  if (PrefMangr.getInstance().getProfilepic() != null) {
            profile = PrefMangr.getInstance().getProfilepic();
            if (profile != null) {
                if (profile != null) {
                    Glide.with(this)
                            .load(profile)
                            .placeholder(R.drawable.loadinguni) // optional
                            .error(R.drawable.loadinguni)         // optional
                            .into(upload);
                }
            }
        }*/
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crop.pickImage(Account_Setting.this);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_RESULTS);

            }
        });


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
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == -1 && data != null) {

            Uri selectedImageUri = data.getData();

            try {

                bitmaps = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                bitmaps = rotateImageIfRequired(Account_Setting.this, bitmaps, selectedImageUri);
                upload.setImageBitmap(bitmaps);
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

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        }
        if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
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

                bitmaps = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
                upload.setImageBitmap(bitmaps);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private AsyncLoadData asyncLoad;

    public void Submit() {


        full = fname.getText().toString();
        nick = nick_name.getText().toString();


        if (radioGroup.getCheckedRadioButtonId() == R.id.radio_rider) {
            AccountType = "0";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_club) {
            AccountType = "1";
        } else {
            AccountType = null;
            // one of the radio buttons is checked
        }

        if (upload == null) {
            Toast.makeText(this, "please upload you profile picture", Toast.LENGTH_SHORT).show();
        } else if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData();
            asyncLoad.execute();
        }

    }

    private void makeStringReq() {


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
                                Log.d("Myprofiledata", response.toString().substring(start1, end));
                            }
///                            msg=obj.getString("msg");
                            JSONObject object = obj.getJSONObject("users");
                            String fullname = object.getString("fullname");
                            Commom_Method.printLog("fullname", fullname);
                            String nickname = object.getString("nickname");
                            Commom_Method.printLog("nickname", nickname);

                            String profileimage = object.getString("profileimage");
                            Commom_Method.printLog("profileimage", profileimage);
                            type = object.getString("type");
                            Commom_Method.printLog("type", type);


                            Picasso.with(Account_Setting.this).load(profileimage).placeholder(R.drawable.loadinguni).into(upload);
                            if (profileimage.equals("https://www.unionrides.com/assets/images/noimage.png")) {
                                Picasso.with(Account_Setting.this).load(R.drawable.uploadpic).placeholder(R.drawable.loading).into(upload);
                            }
                            fname.setText(fullname);
                            nick_name.setText(nickname);

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


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Showuserdetails");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Account_Setting.this);
        requestQueue.add(stringRequest);
        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(stringRequest,
//                tag_string_req);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
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
                progress_dialog = new ProgressDialog(Account_Setting.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(Account_Setting.this);
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


        public AsyncLoadData() {


        }

        @Override
        protected Void doInBackground(String... strings) {
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            if (bitmaps == null) {
                // bitmaps = upload.getDrawingCache();

                BitmapDrawable drawable = (BitmapDrawable) upload.getDrawable();
                bitmaps = drawable.getBitmap();
                Log.d("bmp", String.valueOf(bitmaps));
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmaps.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();


            LoadApi api = new LoadApi();
            try {


                JSONObject jobj = api.Action_profileSetting("Profilesetting", full, nick, PrefMangr.getInstance().getUserId(), "AccountType", "Club ", imageBytes);
                Log.d("data", String.valueOf(jobj));
                //jobj = api.Action_profileSetting_noImage(action, user_id, first_name, last_name, email_address, mobile_number, date_of_birth, byte_arr);

                JSONObject object = api.getResult1();
                Log.d("ProfileSetting", ">>" + object);

                msg = object.getString("msg");
                Log.d("Meassage", "  " + msg);
                JSONObject jarray = object.getJSONObject("show");
                String profileimage = jarray.getString("profileimage");
                Log.d("profileimage", profileimage);
                PrefMangr.getInstance().setProfilepic(profileimage);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            try {

                if (msg.equals("Profile updated")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Account_Setting.this);
                    alertDialogBuilder.setMessage("Profile updated");
                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            PrefMangr.getInstance().setUserName(fname.getText().toString());
                            PrefMangr.getInstance().setUsernickName(nick_name.getText().toString());

                            Intent intent = new Intent(Account_Setting.this, Wall.class);
                            startActivity(intent);
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
}
