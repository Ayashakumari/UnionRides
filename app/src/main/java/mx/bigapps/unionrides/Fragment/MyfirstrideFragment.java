package mx.bigapps.unionrides.Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
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

import com.soundcloud.android.crop.Crop;

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

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import mx.bigapps.unionrides.Activity.Crop2;
import mx.bigapps.unionrides.Adapter.RiderPhotos_Adapter;
import mx.bigapps.unionrides.Adapter.onClickShowPic;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.BuildConfig;
import mx.bigapps.unionrides.Model.Added_Image_Entity;
import mx.bigapps.unionrides.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyfirstrideFragment extends Fragment implements onClickShowPic {
    Button addrides ;

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









    public MyfirstrideFragment() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_myfirstride, container, false);



        return rootView;




    }

    @Override
    public void showPicUrl(String url) {

    }
}

