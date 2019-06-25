package mx.bigapps.unionrides.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import mx.bigapps.unionrides.Activity.Rides;
import mx.bigapps.unionrides.Activity.SquareImageView;
import mx.bigapps.unionrides.Activity.StretchVideoView;
import mx.bigapps.unionrides.Adapter.RiderPhotos_Adapter;
import mx.bigapps.unionrides.Adapter.onClickVideo;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.Added_Image_Entity;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.AndroidMultiPartEntity;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.FileUtils;
import mx.bigapps.unionrides.utils.LoadApi;
import mx.bigapps.unionrides.utils.MediaController;
import mx.bigapps.unionrides.utils.helper;


import static android.app.Activity.RESULT_OK;


public class VideoRides extends Fragment implements onClickVideo {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList photolist;
    GridViewWithHeaderAndFooter photo_recycle;
    RecyclerView.LayoutManager layoutManager;

    static StretchVideoView simpleVideoView;

    RiderPhotos_Adapter photosAdapter;
    Activity activity;
    private static int LOAD_IMAGE_RESULTS = 1000;
    ArrayList<Added_Image_Entity> image_list = new ArrayList<Added_Image_Entity>();
    Bitmap bitmap;
    RelativeLayout videolayout;
    Uri uriVideo = null;
    int stopPosition;
    TextView error;
    static SquareImageView video_image;
    String url;
    static ImageButton mPlayButton;
    private PublicPosts.OnFragmentInteractionListener mListener;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    JSONObject jobj;
    String msg;
    Button addVideos;
    RiderPhotos_Adapter photo_adapter;
    private static int REQUEST_TAKE_GALLERY_VIDEO = 1000;
    static final int REQUEST_VIDEO_CAPTURE = 11;
    ArrayList<Uri> videoUriList;

    private static final int SELECT_VIDEO = 1;

    private static String selectedPath, selectedThumbPath;
    private File tempFile;
    private String textPath;
    long totalSize = 0;
    private static final int VIDEO_CAPTURE = 101;
    Uri uri, videoThumbUri;
    private String selectedVideoPath, selectThumbPath;
    Button addphoto;

    public VideoRides() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (savedInstanceState != null) {
                stopPosition = savedInstanceState.getInt("position");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_rides, container, false);
        error = (TextView) view.findViewById(R.id.error);
        videolayout = (RelativeLayout) view.findViewById(R.id.addvideolayout);
        addphoto = (Button) view.findViewById(R.id.addphoto);
        addphoto.setText("+Add Videos");

        photo_recycle = (GridViewWithHeaderAndFooter) view.findViewById(R.id.videolist);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.addphotofooter, null, false);
        photo_recycle.addFooterView(footerView);
        addVideos = (Button) footerView.findViewById(R.id.addphotos);
        addVideos.setText("+Add Videos");
        activity = getActivity();


        videoUriList = new ArrayList<>();
        if (helper.status.equals("otherprofile_ride")) {
            addVideos.setVisibility(View.GONE);
            addphoto.setVisibility(View.GONE);
        }


        Rides.events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.riderStatus.equals("1")) {
                    final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.videodialog_custom);

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

                            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
                                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);

                            }
                            dialog.dismiss();

                        }
                    });
                    uploadfile.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setType("video/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
                            dialog.dismiss();

                        }
                    });
                }
            }
        });
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.videodialog_custom);

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

                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {

                            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                            //   }
                        }
                        dialog.dismiss();

                    }
                });
                uploadfile.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
                        dialog.dismiss();

                    }
                });

            }
        });

        addVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.videodialog_custom);

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
                      /*  Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        if (takeVideoIntent.resolveActivity(Wall.this.getPackageManager()) != null) {
                            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                        }
                        dialog.dismiss();

*/
                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
//                            if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
//                            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                            //   }
                        }
                        dialog.dismiss();

                    }
                });
                uploadfile.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
                        dialog.dismiss();

                    }
                });

            }
        });


      /*  mPlayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (simpleVideoView.isPlaying()) {
                    mPlayButton.setVisibility(View.VISIBLE);
                    simpleVideoView.pause();
                } else {
                    simpleVideoView.start();
                    mPlayButton.setVisibility(View.GONE);
                }
            }
        });*/
        photolist = new ArrayList();
        photolist.add("");
        photolist.add("");
        photolist.add("");
        photolist.add("");
        photolist.add("");
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == -1) {
            bitmap = (Bitmap) data.getExtras().get("data");
            bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
            loadData();

        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == -1) {
            Uri videoUri = data.getData();
            selectedPath = getPath(videoUri);
            bitmap = ThumbnailUtils.createVideoThumbnail(selectedPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            textPath = selectedPath;
            tempFile = FileUtils.saveTempFile(System.currentTimeMillis() + "video.mp4", getActivity(), Uri.fromFile(new File(selectedPath)));

            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(tempFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
            File myDir = new File(Environment.getExternalStorageDirectory() + "/VideoCompressor/Temp/");
            myDir.mkdirs();
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "imagerotation" + n + ".jpg";
            File file = new File(myDir, fname);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                thumb.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tempFile.exists()) {
                Log.e("yes", "yes");
            } else {
                Log.e("no", "no");
            }


            if (tempFile.exists()) {
                if (textPath == null && textPath.equals("")) {
                    Toast.makeText(getActivity(), "Please select Video file.", Toast.LENGTH_SHORT).show();
                } else {

                    Log.e("sizeeeeeeeeeeeee", tempFile.length() + "");
                    if (tempFile.length() < 2394767) {
                        textPath = tempFile.getAbsolutePath();
                        selectedThumbPath = file.getAbsolutePath();
                        new UploadFileToServer().execute();
                    } else {
                        new UploadFileToServer().execute();
                    }
                }
            } else {
                Toast.makeText(getActivity(), "Please select Video file.", Toast.LENGTH_SHORT).show();
            }

        }
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                bitmap = ThumbnailUtils.createVideoThumbnail(selectedPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                textPath = selectedPath;
                tempFile = FileUtils.saveTempFile(System.currentTimeMillis() + "video.mp4", getActivity(), Uri.fromFile(new File(selectedPath)));

                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(tempFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                File myDir = new File(Environment.getExternalStorageDirectory() + "/VideoCompressor/Temp/");
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = "imagerotation" + n + ".jpg";
                File file = new File(myDir, fname);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    thumb.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (tempFile.exists()) {
                    Log.e("yes", "yes");
                } else {
                    Log.e("no", "no");
                }


                if (tempFile.exists()) {
                    if (textPath == null && textPath.equals("")) {
                        Toast.makeText(getActivity(), "Please select Video file.", Toast.LENGTH_SHORT).show();
                    } else {

                        Log.e("sizeeeeeeeeeeeee", tempFile.length() + "");
                        if (tempFile.length() < 2394767) {
                            textPath = tempFile.getAbsolutePath();
                            selectedThumbPath = file.getAbsolutePath();
                            new UploadFileToServer().execute();
                        } else {
                            new UploadFileToServer().execute();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Please select Video file.", Toast.LENGTH_SHORT).show();
                }


            }
        }

    }

    @Override
    public void showVideoUrl(String pic_url, final String video_url) {
        Log.d("Pic_URL", "<<>>" + pic_url);
        Log.d("Video_url", "<<>>" + video_url);
        // Picasso.with(getContext()).load(pic_url).placeholder(R.drawable.loading).into(video_image);
        url = video_url;
        PrefMangr.getInstance().setVideoUrl(url);
    }


    class VideoViewInfo {
        String filePath;
        String mimeType;
        String thumbPath;
        String title;
    }

    class VideoCompressor extends AsyncTask<Void, Void, String> {
        private Dialog dialog;
        private ProgressBar progressBar;
        private TextView txtPercentage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialog.getWindow().setAttributes(lp);

            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_progress);
            dialog.setCancelable(true);
            progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
            txtPercentage = (TextView) dialog.findViewById(R.id.txtPercentage);
            txtPercentage.setVisibility(View.GONE);

            dialog.show();

            progressBar.setVisibility(View.VISIBLE);
            Log.e("MyVideos", "Start video compression");
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.e("VideoCompressor", tempFile.getPath());

            return MediaController.getInstance().convertVideo(tempFile.getPath());
        }

        @Override
        protected void onPostExecute(String compressed) {
            super.onPostExecute(compressed);
            progressBar.setVisibility(View.GONE);
            dialog.dismiss();
            //txtPath.setText(compressed.trim());
            //txtPath.setVisibility(View.VISIBLE);
            textPath = compressed.trim();
            VideoViewInfo info = new VideoViewInfo();
            info.filePath = textPath;

            Log.e("path", textPath);
            //loadData();
            new UploadFileToServer().execute();
        }
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        private Dialog dialog;
        private ProgressBar progressBar;
        private ProgressBar progressBar1;
        private TextView txtPercentage;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialog.getWindow().setAttributes(lp);

            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_progress);
            dialog.setCancelable(true);
            progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
            progressBar1 = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            txtPercentage = (TextView) dialog.findViewById(R.id.txtPercentage);

            dialog.show();
            progressBar1.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar1.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar1.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();
            Log.e("path", textPath);
            // return uploadFile(textPath, PrefMangr.getInstance().getUserId(), imageBytes);
            return uploadFile(selectedPath, PrefMangr.getInstance().getUserId(), imageBytes);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String selectedVideoPath, String users_id,
                                  byte[] selectThumbPath) {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Apis.api_url);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(selectedVideoPath);
                Log.e(selectedVideoPath, sourceFile.getAbsolutePath());


                if (!sourceFile.exists()) {
                    Log.e("no", "no");
                }
                entity.addPart("ride_video", new FileBody(sourceFile));
                entity.addPart("user_id", new StringBody(PrefMangr.getInstance().getUserId(), "text/plain", Charset.forName("UTF-8")));
                entity.addPart("action", new StringBody("Addridevideos", "text/plain", Charset.forName("UTF-8")));
                entity.addPart("cover_photo_id", new StringBody(PrefMangr.getInstance().getCoverPicID(), "text/plain", Charset.forName("UTF-8")));
                entity.addPart("video_thumbnail", new ByteArrayBody(selectThumbPath, "image/jpeg", "test1.jpg"));
                //  entity.addPart("image", new ByteArrayBody(selectThumbPath, "image/jpeg", "test1.jpg"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            Log.d("responseString", responseString);

            return responseString;


        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("MyVideos", "Response from server: " + result.substring(result.indexOf(">") + 1, result.length()));


            txtPercentage.setVisibility(View.GONE);
            progressBar1.setVisibility(View.GONE);

            dialog.dismiss();

            try {
                //	if(flagValue==1){


                if (result.contains("Video Add Successfully")) {

                    AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(getActivity());
                    }
                    //alert.setTitle("Success");
                    alert.setMessage("Video Upload Successfully");


                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog1, int arg1) {
                            // TODO Auto-generated method stub
                            helper.riderfrag = "1";
                            Intent intent = new Intent(getActivity(), Rides.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("flag", 1);
                            intent.putExtra("cover_id", PrefMangr.getInstance().getCoverPicID());
                            startActivity(intent);
                        }

                    });

                    try {
                        Dialog dialog1 = alert.create();
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //mpath_list.clear();
                    File dir = new File(Environment.getExternalStorageDirectory() + "/VideoCompressor/Compressed Videos/");
                    Log.e("dir", dir.getAbsolutePath());
                    if (dir.isDirectory()) {
                        String[] children = dir.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(dir, children[i]).delete();
                        }
                    }

                    File dir1 = new File(Environment.getExternalStorageDirectory() + "/VideoCompressor/Temp/");
                    Log.e("dir", dir1.getAbsolutePath());
                    if (dir1.isDirectory()) {
                        String[] children = dir1.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(dir1, children[i]).delete();
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }

    }


    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();
            LoadApi api = new LoadApi();
            try {


                jobj = api.Action_profileSetting_Image("Postnews", PrefMangr.getInstance().getUserId(), imageBytes,"");

                //  jobj = api.Action_profileSetting_noImage(action, user_id, first_name, last_name, email_address, mobile_number, date_of_birth, byte_arr);

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
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);

            hideProgDialog();

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
///                            msg=obj.getString("msg");
                            image_list.clear();
                            JSONArray jsonArray = obj.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Added_Image_Entity photo_entity = new Added_Image_Entity();
                                photo_entity.setCover_photo_id(object.getString("cover_photo_id"));
                                photo_entity.setFile_type(object.getString("file_type"));
                                photo_entity.setFile_url(object.getString("file_url"));
                                photo_entity.setUser_id(object.getString("user_id"));
                                photo_entity.setDatetime(object.getString("datetime"));
                                if(object.has("video_web_views_url")) {
                                    photo_entity.setVideo_web_views_url(object.getString("video_web_views_url"));
                                }
                                if (helper.status.equals("otherprofile_ride")) {
                                    photo_entity.setProfile_status("1");

                                } else {
                                    photo_entity.setProfile_status("0");
                                }
                                photo_entity.setVideo_thumbnail(object.getString("video_thumbnail"));
                                photo_entity.setId(object.getString("id"));
                                image_list.add(photo_entity);

                            }
                            if (image_list.size() > 0) {
                                error.setVisibility(View.GONE);
                                // videolayout.setBackgroundColor(getContext().getResources().getColor(R.color.black_gunmetal));
                                photo_adapter = new RiderPhotos_Adapter(getActivity(), image_list);
                                photo_recycle.setAdapter(photo_adapter);

                                url = image_list.get(0).getFile_url();
                                PrefMangr.getInstance().setVideoUrl(url);
                                //   Picasso.with(getContext()).load(image_list.get(0).getVideo_thumbnail()).placeholder(R.drawable.loading).into(video_image);
//mPlayButton.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//      //  video_image.setVisibility(View.GONE);
////        simpleVideoView.setVisibility(View.VISIBLE);
////        Uri uri = Uri.parse(image_list.get(0).getFile_url());
////        simpleVideoView.setVideoURI(uri);
////        simpleVideoView.start();
//        Intent intent=new Intent(getActivity(),Web.class);
//        intent.putExtra("video",image_list.get(0).getFile_url());
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(intent);
//    }
//});

                            } else {
                                if (helper.status.equals("otherprofile_ride")) {

                                    addphoto.setVisibility(View.GONE);
                                } else {
                                    addphoto.setVisibility(View.VISIBLE);
                                }
                                error.setVisibility(View.VISIBLE);
                                error.setVisibility(View.VISIBLE);
                                error.setText("No video preview available for this Ride");

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
                if (helper.status.equals("otherprofile_ride")) {
                    params.put("user_id", helper.user_id);
                } else {
                    params.put("user_id", PrefMangr.getInstance().getUserId());
                }
                params.put("cover_photo_id", PrefMangr.getInstance().getCoverPicID());
                params.put("type", "video");

                //   params.put("cover_image",image_aaray);


                Log.d("params", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeStringReq();
    }
}

