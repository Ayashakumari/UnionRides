package mx.bigapps.unionrides.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.soundcloud.android.crop.CropRide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

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

import mx.bigapps.unionrides.Adapter.First_rideAdapter;
import mx.bigapps.unionrides.Adapter.Rides_pic_adapter;

import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.BuildConfig;
import mx.bigapps.unionrides.Fragment.MyfirstrideFragment;
import mx.bigapps.unionrides.Fragment.PostsWall;
import mx.bigapps.unionrides.Fragment.RidesPhotos;
import mx.bigapps.unionrides.Model.Cover_Photo_List;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.Commom_Method;
import mx.bigapps.unionrides.utils.LoadApi;
import mx.bigapps.unionrides.utils.helper;

import static android.view.View.*;
import static mx.bigapps.unionrides.Activity.PostPreviewimage.context;
import static mx.bigapps.unionrides.Activity.Wall.fragment;


  public class RidesFragmnts extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String ARG_PARAM1 = "param1";
    private final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    Rides_pic_adapter ridesphoto_adapter;

    ExpandableHeightGridView photolistView;
    RecyclerView rclist;

    ArrayList photolist;
    Button addRides;
    TextView txtrides, msg_select, error;

    TextView first_ride, nextride;

    ImageView image_rider;

    int flag = 0;
    String type;


    ImageView camera, video, events;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String msg;
    ImageView resultView;
    byte[] image_aaray;
    private static int LOAD_IMAGE_RESULTS = 10000;
    RelativeLayout header;
      public static ArrayList<Cover_Photo_List> cover_photo_lists = new ArrayList<>();

      Bitmap bitmap;
    ScrollView scroll_view;
    ImageView header_logo;

      String comment;
    private Uri mHighQualityImageUri = null;
    private final int REQUEST_TAKE_PHOTO = 1;
    String imagethumb320 = "";
    View vone,vtwo;
      JSONObject jobj;

     EditText edt_comment;

    Fragment fragment = null;

      String mCurrentPhotoPath;

    public RidesFragmnts() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RidesFragmnts newInstance(String param1, String param2) {
        RidesFragmnts fragment = new RidesFragmnts();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photos_rides, container, false);
        header = (RelativeLayout) rootView.findViewById(R.id.header);
        photolistView = (ExpandableHeightGridView) rootView.findViewById(R.id.photolist);

        helper.rides_status=2;


        helper.backbutton = "1";
        helper.riderfrag = "";
        resultView = (ImageView) rootView.findViewById(R.id.result_image);
        addRides = (Button) rootView.findViewById(R.id.addRides);

        txtrides = (TextView) rootView.findViewById(R.id.txtrides);
        txtrides.setVisibility(VISIBLE);
        scroll_view = (ScrollView) rootView.findViewById(R.id.scroll_view);
        camera = (ImageView) rootView.findViewById(R.id.header_camera);
        camera.setImageResource(R.drawable.header_ride);


        edt_comment = (EditText)rootView.findViewById(R.id.edt_comment) ;

        first_ride = (TextView) rootView.findViewById(R.id.first_ride);



        nextride = (TextView) rootView.findViewById(R.id.last_ride);

        rclist = (RecyclerView) rootView.findViewById(R.id.recylist) ;

        image_rider = (ImageView) rootView.findViewById(R.id.image_rider);
        camera.setEnabled(false);

        vone = (View)rootView.findViewById(R.id.view_one) ;
        vtwo = (View)rootView.findViewById(R.id.view_two);

        if(helper.vonestatus==1)
        {
            vone.setVisibility(VISIBLE);

        }
        else

        {
            vone.setVisibility(GONE);

        }
//        if (helper.vtwostatus==2){
//            vtwo.setVisibility(View.VISIBLE);
//        }
//        else {
//            vtwo.setVisibility(View.GONE);
//        }



        first_ride.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                  flag = 1;
                helper.rides_status=1;
                helper.vonestatus=0;

                Log.d("selected_Array", "hhhjjb");
                    rclist.removeAllViews();
                    rclist.removeAllViewsInLayout();
                      cover_photo_lists.clear();

                vone.setVisibility(View.VISIBLE);
                      makeStringReq();
                       new AsyncLoadData2();


                     {
                         vtwo.setVisibility(View.GONE);
                     }
            }

        });

        image_rider.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                helper.rides_status = 2;
                    rclist.removeAllViews();
                rclist.removeAllViewsInLayout();
                cover_photo_lists.clear();
                  makeStringReq();
                  vone.setVisibility(View.GONE);
                  vtwo.setVisibility(View.GONE);

            }
        });


        nextride.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.rides_status=3;

                flag=1;
                rclist.removeAllViews();
                rclist.removeAllViewsInLayout();
                cover_photo_lists.clear();
                vtwo.setVisibility(View.VISIBLE);
                makeStringReq();
                vone.setVisibility(View.GONE);


            }
        });


        camera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        header_logo = (ImageView) rootView.findViewById(R.id.header_logo);
        header_logo.setImageResource(R.drawable.header_logo);
        header_logo.setVisibility(VISIBLE);
        video = (ImageView) rootView.findViewById(R.id.header_video);
        video.setVisibility(GONE);
        video.setEnabled(false);
        video.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        msg_select = (TextView) rootView.findViewById(R.id.msg_select);
        error = (TextView) rootView.findViewById(R.id.error);
        events = (ImageView) rootView.findViewById(R.id.header_event);
        events.setImageResource(R.drawable.header_add);
        events.setVisibility(VISIBLE);


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rclist.setLayoutManager(manager);


        if (helper.status.equals("myprofile_ride") || helper.status.equals("otherprofile_ride")) {

            header.setVisibility(GONE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) scroll_view.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            scroll_view.setLayoutParams(layoutParams);
            addRides.setVisibility(GONE);
            msg_select.setVisibility(GONE);

        }
        makeStringReq();


        events.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View v) {

                if (helper.rides_status == 2) {

                    final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_custom4);

                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();
                    RelativeLayout takepicture = (RelativeLayout) dialog.findViewById(R.id.gallery_rl4);
                    RelativeLayout uploadfile = (RelativeLayout) dialog.findViewById(R.id.camera_rl4);
                    RelativeLayout cancel_rl = (RelativeLayout) dialog.findViewById(R.id.cancel_rl);
                    cancel_rl.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });
                    takepicture.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            startCamera1();
                            dialog.dismiss();

                        }
                    });


                    uploadfile.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_RESULTS);
                            dialog.dismiss();

                        }

                    });


                } else {


                    final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_custom);

                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
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
                              Crop2.pickImage(getActivity(), RidesFragmnts.this);
                              dialog.dismiss();

                        }
                    });
                }
            }
        });


        return rootView;
    }




      void startCamera1() {
          try {

              dispatchTakePictureIntent1();
          } catch (IOException e) {

          }
      }

      void startCamera() {
          try {

              dispatchTakePictureIntent();

          }    catch (IOException e) {

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

          Log.d("cuurent path",">>>>>>"+mCurrentPhotoPath);
          return image;
      }
      private void dispatchTakePictureIntent1() throws IOException {
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
                  startActivityForResult(takePictureIntent, 1000);
              }
          }
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

      private void hideProgDialog() {
          try {
              if (progress_dialog != null && progress_dialog.isShowing())
                  progress_dialog.dismiss();
          } catch (Exception e) {
              e.printStackTrace();
          }


      }


      private AsyncLoadDatacover asyncLoad;

      private void loadDatacover() {
          if (asyncLoad == null
                  || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
              asyncLoad = new AsyncLoadDatacover();
              asyncLoad.execute();
          }
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




//
//          @Override
//          protected void onPostExecute(Void aVoid) {
//              mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
//              mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
//              try {
//
//                  if (msg.equals("hoto Add Successfully")) {
//
//
//                      JSONObject jsonObject = jobj.getJSONObject("response");
//
//
//                  }
//              } catch (JSONException e) {
//                  e.printStackTrace();
//              }
//
        // }





      private void makeStringReq() {
//        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
//        progress_dialog_msg = "loading...";

          cover_photo_lists.clear();
//          helper.preview_list.clear();


          StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                  new Response.Listener<String>() {
                      @Override
                      public void onResponse(String response) {
                          Log.d("params", response.toString());


//                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
//                       mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                          JSONObject obj = null;
                          try {
                              obj = new JSONObject(response.toString());
                              Log.d("params", response.toString());
                              int maxLogSize = 1000;
                              for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                  int start1 = i * maxLogSize;
                                  int end = (i + 1) * maxLogSize;
                                  end = end > response.length() ? response.toString().length() : end;
                                  Log.d("RideData", ">>>>>>" + response.toString().substring(start1, end));

                              }
///                            msg=obj.getString("msg");
                              JSONArray jsonArray = obj.getJSONArray("result");

                              ArrayList<Cover_Photo_List> cover_photo = new ArrayList<>();

                              for (int i = 0; i < jsonArray.length(); i++) {
                                  JSONObject object = jsonArray.getJSONObject(i);
                                  Cover_Photo_List photo_entity = new Cover_Photo_List();
                                  photo_entity.setRide_id(object.getString("cover_photo_id"));
                                  photo_entity.setCover_photo_id(object.getString("cover_photo_id"));
                                  photo_entity.setCover_photo(object.getString("thumb_320_320"));
                                  photo_entity.setUser_id(object.getString("user_id"));
                                  photo_entity.setDatetime(object.getString("datetime"));
                                  photo_entity.setMainPic(object.getString("mainPic"));

                                  if (helper.status.equals("myprofile_ride")) {
                                      photo_entity.setClass_status("1");
                                  } else if (helper.status.equals("otherprofile_ride")) {
                                      photo_entity.setClass_status("2");
                                  } else {
                                      photo_entity.setClass_status("0");
                                  }
                                  cover_photo.add(photo_entity);

                              }

                              cover_photo_lists.addAll(cover_photo);

//                              helper.preview_list.addAll(cover_photo);


                              if (cover_photo_lists.size() > 0) {


                                  ridesphoto_adapter = new Rides_pic_adapter(getActivity(), cover_photo_lists);

                                  rclist.setAdapter(ridesphoto_adapter);
                                  //photolistView.setAdapter((ListAdapter) ridesphoto_adapter);


                              } else {
                                  msg_select.setVisibility(GONE);
                                  error.setVisibility(VISIBLE);
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
//                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
//                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
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


//.............................//

              @Override
              protected Map<String, String> getParams() {
                  Map<String, String> params = new HashMap<String, String>();

                  params.put("action", "Show_all_my_ride_list");
                  if (helper.status.equals("otherprofile_ride")) {
                      params.put("user_id", helper.user_id);
                      params.put("type", "otherride");
                  } else {
                      params.put("user_id", PrefMangr.getInstance().getUserId());
                      params.put("type", "myride");
                  }
                  Log.d("params", params.toString());
                  return params;
              }

          };




          //.....................//

          RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
          requestQueue.add(stringRequest);



          // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(stringRequest,
//                tag_string_req);

          // Cancelling request
          // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
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



      public Uri getImageUri(Context inContext, Bitmap inImage) {
          ByteArrayOutputStream bytes = new ByteArrayOutputStream();
          inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
          String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
          return Uri.parse(path);
      }


      @Override
      public void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);



          if (requestCode == CropRide.REQUEST_PICK && resultCode == getActivity().RESULT_OK) {
              beginCrop(data.getData());

          } else if (requestCode == CropRide.REQUEST_CROP) {
              handleCrop2(resultCode, data);


          }






         /* if (requestCode == LOAD_IMAGE_RESULTS && resultCode == getActivity().RESULT_OK) {

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
                  loadDatacover();

              } catch (FileNotFoundException e) {
                  return;
              } catch (IOException e) {
                  e.printStackTrace();
              }



              MediaScannerConnection.scanFile(getActivity(),
                      //new String[]{.getPath()}, null,
                      new MediaScannerConnection.OnScanCompletedListener() {
                          public void onScanCompleted(String path, Uri uri) {
                          }
                      });
          }*/
          if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK) {

              Uri imageUri = Uri.parse(mCurrentPhotoPath);
              File file = new File(imageUri.getPath());
              try {
                  InputStream ims = new FileInputStream(file);
                  // ivPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
                  bitmap = BitmapFactory.decodeStream(ims);

                  try {
                      bitmap = rotateImageIfRequired(getActivity(), bitmap, imageUri);
                      int w = bitmap.getWidth();
                      int h = bitmap.getHeight();

                      beginCrop(getImageUri(getActivity(), bitmap));
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
                   loadDatacover();

              } catch (FileNotFoundException e) {
                  return;
              }


              // ScanFile so it will be appeared on Gallery
              MediaScannerConnection.scanFile(getActivity(),
                      new String[]{imageUri.getPath()}, null,
                      new MediaScannerConnection.OnScanCompletedListener() {
                          public void onScanCompleted(String path, Uri uri) {
                          }
                      });
          }
          if (requestCode == 1000 && resultCode == getActivity().RESULT_OK) {

              Uri imageUri = Uri.parse(mCurrentPhotoPath);
              File file = new File(imageUri.getPath());
              try {
                  InputStream ims = new FileInputStream(file);
                  // ivPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
                  bitmap = BitmapFactory.decodeStream(ims);

                  try {
                      bitmap = rotateImageIfRequired(getActivity(), bitmap, imageUri);
                      int w = bitmap.getWidth();
                      int h = bitmap.getHeight();

                      beginCrop(getImageUri(getActivity(), bitmap));
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
                  // loadDatacover();

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

          if (requestCode == LOAD_IMAGE_RESULTS && resultCode == -1 && data != null) {

              Uri selectedImageUri = data.getData();

              try {
                  bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                  bitmap = rotateImageIfRequired(getActivity(), bitmap, selectedImageUri);
              } catch (IOException e) {
                  e.printStackTrace();
              }
              loadDatacover();


              // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
              if (null != selectedImageUri) {
                  // Get the path from the Uri
                  String path = getPathFromURI(selectedImageUri);

                  // Set the image in ImageView

                  // bitmap = getBitmapFromURL(path);
                  ByteArrayOutputStream baos = new ByteArrayOutputStream();

              }}
      }

      private void beginCrop(Uri source) {
          Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
          CropRide.of(source, destination).asSquare().start(getActivity(), RidesFragmnts.this);

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

      private void handleCrop2(int resultCode, Intent result) {
          if (resultCode == getActivity().RESULT_OK) {
              try {
                  bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), CropRide.getOutput(result));

              } catch (IOException e) {
                  e.printStackTrace();
              }
              loadDatacover();

          } else if (resultCode == CropRide.RESULT_ERROR) {
              Toast.makeText(getActivity(), CropRide.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
          }
      }

      public void encodeTobase64(Bitmap image) {
          Bitmap immagex = image;
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
          image_aaray = baos.toByteArray();
          loadDatacover();
//        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
//        return imageEncoded;
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




      //asynchronous task
      class AsyncLoadDatacover extends AsyncTask<String, Void, Void> {
          boolean flag = false;

          @Override
          protected Void doInBackground(String... strings) {
              mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
              ByteArrayOutputStream baos = new ByteArrayOutputStream();
              bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
              image_aaray = baos.toByteArray();
              LoadApi api = new LoadApi();

              try {
                  if (image_aaray != null) {
                      Log.d("action", "" + "Add_ride_cover_photo");
                      Log.d("user_id", "" + PrefMangr.getInstance().getUserId());
                      Log.d("Picture", "" + image_aaray);
//                    Log.d("pic_type", "" + pic_type);
                      api.uploadcover("Add_ride_cover_photo", PrefMangr.getInstance().getUserId(), image_aaray, "h");

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
              if (msg.equals("Add Successfully")) {

                  AlertDialog.Builder alert;
                  if (Build.VERSION.SDK_INT >= 11) {
                      alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                  } else {
                      alert = new AlertDialog.Builder(getActivity());
                  }
                  alert.setTitle("Successfull");
                  alert.setMessage("Add Successfully");


                  alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                                 /*       preferences2 =getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
                                        dev=preferences2.edit();

                                        dev.putString("email_txt",email_login);
                                        dev.putString("password_txt",password_login);
                                        dev.commit();
*/
                          Intent intent = new Intent(getActivity(), Wall.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                          intent.putExtra("flag", 3);

                          helper.vonestatus=1;

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
      }

      private class AsyncLoadData2 extends AsyncTask<String, Integer, Void> {


          public AsyncLoadData2() {

          }

          @Override
          protected Void doInBackground(String... strings) {
//              mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
//              progress_dialog_msg = "loading";
              ByteArrayOutputStream baos = new ByteArrayOutputStream();
//              bitmap.compress(Bitmap.CompressFormat.JPEG, 400, baos);
              byte[] imageBytes = baos.toByteArray();
              LoadApi api = new LoadApi();

              try

              {
                  if (image_aaray != null) {
                      Log.d("action", "" + "Add_ride");
                      Log.d("user_id", "" + PrefMangr.getInstance().getUserId());
                      Log.d("ride_photo", "" + image_aaray);
//                      Log.d("description ",""+Comment);

//                    Log.d("pic_type", "" + pic_type);
                      api.Action_Add_ride("Add_ride", PrefMangr.getInstance().getUserId(), image_aaray, comment);

                  }
                  else {
                      Toast.makeText(getActivity(), "Please Select Image", Toast.LENGTH_SHORT).show();
                  }


              } catch (Exception e) {
                  e.printStackTrace();
              }

              return null;


          }


          private AsyncLoadData2 asyncLoad2;

          private void loadData2() {

              if (asyncLoad2 == null
                      || asyncLoad2.getStatus() != AsyncTask.Status.RUNNING) {
                  asyncLoad2 = new AsyncLoadData2();
                  asyncLoad2.execute();
              }

          }


          @Override
          protected void onPostExecute(Void aVoid) {
              super.onPostExecute(aVoid);
              mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
              if (msg.equals("Add Successfully")) {

                  AlertDialog.Builder alert;
                  if (Build.VERSION.SDK_INT >= 11) {
                      alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                  } else {
                      alert = new AlertDialog.Builder(getActivity());
                  }
                  alert.setTitle("Successfull");
                  alert.setMessage("Add Successfully");


              }
          }
      }

      @Override
      public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
          super.onViewCreated(view, savedInstanceState);
          if (Commom_Method.isOnline(getActivity())) {
              cover_photo_lists.clear();

//            makeStringReq();
          } else {
              Commom_Method.showAlert(getActivity(),
                      getString(R.string.networkError_Message));
          }
      }


  }


