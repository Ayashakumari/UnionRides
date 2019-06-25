package mx.bigapps.unionrides.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.UserList;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.LoadApi;

/**
 * Created by dev on 02-11-2017.
 */

public class Add_Events extends FragmentActivity implements View.OnClickListener {
    List<String> list;
    List<String> listId;
    Spinner invite_frnd, pub;
    CircleImageView event_upload;
    private static final int SELECT_PICTURE = 1;
    private static final String TAG = "Add_Events";
    static TextView st_dt;
    static TextView ed_dt;
    static int flag_value = 0;
    TextView event_location;
    LinearLayout eventlocation;
    UserList userList;
    ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0;
    private final int HIDE_PROG_DIALOG = 1;
    Bitmap bitmap;

    String[] publ = new String[]{
            "Public",
            "Private",
    };

    String[] Friend = new String[]{
            "Invite Friends",
            "AAAA",
            "BBBB",
            "CCCC",
            "DDDD",};
    String location;
    MultiSelectionSpinner spinner;
    static ArrayList<UserList> photolist;
    GPSTracker gpsTracker;
    Button submit;
    String msg;
    EditText edttitle, description;
    String selectedItemText;

    @Override
    protected void onCreate(Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        setContentView(R.layout.add_events);
        PrefMangr.getInstance().setLocation(null);
        PrefMangr.getInstance().setlatitude(null);
        PrefMangr.getInstance().setlogitude(null);
        PrefMangr.getInstance().setinvitefriendId("");
        gpsTracker = new GPSTracker(this);
        event_location = (TextView) findViewById(R.id.event_location);
        edttitle = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        // description.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        pub = (Spinner) findViewById(R.id.pub);
        spinner = (MultiSelectionSpinner) findViewById(R.id.input1);
        event_upload = (CircleImageView) findViewById(R.id.event_upload);
        event_upload.setOnClickListener(this);
        st_dt = (TextView) findViewById(R.id.start_dt);
        ed_dt = (TextView) findViewById(R.id.end_dt);
        userList();
        Intent intent = getIntent();
        location = PrefMangr.getInstance().getLocation();
        eventlocation = (LinearLayout) findViewById(R.id.eventlocation);
        submit = (Button) findViewById(R.id.event_sumit);
        event_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapView_Activity.class));
            }
        });
        if (location != null) {
            event_location.setText(location);
        }
        edttitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 31) {
                    Toast.makeText(Add_Events.this, "Title can't be more than 35 charaters", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 119) {
                    Toast.makeText(Add_Events.this, "Description can't be more than 120 charaters", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

      /*  spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String friendids = null;
                if(photolist!=null){
                String friendid = photolist.get(position).getFullname().toString();
                if(friendid!=null ) {
                    friendids = friendids + "," + friendid;
                    Log.d("friendids", friendids);
                }
                else {
                    friendids = "";
                }}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edttitle.getText().toString();
                String startdate = st_dt.getText().toString();
                String starttime = st_dt.getText().toString();
                String enddate = ed_dt.getText().toString();
                String endtime = ed_dt.getText().toString();
                String eventlocation = event_location.getText().toString();
                String profileType = selectedItemText;
                Log.d("profileType", profileType);
                String descriptions = description.getText().toString();
                String[] startdatetime = startdate.split("\\s+");
                String sdate = startdatetime[0];
                String stime = startdatetime[1] + " " + startdatetime[2];
                String[] enddatetime = enddate.split("\\s+");
                String edate = enddatetime[0];
                String etime = enddatetime[1] + " " + enddatetime[2];
                Log.d("datetime", sdate + "vc" + stime + "vfxv" + edate + "vb " + etime);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(Add_Events.this.getResources(),
                            R.drawable.header_logo);
                }
                if (TextUtils.isEmpty(title)) {

                    Toast.makeText(Add_Events.this, "Enter Event Title", Toast.LENGTH_LONG).show();
                    edttitle.requestFocus();
                    return;

                } else if (TextUtils.isEmpty(startdate)) {
                    //   nick_name.setError("Enter nick Name");
                    Toast.makeText(Add_Events.this, "Enter Start Date", Toast.LENGTH_LONG).show();
                    st_dt.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(enddate)) {
                    //email.setError("Invalid Email");
                    Toast.makeText(Add_Events.this, " Enter valid EndDate", Toast.LENGTH_LONG).show();
                    ed_dt.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(eventlocation)) {
                    Toast.makeText(Add_Events.this, " Please Enter Event Location", Toast.LENGTH_LONG).show();

                    event_location.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(descriptions)) {
                    // repass.setError("Please Enter Same Password");
                    Toast.makeText(Add_Events.this, " Please Enter decription of events", Toast.LENGTH_LONG).show();
                    description.requestFocus();
                    return;
                } else if (profileType == null) {
                    Toast.makeText(Add_Events.this, " Please select profile Type", Toast.LENGTH_LONG).show();
                    return;
                } else if (profileType.equals("1") & PrefMangr.getInstance().getinvitefriendId().equals("")) {
                    Toast.makeText(Add_Events.this, " Please Invite Friends", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    loadData(title, sdate, stime, edate, etime, eventlocation, Double.parseDouble(PrefMangr.getInstance().getlatitude()), Double.parseDouble(PrefMangr.getInstance().getlogitude()), profileType, descriptions);

                }

            }
        });
        ImageView l = (ImageView) findViewById(R.id.event_back);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Add_Events.this, EventsMain.class));
                finish();
            }
        });
        ArrayAdapter<String> ar = new ArrayAdapter<String>(this, R.layout.spinner_text_view, R.id.textView1, publ);
        ar.setDropDownViewResource(R.layout.spinner_text_view);
        pub.setAdapter(ar);
        pub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedItemText = String.valueOf(position);
                // Notify the selected item text

                //   invite_frnd.setEnabled(true);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // invite_frnd.setClickable(false);
            }

        });


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String dateString = sdf.format(new Date());
        st_dt.setText(dateString);

        st_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                flag_value = 0;
                showTruitonTimePickerDialog(v);
                showTruitonDatePickerDialog(v);


            }
        });


        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String dateS = sd.format(new Date());
        ed_dt.setText(dateS);

        ed_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_value = 1;
                showTruitonTimePickerDialog(v);
                showTruitonDatePickerDialog(v);
            }
        });


    }

    private AsyncLoadData asyncLoad;

    private void loadData(String title, String startdate, String starttime, String enddate, String endtime, String eventlocation, double latitude, double longitude, String profileType, String descriptions) {
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData(title, startdate, starttime, enddate, endtime, eventlocation, latitude, longitude, profileType, descriptions);
            asyncLoad.execute();
        }
    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    private void userList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        photolist = new ArrayList();
                        list = new ArrayList<String>();
                        listId = new ArrayList<>();
                        JSONObject obj = null;
                        Log.d("Showuserlist", response.toString());
                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jsonArray = obj.getJSONArray("users");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jarray = jsonArray.getJSONObject(i);
                                String user_id = jarray.getString("user_id");
                                String fullname = jarray.getString("fullname");
                                String nickname = jarray.getString("nickname");
                                //  String email = jarray.getString("email");
                                String email = "";
                                // String password = jarray.getString("password");

                                String profileimage = jarray.getString("profileimage");
                                //    String friend_status = jarray.getString("friend_status");
                                String type = jarray.getString("type");
                                //  String verify_code = jarray.getString("verify_code");
                                String verify_code = "";

                                Log.d("name", fullname);
                                userList = new UserList(user_id, fullname, nickname, email, verify_code, type, profileimage);
                                photolist.add(userList);
                                list.add(fullname);
                                listId.add(user_id);


                            }

                            spinner.setItems(list, listId);
                        } catch (JSONException e1) {

                            e1.printStackTrace();
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

                params.put("action", "Myfriendlist");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                Log.d("Showuserlist", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Add_Events.this);
        requestQueue.add(stringRequest);

    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            Date startDate = null;
            if (flag_value == 1) {
                String startDateString = st_dt.getText().toString();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

                try {
                    startDate = df.parse(startDateString);
                    String newDateString = df.format(startDate);
                    System.out.println(newDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.setTime(startDate);


            } else {

                c.add(Calendar.DATE, 0);
            }
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog d = new DatePickerDialog(getActivity(), this, year, month, day);
            DatePicker dp = d.getDatePicker();
            dp.setMinDate(c.getTimeInMillis());

            return d;
            // Create a new instance of DatePickerDialog and return it
            //  return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            if (flag_value == 0) {
                st_dt.setText(year + "-" + (month + 1) + "-" + day);
            } else {
                ed_dt.setText(year + "-" + (month + 1) + "-" + day);
            }
        }
    }


    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, false);
//
        }


        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Time t = new Time(hourOfDay, minute, 0);
            Format formatter;
            formatter = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            String time = formatter.format(t);
            if (flag_value == 0) {
                st_dt.setText(st_dt.getText() + "  " + time);
            } else {
                ed_dt.setText(ed_dt.getText() + " " + time);
            }

           /* if (hourOfDay >= 0 && hourOfDay < 10) {
                if (flag_value == 0) {
                    st_dt.setText(st_dt.getText() + "  " + hourOfDay + ":" + minute + " AM");
                } else {
                    ed_dt.setText(ed_dt.getText() + " " + "0" + hourOfDay + ":" + minute + " AM");
                }
            } else if (hourOfDay >= 0 && hourOfDay < 10 && minute < 10) {
                if (flag_value == 0) {
                    st_dt.setText(st_dt.getText() + "  " + hourOfDay + ":" + "0"+ minute + " AM");
                } else {
                    ed_dt.setText(ed_dt.getText() + " " + "0" + hourOfDay + ":" + "0" + minute + " AM");
                }
            } else if (hourOfDay >= 0 && hourOfDay < 12) {
                if (flag_value == 0) {
                    st_dt.setText(st_dt.getText() + "  " + hourOfDay + ":" + minute + " AM");
                } else {
                    ed_dt.setText(ed_dt.getText() + "  " + hourOfDay + ":" + minute + " AM");
                }
            } else if (hourOfDay == 12) {
                if (flag_value == 0) {
                    st_dt.setText(st_dt.getText() + " " + hourOfDay + ":" + minute + " PM");
                } else {
                    ed_dt.setText(ed_dt.getText() + "  " + hourOfDay + ":" + minute + " PM");
                }
            } else if (hourOfDay < 22) {
                hourOfDay = hourOfDay - 12;
                if (flag_value == 0) {
                    st_dt.setText(st_dt.getText() + " " + "0" + hourOfDay + ":" + minute + " PM");
                } else {
                    ed_dt.setText(ed_dt.getText() + " " + "0" + hourOfDay + ":" + minute + " PM");
                }
            }
            else if (hourOfDay < 22&&minute<10) {
                hourOfDay = hourOfDay - 12;
                if (flag_value == 0) {
                    st_dt.setText(st_dt.getText() + " " + "0" + hourOfDay + ":" + "0"+minute + " PM");
                } else {
                    ed_dt.setText(ed_dt.getText() + " " + "0" + hourOfDay + ":" +"0" +minute + " PM");
                }
            }else {

                hourOfDay = hourOfDay - 12;

                if (flag_value == 0) {
                    st_dt.setText(st_dt.getText() + " " + hourOfDay + ":" + minute + " PM");
                } else {
                    ed_dt.setText(ed_dt.getText() + " " + hourOfDay + ":" + minute + " PM");
                }

            }*/
        }
    }


    /* Choose an image from Gallery */
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    bitmap = rotateImageIfRequired(Add_Events.this, bitmap, imageUri);
                    event_upload.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    private void showProgDialog() {
        // TODO Auto-generated method stub
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(Add_Events.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(Add_Events.this);
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

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onClick(View v) {
        openImageChooser();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Add_Events.this, EventsMain.class));
        finish();
    }

    private class AsyncLoadData extends AsyncTask<String, Void, Void> {
        boolean flag = false;
        String title, starttime, startdate, endtime, enddate, eventlocation, latitude, longitude, profiletype, description;

        public AsyncLoadData(String title, String startdate, String starttime, String enddate, String endtime, String eventlocation, double latitude, double longitude, String profileType, String descriptions) {
            this.title = title;
            this.starttime = starttime;
            this.startdate = startdate;
            this.endtime = endtime;
            this.enddate = enddate;
            this.eventlocation = eventlocation;
            this.latitude = String.valueOf(latitude);
            this.longitude = String.valueOf(longitude);
            this.profiletype = profileType;
            this.description = descriptions;


        }

        @Override
        protected Void doInBackground(String... strings) {
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            if (bitmap == null) {
                // bitmaps = upload.getDrawingCache();

                BitmapDrawable drawable = (BitmapDrawable) event_upload.getDrawable();
                bitmap = drawable.getBitmap();
                Log.d("bmp", String.valueOf(bitmap));
            }
          //  bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();
            LoadApi api = new LoadApi();
            try {

                JSONObject jobj = api.Action_AddEvent("Addevent", PrefMangr.getInstance().getUserId(), title, starttime, startdate, endtime, enddate, eventlocation, latitude, longitude, profiletype, description, PrefMangr.getInstance().getinvitefriendId(), imageBytes);

                //  jobj = api.Action_profileSetting_noImage(action, user_id, first_name, last_name, email_address, mobile_number, date_of_birth, byte_arr);

                JSONObject object = api.getResult1();
                Log.d("Profile_Setting", ">>" + object);
                msg = object.getString("msg");
                Log.d("Meassage", "  " + msg);

                JSONObject jsonObject = object.getJSONObject("response");
                Log.d("Responce_object", ">>>" + jsonObject);
                String user_id = jsonObject.getString("user_id");
                Log.d("User_id", "  " + user_id);
                String Facebookid = jsonObject.getString("facebook_id");
                String usrname = jsonObject.getString("name");
                Log.d("Last_name", "  " + usrname);
                String email_address = jsonObject.getString("email");
                Log.d("Email_Address", "  " + email_address);
                String password = jsonObject.getString("password");
                Log.d("Password", "  " + password);
                String mobile_number = jsonObject.getString("mobile_number");
                String calling_code = jsonObject.getString("calling_code");
                String device_id = jsonObject.getString("device_id");
                String gender = jsonObject.getString("gender");
                String login_status = jsonObject.getString("login_status");
                String profile_pic = jsonObject.getString("profile_pic");
                Log.d("profile_pic", profile_pic);
                String password_code = jsonObject.getString("password_code");
                Log.d("Mobile_number", mobile_number);
                String latitude = jsonObject.getString("latitude");
                String longitude = jsonObject.getString("longitude");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            //   hideProgDialog();
            PrefMangr.getInstance().setinvitefriendId("");
            try {

                if (msg.equals("Event posted successsfully")) {

                  /*  AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(PersonalInfo.this, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(PersonalInfo.this);
                    }
                    alert.setTitle("Successful");
                    alert.setMessage("Profile updated Successfully");


                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

*/
                    Intent intent = new Intent(Add_Events.this, EventsMain.class);
                    intent.putExtra("event","3");
                    startActivity(intent);
                           /* dialog.dismiss();
                        }
                    });

                    try {
                        Dialog dialog = alert.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
*/
                } else {
                    AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(Add_Events.this, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(Add_Events.this);
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
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        event_location.setText(PrefMangr.getInstance().getLocation());
    }
}

