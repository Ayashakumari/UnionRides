package mx.bigapps.unionrides.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.bigapps.unionrides.Adapter.Car_Model_Adapter;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.BuildConfig;
import mx.bigapps.unionrides.Fragment.SpecRides;
import mx.bigapps.unionrides.Model.Car_Brand_Entity;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.LoadApi;
import mx.bigapps.unionrides.utils.helper;

/**
 * Created by dev on 02-11-2017.
 */

public class Add_specification extends AppCompatActivity implements View.OnClickListener {
    CircleImageView spec_image;
    TextView model, brand;
    String path_img = "";
    Button specification_sumit;
    EditText touque, color, hp, speed_txt;
    static TextView year;

    Car_Brand_Entity entity1;
    Car_Model_Adapter adapter;
    ArrayList<String> Select = new ArrayList<>();
    int mYear, mMonth, mDay;
    Uri imageUri;
    ProgressDialog dlg = null;
    Bitmap myBitmap, myBitma, rotatedBitmap;
    byte[] byte_arr = null;

    ArrayList<Car_Brand_Entity> brand_list = new ArrayList<>();
    ArrayList<Car_Brand_Entity> model_list = new ArrayList<>();
    private static final int SELECT_PICTURE = 1;
    private static final String TAG = "Add_Specifiction";
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    double diff;
    Context context;
    String msg;
    public static final int RequestPermissionCode = 1;
    int flagValue = 0;
    String brand_name = "", model_name = "";

    String year_str = "", hp_str = "", tourque_str, color_str, speed_str;
    int value = 0;
    Intent intent;
    TextView header_txt;

    String mCurrentPhotoPath;
    Bitmap bitmap;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static int LOAD_IMAGE_RESULTS = 1000;
    EditText price_txt;
    String specPrice;

    @Override
    protected void onCreate(Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        setContentView(R.layout.add_specifications);
        context = Add_specification.this;
//        Select.add("Select Car Brand");
        Car_Brand_Entity entity_new = new Car_Brand_Entity();
        entity_new.setBrand_name("Select Brand Name");
        entity_new.setModel("Select Car Model");
        entity_new.setId("0");
        brand_list.add(entity_new);
        header_txt = (TextView) findViewById(R.id.header_txt);
        intent = getIntent();
        value = intent.getIntExtra("flag", 0);
        Log.d("Value", "<<>>" + value);
        specification_sumit = (Button) findViewById(R.id.specification_sumit);
        ImageView l = (ImageView) findViewById(R.id.specification_back);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        speed_txt = (EditText) findViewById(R.id.speed_txt);
        brand = (TextView) findViewById(R.id.brand);
        model = (TextView) findViewById(R.id.model);
        price_txt = (EditText) findViewById(R.id.price_txt);
        touque = (EditText) findViewById(R.id.touque);
        color = (EditText) findViewById(R.id.color);
        hp = (EditText) findViewById(R.id.hp);
        year = (TextView) findViewById(R.id.year);
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                date();

            }
        });
        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_specification.this, Select_Car_Brand.class);
                intent.putExtra("flag", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brand_name = brand.getText().toString();
                if (!TextUtils.isEmpty(brand_name) && brand_name.equals("Select Brand")) {
                    Toast.makeText(Add_specification.this, "Please Select Brand name", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Add_specification.this, Select_Car_Brand.class);
                    intent.putExtra("flag", 2);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
        specification_sumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed_str = speed_txt.getText().toString();
                hp_str = hp.getText().toString();
                tourque_str = touque.getText().toString();
                year_str = year.getText().toString();
                color_str = color.getText().toString();
                specPrice = price_txt.getText().toString();
                if (value == 0) {
                    if (bitmap == null) {
                        bitmap = BitmapFactory.decodeResource(Add_specification.this.getResources(),
                                R.drawable.header_logo);
                    }
                }
                if (brand_name.equals("Select Brand Name")) {
                    Toast.makeText(Add_specification.this, "Please Select Brand Name", Toast.LENGTH_SHORT).show();
                } else if (model_name.equals("Select Car Model")) {
                    Toast.makeText(Add_specification.this, "Please Select Car Model", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(color_str)) {
                    Toast.makeText(Add_specification.this, "Please Enter Car Color", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(hp_str)) {
                    Toast.makeText(Add_specification.this, "Please Enter Car Horse Power", Toast.LENGTH_SHORT).show();
                } else if (hp_str.equals("Year")) {
                    Toast.makeText(Add_specification.this, "Please Select Year", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(tourque_str)) {
                    Toast.makeText(Add_specification.this, "Please Enter Car Tourque", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(speed_str)) {
                    Toast.makeText(Add_specification.this, "Please Enter Car Speed", Toast.LENGTH_SHORT).show();
                } else {
                    flagValue = 3;
                    loadData();
                }
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text_view, R.id.textView1, Select);

        EnableRuntimePermission();
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text_view);


        spec_image = (CircleImageView) findViewById(R.id.upload_image);
        if (!TextUtils.isEmpty(helper.id)) {
            brand_name = helper.brand_name;
            model_name = helper.model_name;
            brand.setText(helper.brand_name);
            model.setText(helper.model_name);
            year.setText(helper.year);
            hp.setText(helper.hp);
            // helper.name_brand=brand_name;
            color.setText(helper.color);
            touque.setText(helper.torque);
            speed_txt.setText(helper.speed);
            price_txt.setText("$ " + helper.ridePrice);
            header_txt.setText("Edit Specification");
            specification_sumit.setText("Edit Specification");
            if (helper.pic_url != null) {
                Picasso.with(Add_specification.this).load(helper.pic_url).placeholder(R.drawable.loading).into(spec_image);
            } else {
                Picasso.with(Add_specification.this).load(R.drawable.loadinguni).placeholder(R.drawable.loading).into(spec_image);

            }
        }
        spec_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(Add_specification.this, R.style.DialogSlideAnim);
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

                        // Crop.pickImage(Add_specification.this);
                        dialog.dismiss();

                    }
                });
            }
        });
        // m.setOnClickListener(this);
        flagValue = 1;
//makeStringReq();
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
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
                Uri photoURI = FileProvider.getUriForFile(Add_specification.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            File file = new File(imageUri.getPath());
           /* try {
                InputStream ims = new FileInputStream(file);
                // ivPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
                bitmap = BitmapFactory.decodeStream(ims);

                try {

                    bitmap = rotateImageIfRequired(Add_specification.this, bitmap, imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                spec_image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
*/
            MediaScannerConnection.scanFile(Add_specification.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == -1 && data != null) {

            Uri selectedImageUri = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                bitmap = rotateImageIfRequired(Add_specification.this, bitmap, selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            spec_image.setImageBitmap(bitmap);


            // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (null != selectedImageUri) {
                // Get the path from the Uri
                String path = getPathFromURI(selectedImageUri);

                // Set the image in ImageView

                // bitmap = getBitmapFromURL(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

            }
        }

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
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

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }

            spec_image.setImageBitmap(bitmap);


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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

    class AsyncReceiverTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (Build.VERSION.SDK_INT >= 11) {
                dlg = new ProgressDialog(Add_specification.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                dlg = new ProgressDialog(Add_specification.this);
            }
            dlg.setMessage("loading..");
            dlg.show();
        }

        @Override
        protected String doInBackground(String... params) {


            if (myBitmap != null) {
                myBitmap.recycle();
                myBitmap = null;
            }
            if (myBitma != null) {
                myBitma.recycle();
                myBitma = null;
            }

            try {

                BitmapFactory.Options bfOptions = new BitmapFactory.Options();
                bfOptions.inJustDecodeBounds = false;
                bfOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                bfOptions.inDither = true;

                File file = new File(path_img);


                FileInputStream fs = null;
                if (file.exists()) {


                    try {
                        fs = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Log.e("Camera", "Image Picker PATH::::" + path_img);
                    try {
                        if (fs != null) {
                            myBitma = BitmapFactory.decodeFile(file.getAbsolutePath(), bfOptions);
                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            if (myBitma != null) {
                                myBitma.compress(Bitmap.CompressFormat.JPEG, 80, bao);
                                myBitmap = getResizedBitmap(myBitma, 400, 400);
                            }
                            byte_arr = bao.toByteArray();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        myBitma.recycle();
                        myBitma = null;

                    } finally {
                        if (fs != null) {
                            try {
                                fs.close();
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }

                    Log.d("Bitmap", "Actual bitmap item::Width:" + myBitmap.getWidth()
                            + "   Height:" + myBitmap.getHeight());


                    Log.d("setting", "img on bitmap from camera");
                    rotatedBitmap = gettingRotatedBitmap(myBitmap, path_img);

                    Log.d("Bitmap", "Rotaated item::Width:" + rotatedBitmap.getWidth()
                            + "   Height:" + rotatedBitmap.getHeight());

                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bao);
                    Log.d("inside", "camera");

                    Log.d("11111111111111111", "1111111111111");
//                imgresume = path_img;

//                    if (flag_value==0)
//                    {
//                        picture0=bao.toByteArray();
//                        Images_array.add(picture0);
//                        Log.e("picture0","===="+Images_array.toString());
//
//                    }
//
//                    else if (flag_value==1)
//                    {
//                        picture1=bao.toByteArray();
//                        Images_array.add(picture1);
//                        Log.e("picture1","===="+Images_array.toString());
//                    }
//                    else if (flag_value==3)
//                    {
//                        shop_img=bao.toByteArray();
////                        Images_array.add(picture1);
////                        Log.e("picture1","===="+Images_array.toString());
//                    }
//                    else
//                    {
//                        picture2=bao.toByteArray();
//                        Images_array.add(picture2);
//                        Log.e("picture2","===="+Images_array.toString());
//                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (dlg.isShowing() && dlg != null) {
                dlg.dismiss();
            }
            try {

                Log.d("load", "photo upload:" + rotatedBitmap.getHeight() + "   width:" + rotatedBitmap.getWidth());
                spec_image.setImageBitmap(rotatedBitmap);
                //myprofile_img_photo.setImageBitmap(rotatedBitmap);
//                if(flag_value==0) {
//                    photo_image1_add.setImageBitmap(rotatedBitmap);
//                    delete_imageView1.setVisibility(View.VISIBLE);
//                }
//                else if(flag_value==1) {
//                    photo_image2_add.setImageBitmap(rotatedBitmap);
//                    delete_imageView2.setVisibility(View.VISIBLE);
//                }
//                else if(flag_value==2) {
//                    photo_image3_add.setImageBitmap(rotatedBitmap);
//                    delete_imageView3.setVisibility(View.VISIBLE);
//                }
//                else if(flag_value==3) {
//                    shopicon_img.setImageBitmap(rotatedBitmap);
//                    delete_shopicon.setVisibility(View.VISIBLE);
//                }
                //setcircularimage(rotatedBitmap);
                // helper.company_logo=rotatedBitmap;
                //counter = "photo";

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    protected Bitmap gettingRotatedBitmap(Bitmap bitmap_leftn, String path) {
        Bitmap rotatedBitmap = null;

        int width = bitmap_leftn.getWidth();
        int height = bitmap_leftn.getHeight();


        ExifInterface exif;
        try {
            exif = new ExifInterface(path);
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);


            Log.e("bitmap", "11 width:"
                    + width + "   height:" + height + "   filepath:" + path);

            Log.d("ROTATION", "ORITATION::::" + rotation
                    + "  rotation 90:" + ExifInterface.ORIENTATION_ROTATE_90
                    + "  rotation 180:" + ExifInterface.ORIENTATION_ROTATE_180 +
                    "   rotation 270:" + ExifInterface.ORIENTATION_ROTATE_270);
            //if(width > height){
//
            Log.e("bitmap", "22 width:" + width + "   height:" + height);
            Matrix matrix = new Matrix();

            switch (rotation) {
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;

                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;

                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.e("bitmap", "SWITCH MATRIX:");
                    matrix.setRotate(90);
                    break;

                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    break;
            }
            Log.e("bitmap", "33111 width:" + width + "   height:" + height);

            rotatedBitmap = Bitmap.createBitmap(bitmap_leftn, 0, 0, 400, 400, matrix, true);

            Log.e("bitmap", "33222 width:" + rotatedBitmap.getWidth() + "   height:" + rotatedBitmap.getHeight());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return rotatedBitmap;
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Add_specification.this,
                Manifest.permission.CAMERA)) {

            Toast.makeText(Add_specification.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Add_specification.this, new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }

    private void setcircularimage(Bitmap myBitmap) {
        Bitmap newBItmap = getResizedBitmap(myBitmap, 450, 450);
        Bitmap circleBitmap = Bitmap.createBitmap(newBItmap.getWidth(), newBItmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader(newBItmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(newBItmap.getWidth() / 2, newBItmap.getHeight() / 2, newBItmap.getWidth() / 2, paint);


        Log.d("11111111111111111", "1111111111111");
//                imgresume = path_img;
     /*   photo_image1_add.setImageBitmap(circleBitmap);
        photo_image2_add.setImageBitmap(circleBitmap);
        photo_image3_add.setImageBitmap(circleBitmap);*/


    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        Log.d("SellItemsNew", "GET_RESIZED_BITMAP:");
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }


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

    public void date() {
        Calendar calendar = Calendar.getInstance();


        MonthYearPickerDialog pd = MonthYearPickerDialog.newInstance(calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));

        pd.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

                String formatedDate = "";

                if (selectedYear == 1904) {
                    String currentDateFormat = selectedMonth + "/" + selectedDay;// + "/" + selectedYear;  //"MM/dd/yyyy"
                    year.setText(currentDateFormat);
                } else {
                    String currentDateFormat = "" + selectedYear;  //"MM/dd/yyyy"
                    year.setText(currentDateFormat);
                    // formatedDate = DateTimeOp.oneFormatToAnother(currentDateFormat, Constants.dateFormat0, Constants.dateFormat21);
                }


            }
        });
        pd.show(getFragmentManager(), "MonthYearPickerDialog");
    }

    @Override
    public void onClick(View v) {
        openImageChooser();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Add_specification.this, Rides.class));
        finish();
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
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;

                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                Log.d("Json Data", response.toString().substring(start1, end));
                            }
//                            if (flagValue==1) {
//                                JSONArray jsonArray = obj.getJSONArray("result");
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject object = jsonArray.getJSONObject(i);
//                                    Car_Brand_Entity entity = new Car_Brand_Entity();
//                                    entity.setBrand_name(object.getString("brand_name"));
//                                    entity.setId(object.getString("id"));
//                                    Select.add(object.getString("brand_name"));
//                                    brand_list.add(entity);
//                                }
//                                brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                    @Override
//                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////        TextView textView1= (TextView) findViewById(R.id.textView1);
////        textView1.setText(brand_list.get(i).getBrand_name());
//                                        entity1 = brand_list.get(i);
//                                        Log.d("BRAND_NAME", "+++" + brand_list.get(i).getBrand_name());
//                                        Log.d("BRAND_ID", "+++" + brand_list.get(i).getId());
//
//                                        Log.d("BRAND_NAME2", "+++" + entity1.getBrand_name());
//                                        Log.d("BRAND_ID2", "+++" + entity1.getId());
//                                        brand_name=entity1.getBrand_name();
//                                        if ( entity1.getId().equals("0")){
//                                            Toast.makeText(Add_specification.this,"Please Select Brand Name",Toast.LENGTH_SHORT).show();
//                                        }
//                                        else {
//                                            model_list.clear();
//                                            Car_Brand_Entity entity_new=new Car_Brand_Entity();
//                                            entity_new.setBrand_name("Select Brand Name");
//                                            if (!TextUtils.isEmpty(brand_name)) {
//                                                entity_new.setModel("Select Car Model");
//                                            }
//                                            else {
//                                                entity_new.setModel("First Select Brand Name");
//                                            }
//                                            entity_new.setId("0");
//                                            model_list.add(entity_new);
//                                            flagValue=2;
//                                            makeStringReq();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                    }
//                                });
//
//                                sp_adapter = new Car_Brand_Adapter(Add_specification.this, brand_list);
//                                sp_adapter.notifyDataSetChanged();
//                                brand.setAdapter(sp_adapter);
//                            }
//                            if (flagValue==2){
//                                JSONArray jsonArray = obj.getJSONArray("result");
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject object = jsonArray.getJSONObject(i);
//                                    Car_Brand_Entity entity = new Car_Brand_Entity();
//                                    entity.setBrand_name(object.getString("brand_name"));
//                                    entity.setId(object.getString("id"));
//                                    entity.setModel(object.getString("model"));
//                                  //  Select.add(object.getString("brand_name"));
//                                   model_list.add(entity);
//                                }
//                               model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                    @Override
//                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////        TextView textView1= (TextView) findViewById(R.id.textView1);
////        textView1.setText(brand_list.get(i).getBrand_name());
//                                        entity1 = model_list.get(i);
//                                        Log.d("BRAND_NAME", "+++" + model_list.get(i).getBrand_name());
//                                        Log.d("BRAND_ID", "+++" + model_list.get(i).getId());
//                                        Log.d("BRAND_MODEL","+++"+model_list.get(i).getModel());
//
//                                        Log.d("BRAND_NAME2", "+++" + entity1.getBrand_name());
//                                        Log.d("BRAND_ID2", "+++" + entity1.getId());
//                                        Log.d("BRAND_MODEL22","+++"+entity1.getModel());
//                                     model_name=entity1.getBrand_name();
//
//                                    }
//
//                                    @Override
//                                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                    }
//                                });
//
//                                adapter = new Car_Model_Adapter(Add_specification.this, model_list);
//                                adapter.notifyDataSetChanged();
//                               model.setAdapter(adapter);
//                            }
                            if (flagValue == 3) {
                                if (msg.equals("Specification Add Successfully")) {
                                    AlertDialog.Builder alert;
                                    if (Build.VERSION.SDK_INT >= 11) {
                                        alert = new AlertDialog.Builder(Add_specification.this, AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(Add_specification.this);
                                    }
                                    alert.setTitle("Successfull");
                                    alert.setMessage("Specification Add Successfully");


                                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
                                        dev=preferences2.edit();

                                        dev.putString("email_txt",email_login);
                                        dev.putString("password_txt",password_login);
                                        dev.commit();
*/
                                            Intent intent = new Intent(Add_specification.this, Rides.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                                            intent.putExtra("flag", 3);

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
                                        alert = new AlertDialog.Builder(Add_specification.this, AlertDialog.THEME_HOLO_LIGHT);
                                    } else {
                                        alert = new AlertDialog.Builder(Add_specification.this);
                                    }
                                    alert.setTitle("Alert!");
                                    alert.setMessage("Some thing Went Wrong");


                                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
                                        dev=preferences2.edit();

                                        dev.putString("email_txt",email_login);
                                        dev.putString("password_txt",password_login);
                                        dev.commit();
*/
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
                if (flagValue == 1) {
                    params.put("action", "Carbrandlist");
                } else if (flagValue == 2) {
                    params.put("action", "Carmodellist");
                    params.put("brand_name", brand_name);

                } else if (flagValue == 3) {

                    params.put("action", "Addridespecification");
                    params.put("brand_name", brand_name);
                    params.put("model_name", model_name);
                    params.put("color", color_str);
                    params.put("horsepower", hp_str);
                    params.put("year", year_str);
                    params.put("touque", tourque_str);
                    params.put("mph", speed_str);
                    params.put("user_id", PrefMangr.getInstance().getUserId());

                    params.put("cover_photo_id", PrefMangr.getInstance().getCoverPicID());


                }
                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(stringRequest,
//                tag_string_req);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
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


            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bao);
                bitmap = getResizedBitmap(bitmap, 400, 400);
            }
            byte_arr = bao.toByteArray();
            LoadApi api = new LoadApi();

            try {
                if (byte_arr != null) {


                    if (value == 0) {
                        // api.Action_profileSetting_Image("Postnews", PrefMangr.getInstance().getUserId(), byte_arr);
                        api.Action_SpecificationImage("Addridespecification", PrefMangr.getInstance().getUserId(), PrefMangr.getInstance().getCoverPicID(), brand_name,
                                model_name, color_str, hp_str, year_str, tourque_str, speed_str, byte_arr, specPrice);
                    } else {
                        api.Action_EditSpecificationImage("Editspecification", PrefMangr.getInstance().getUserId(), helper.id, helper.brand_name, helper.model_name, color_str, hp_str
                                , year_str, tourque_str, speed_str, byte_arr, specPrice);
                    }
                } else {
                    if (value == 0) {
                        //  Toast.makeText(getActivity(),"Please Select Image",Toast.LENGTH_SHORT).show();
                        api.Action_SpecificationN0Image("Addridespecification", PrefMangr.getInstance().getUserId(), PrefMangr.getInstance().getCoverPicID(), brand_name,
                                model_name, color_str, hp_str, year_str, tourque_str, speed_str, byte_arr, specPrice);
                    } else {
                        api.Action_EditNOSpecificationImage("Editspecification", PrefMangr.getInstance().getUserId(), helper.id, helper.brand_name, helper.model_name, color_str, hp_str
                                , year_str, tourque_str, speed_str, byte_arr, specPrice);
                    }
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
            if (msg.equals("Specification Add Successfully")) {
                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(Add_specification.this, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(Add_specification.this);
                }
                alert.setTitle("Successfull");
                alert.setMessage("Specification Add Successfully");


                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
                                        dev=preferences2.edit();

                                        dev.putString("email_txt",email_login);
                                        dev.putString("password_txt",password_login);
                                        dev.commit();
*/
                        //  SpecRides.refeshData(Add_specification.this);
                        helper.riderfrag = "2";
                        Intent intent = new Intent(Add_specification.this, Rides.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        intent.putExtra("flag", 2);

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
            } else if (msg.equals("Edited successfully")) {


                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(context);
                }
                alert.setTitle("Successfull");
                alert.setMessage("Edited successfully");


                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
                                        dev=preferences2.edit();

                                        dev.putString("email_txt",email_login);
                                        dev.putString("password_txt",password_login);
                                        dev.commit();
*/
                        SpecRides.refeshData(Add_specification.this);
                      /*  Intent intent = new Intent(Add_specification.this, Rides.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        intent.putExtra("flag", 2);

                        intent.putExtra("cover_id", PrefMangr.getInstance().getCoverPicID());
                        startActivity(intent);*/
                        finish();
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
                    alert = new AlertDialog.Builder(Add_specification.this, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(Add_specification.this);
                }
                alert.setTitle("Alert!");
                alert.setMessage("Some thing Went Wrong");


                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
                                        dev=preferences2.edit();

                                        dev.putString("email_txt",email_login);
                                        dev.putString("password_txt",password_login);
                                        dev.commit();
*/
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

//
//
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(helper.brand_name)) {
            brand.setText(helper.brand_name);
            brand_name = helper.brand_name;
        }
        if (!TextUtils.isEmpty(helper.model_name)) {
            if (brand_name.equals(helper.name_brand)) {
                model.setText(helper.model_name);
                model_name = helper.model_name;
            }


        } else {
            helper.model_name = "";
            model.setText("Select Model");

        }


    }
}
