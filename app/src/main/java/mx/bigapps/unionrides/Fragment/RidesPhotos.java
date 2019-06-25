package mx.bigapps.unionrides.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Time;
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
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import mx.bigapps.unionrides.Activity.Crop2;
import mx.bigapps.unionrides.Activity.Rides;
import mx.bigapps.unionrides.Adapter.RiderPhotos_Adapter;
import mx.bigapps.unionrides.Adapter.onClickShowPic;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.BuildConfig;
import mx.bigapps.unionrides.Model.Added_Image_Entity;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.LoadApi;
import mx.bigapps.unionrides.utils.helper;


public class RidesPhotos extends Fragment implements onClickShowPic {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Intent intentv;
    String cover_id, cover_pic;
    ArrayList photolist;
    GridViewWithHeaderAndFooter photo_recycle;
    RecyclerView.LayoutManager layoutManager;
    RiderPhotos_Adapter photo_adapter;
    public static ImageView images_data;

    Button addphotos;
    Button addphoto;
    TextView error;
    ImageView events;
    ImageView header_logo;
    private static int LOAD_IMAGE_RESULTS = 1000;
    ArrayList<Bitmap> photobitmapList;
    static Context context;
    Bitmap bitmap;
    byte[] image_aaray;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String msg;
    String pic_url;
    RelativeLayout videolayout, container_video;
    private Uri mHighQualityImageUri = null;
    ArrayList<Added_Image_Entity> image_list = new ArrayList<>();
    private static final int REQUEST_TAKE_PHOTO = 1;

    String mCurrentPhotoPath;

    public RidesPhotos() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_rides_photos, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        intentv = getActivity().getIntent();
        cover_id = PrefMangr.getInstance().getCoverPicID();
        cover_pic = intentv.getStringExtra("cover_pic");
        Log.d("COVER_ID", "<<>>" + cover_id);
        Log.d("COVER_Pic", "<<>>" + cover_pic);
        error = (TextView) view.findViewById(R.id.error);
        videolayout = (RelativeLayout) view.findViewById(R.id.addvideolayout);
        addphoto = (Button) view.findViewById(R.id.addphoto);

//        events = (ImageView) view.findViewById(R.id.header_event);

//        header_logo = (ImageView) view.findViewById(R.id.header_logo);
//        header_logo.setImageResource(R.drawable.header_logo);
//        header_logo.setVisibility(View.VISIBLE);

//        events.setImageResource(R.drawable.header_add);
//        events.setVisibility(View.VISIBLE);

        photo_recycle = (GridViewWithHeaderAndFooter) view.findViewById(R.id.photolist);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.addphotofooter, null, false);
        photo_recycle.addFooterView(footerView);
        addphotos = (Button) footerView.findViewById(R.id.addphotos);
        photobitmapList = new ArrayList<>();

        if (helper.status.equals("otherprofile_ride")) {
            addphotos.setVisibility(View.GONE);
            addphoto.setVisibility(View.GONE);
        }

        Rides.events.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (helper.riderStatus.equals("0")) {
                    final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_custom);

                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();
                    RelativeLayout takepicture = (RelativeLayout) dialog.findViewById(R.id.gallery_rl);
                    RelativeLayout uploadfile = (RelativeLayout) dialog.findViewById(R.id.camera_rl);
                    RelativeLayout cancel_rl = (RelativeLayout) dialog.findViewById(R.id.cancel_rl);
                    cancel_rl.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });
                    takepicture.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                       /* ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 7);
                        dialog.dismiss();*/
                            startCamera();
                            dialog.dismiss();

                        }
                    });
                    uploadfile.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                        /*Intent i = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 0);*/
                            // Create intent to Open Image applications like Gallery, Google Photos
                       /* Intent intent = new Intent();
                        intent.setType("image*//*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_RESULTS);
                        dialog.dismiss();*/
                            Crop2.pickImage(getActivity(), RidesPhotos.this);
                            dialog.dismiss();

                        }
                    });
                    if (!TextUtils.isEmpty(pic_url)) {

                    }
                }
            }
        });
        addphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_custom);

                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
                RelativeLayout takepicture = (RelativeLayout) dialog.findViewById(R.id.gallery_rl);
                RelativeLayout uploadfile = (RelativeLayout) dialog.findViewById(R.id.camera_rl);
                RelativeLayout cancel_rl = (RelativeLayout) dialog.findViewById(R.id.cancel_rl);
                cancel_rl.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                takepicture.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                       /* ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 7);
                        dialog.dismiss();*/
                        startCamera();
                        dialog.dismiss();

                    }
                });
                uploadfile.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_RESULTS);

                        // Crop2.pickImage(getActivity(), RidesPhotos.this);
                        dialog.dismiss();

                    }
                });
                if (!TextUtils.isEmpty(pic_url)) {

                }

            }
        });
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_custom);

                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
                RelativeLayout takepicture = (RelativeLayout) dialog.findViewById(R.id.gallery_rl);
                RelativeLayout uploadfile = (RelativeLayout) dialog.findViewById(R.id.camera_rl);
                RelativeLayout cancel_rl = (RelativeLayout) dialog.findViewById(R.id.cancel_rl);
                cancel_rl.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                takepicture.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                       /* ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 7);
                        dialog.dismiss();*/
                        startCamera();
                        dialog.dismiss();

                    }
                });
                uploadfile.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_RESULTS);

                        dialog.dismiss();

                    }
                });


            }
        });
        return view;
    }

    void startCamera() {
        try {
            dispatchTakePictureIntent();
        } catch (IOException e) {
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Crop2.REQUEST_PICK && resultCode == getActivity().RESULT_OK) {
            beginCrop(data.getData());

        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop2(resultCode, data);


        }
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            File file = new File(imageUri.getPath());
            try {
                InputStream ims = new FileInputStream(file);
                // ivPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
                bitmap = BitmapFactory.decodeStream(ims);

                try {
                    bitmap = rotateImageIfRequired(getActivity(), bitmap, imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                image_aaray = baos.toByteArray();
                loadData();
            } catch (FileNotFoundException e) {
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
     /*   if (requestCode == 7 && resultCode == -1) {
            bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            image_aaray = baos.toByteArray();
            loadData();


        }*/
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == -1 && data != null) {

            Uri selectedImageUri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                bitmap = rotateImageIfRequired(getActivity(), bitmap, selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            image_aaray = baos.toByteArray();
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bao);
                // bitmap = getResizedBitmap(bitmap, 400, 400);
            }
            image_aaray = bao.toByteArray();
            loadData();


            // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (null != selectedImageUri) {
                // Get the path from the Uri
                String path = getPathFromURI(selectedImageUri);

                // Set the image in ImageView
                //    bitmap = getBitmapFromURL(path);
                //images.setImageBitmap(bitmap);
                // upload_img.setImageURI(selectedImageUri);

            }
        }

    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(getActivity(), RidesPhotos.this);
    }

    private void handleCrop2(int resultCode, Intent result) {
        if (resultCode == getActivity().RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Crop.getOutput(result));

            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, baos);
            image_aaray = baos.toByteArray();
            loadData();

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

    private File getPhotoDirectory() {
        File outputDir = null;
        String externalStorageStagte = Environment.getExternalStorageState();
        if (externalStorageStagte.equals(Environment.MEDIA_MOUNTED)) {
            File photoDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            outputDir = new File(photoDir, getString(R.string.app_name));
            if (!outputDir.exists())
                if (!outputDir.mkdirs()) {
                    Toast.makeText(
                            getActivity(),
                            "Failed to create directory "
                                    + outputDir.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    outputDir = null;
                }
        }
        return outputDir;
    }

    private Uri generateTimeStampPhotoFileUri() {

        Uri photoFileUri = null;
        File outputDir = getPhotoDirectory();
        if (outputDir != null) {
            Time t = new Time();
            t.setToNow();
            File photoFile = new File(outputDir, System.currentTimeMillis()
                    + ".jpg");
            photoFileUri = Uri.fromFile(photoFile);
        }
        return photoFileUri;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
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
                progress_dialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(getActivity());
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


    //asynchronous task
    class AsyncLoadData extends AsyncTask<String, Void, Void> {
        boolean flag = false;

        @Override
        protected Void doInBackground(String... strings) {
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);


//            byte[] imageBytes = baos.toByteArray();

            LoadApi api = new LoadApi();

            try {
                if (image_aaray != null) {
                    Log.d("action", "" + "Addrideimages");
                    Log.d("user_id", "" + PrefMangr.getInstance().getUserId());
                    Log.d("Picture", "" + image_aaray);
                    Log.d("COVER_ID", cover_id);

                    api.Action_upload_Image("Addrideimages", PrefMangr.getInstance().getUserId(), cover_id, image_aaray);

                } else {
                    Toast.makeText(getActivity(), "Please Select Image", Toast.LENGTH_SHORT).show();
                }


                JSONObject object = api.getResult1();
                Log.d("Profile_Setting", ">>" + object);
                msg = object.getString("msg");
                Log.d("Meassage", "  " + msg);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            if (msg.equals("Image Add Successfully")) {

                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(getActivity());
                }
                alert.setTitle("Successfull");
                alert.setMessage("Image Added Successfully");


                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        helper.riderfrag = "0";
                        Intent intent = new Intent(getActivity(), Rides.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("cover_id", cover_id);
                        intent.putExtra("flag", 0);
                        intent.putExtra("cover_id", PrefMangr.getInstance().getCoverPicID());
                        startActivity(intent);
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
            } else {
                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(getActivity());
                }
                alert.setTitle("Alert!");
                alert.setMessage("Some thing Went Wrong");


                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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

        }
    }


    private void makeStringReq() {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = "loading...";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        JSONObject obj = null;
                        String imagethumb, imagethumb320;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                Log.d("Json Data", response.toString().substring(start1, end));
                            }
                            image_list.clear();
///                            msg=obj.getString("msg");
                            JSONArray jsonArray = obj.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Added_Image_Entity photo_entity = new Added_Image_Entity();
                                photo_entity.setCover_photo_id(object.getString("cover_photo_id"));
                                photo_entity.setFile_type(object.getString("file_type"));
                                photo_entity.setFile_url(object.getString("file_url"));
                                photo_entity.setUser_id(object.getString("user_id"));
                                photo_entity.setId(object.getString("id"));
                                photo_entity.setDatetime(object.getString("datetime"));
                                if (helper.status.equals("otherprofile_ride")) {
                                    photo_entity.setProfile_status("1");

                                } else {
                                    photo_entity.setProfile_status("0");
                                }
                                JSONArray image_thumb_size = object.getJSONArray("image_thumb_size");
                                if (image_thumb_size.length() != 0) {
                                    JSONObject jarrayimage = image_thumb_size.getJSONObject(0);
                                    imagethumb = jarrayimage.getString("640_640");
                                    imagethumb320 = jarrayimage.getString("320_320");
                                } else {
                                    imagethumb = null;
                                    imagethumb320 = null;
                                }
                                // photo_entity.setFile_url(imagethumb);
                                photo_entity.setImage320(imagethumb320);
                                image_list.add(photo_entity);
                            }
                            if (image_list.size() > 0) {
                                photo_adapter = new RiderPhotos_Adapter(getContext(), image_list);
                                photo_recycle.setAdapter(photo_adapter);
                                try {
                                    URL url = new URL(image_list.get(0).getFile_url());
                                    Log.d("imagehight", String.valueOf(url));

                                    HttpURLConnection connection = (HttpURLConnection) url
                                            .openConnection();

                                    connection.setDoInput(true);
                                    connection.connect();

                                    InputStream input = connection.getInputStream();
                                    bitmap = BitmapFactory.decodeStream(input);
                                    Log.d("imagehight", String.valueOf(bitmap));

                                } catch (IOException e) {
                                    e.printStackTrace();

                                }


                            } else {
                                photo_recycle.setVisibility(View.GONE);
                                if (helper.status.equals("otherprofile_ride")) {
                                    addphotos.setVisibility(View.GONE);
                                    addphoto.setVisibility(View.GONE);
                                } else {
                                    addphoto.setVisibility(View.VISIBLE);
                                }
                                error.setVisibility(View.VISIBLE);
                                error.setTextColor(getResources().getColor(R.color.Black));

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
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Time Out Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getActivity(), "Authentication Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {
//							Toast.makeText(StringRequestActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
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

                params.put("action", "Show_ride_image_video");
                if (!helper.status.equals("otherprofile_ride")) {
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                } else {
                    params.put("user_id", helper.user_id);
                }
                params.put("cover_photo_id", cover_id);
                params.put("type", "image");

                //   params.put("cover_image",image_aaray);


                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(stringRequest,
//                tag_string_req);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("URL", "====" + pic_url);
        makeStringReq();
    }

    @Override
    public void showPicUrl(String url) {
        Log.d("URL", "+++" + url);
        pic_url = url;
        //images_data= (ImageView) view.findViewById(R.id.source_pic);
        // UrlImageViewHelper.setUrlDrawable(images_data,pic_url,R.drawable.default_user);


      /*  Picasso.with(getActivity())
                .load(pic_url)
                .error(R.drawable.background2)
                .placeholder(R.drawable.loading)

                .into(images_data, new Callback() {
                    @Override
                    public void onSuccess() {
                        //progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });*/
//        Picasso.with(getContext())
//                .load(pic_url)
//                .error(R.drawable.background2)
//                .placeholder(R.drawable.loading)
//
//                .into(images_data);
    }

}




