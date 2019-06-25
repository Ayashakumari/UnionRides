package mx.bigapps.unionrides.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.soundcloud.android.crop.Crop;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.BuildConfig;
import mx.bigapps.unionrides.Fragment.Clubs;
import mx.bigapps.unionrides.Fragment.Fans;
import mx.bigapps.unionrides.Fragment.Fans2;
import mx.bigapps.unionrides.Fragment.My_lastrideFragment;
import mx.bigapps.unionrides.Fragment.MyfirstrideFragment;
import mx.bigapps.unionrides.Fragment.Page3;
import mx.bigapps.unionrides.Fragment.Posts;
import mx.bigapps.unionrides.Fragment.PostsWall;
import mx.bigapps.unionrides.Model.NetWorkUserList;
import mx.bigapps.unionrides.Model.UserPosts;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.AndroidMultiPartEntity;
import mx.bigapps.unionrides.utils.Apis;
import mx.bigapps.unionrides.utils.FileUtils;
import mx.bigapps.unionrides.utils.LoadApi;
import mx.bigapps.unionrides.utils.MediaController;
import mx.bigapps.unionrides.utils.ViewFindUtils;
import mx.bigapps.unionrides.utils.helper;


public class Wall extends AppCompatActivity {
    ImageView camera, video, events;
    LinearLayout home, serrch, rides, setting;
    RelativeLayout chat;
    TextView txt_chatcount;
    ImageView ivhome, ivsearch, ivrides, ivchat;
    CircleImageView ivsetting;
    SlidingTabLayout tl_2;

    TextView myfirst_ride, mylast_ride;

    LinearLayout heading_header, linar_swift;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "FANS", "CLUBS", "PAGES"
    };
    private MyPagerAdapter mAdapter;
    static final int REQUEST_VIDEO_CAPTURE = 11;
    ArrayList<Uri> videoUriList;
    // private static final int SELECT_VIDEO = 1;
    static Fragment fragment = null;
    FrameLayout frameLayout;
    private boolean backPressedToExitOnce = false;
    private Toast toast = null;
    private static int LOAD_IMAGE_RESULTS = 1000;
    Bitmap bitmap;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    JSONObject jobj;
    String msg;
    int flag = 0;
    Intent intentn;
    private static final int SELECT_VIDEO = 3;

    private String selectedPath, selectedThumbPath;
    private File tempFile;
    private String textPath, outPath;
    long totalSize = 0;
    ImageView resultView;
    GPSTracker gpsTracker;
    private Uri mHighQualityImageUri = null;


    private final int REQUEST_CODE_HIGH_QUALITY_IMAGE = 2;
    private GoogleApiClient client;
    public static ViewPager vp;
    static ViewPager vp2;
    ImageView header_logo;
    FrameLayout content_Post;
    String search;
    NestedScrollView scroll_view;

    View vone,vtwo;

    private static final int REQUEST_TAKE_PHOTO = 1;

    final Handler ha = new Handler();
    String mCurrentPhotoPath;
    ImageView ridesimg;
    private Dialog m_dialog;
    private LinearLayout m_llMain;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static int page;
    LinearLayout headerlayout;
    private PostsWall postsWall = null;
    private Fans2 fans2 = null;
    private Search objSearch;
    private Recent_Chat recent_chat;
    private  Fans fans;
    private Clubs clubs;
    private Page3 page3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wall);
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
        File newdir = new File(dir);
        newdir.mkdirs();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //  chat_status.setVisibility(View.GONE);
                Showbadgecount();
                ha.postDelayed(this, 2000);
            }
        }, 2000);


        // postsWall.refresh = "1";
        //  Log.d("refreshh", "333" + postsWall.refresh);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.my_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(

                R.color.piink, R.color.darkpurple, R.color.green, R.color.orange);
        txt_chatcount = (TextView) findViewById(R.id.txt_chatcount);
//        header = findViewById(R.id.header);
        resultView = (ImageView) findViewById(R.id.result_image);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        content_Post = (FrameLayout) findViewById(R.id.content_Post);
        tl_2 = (SlidingTabLayout) findViewById(R.id.tl_2);
        ivhome = (ImageView) findViewById(R.id.home_img);
        ivhome.setImageResource(R.drawable.ic_home_active);
        ivsearch = (ImageView) findViewById(R.id.search_img);
        ivrides = (ImageView) findViewById(R.id.rides);
        ivchat = (ImageView) findViewById(R.id.chat);

        vone = (View)findViewById(R.id.view_one);
        vtwo = (View)findViewById(R.id.view_two);
        ivsetting = (CircleImageView) findViewById(R.id.setting);
        // ivsetting.setImageResource(R.drawable.ic_user);
        home = (LinearLayout) findViewById(R.id.toolbar_home);
        headerlayout = (LinearLayout) findViewById(R.id.headerlayout);
        serrch = (LinearLayout) findViewById(R.id.toolbar_search);
        rides = (LinearLayout) findViewById(R.id.toolbar_rides);
        chat = (RelativeLayout) findViewById(R.id.toolbar_chat);
        setting = (LinearLayout) findViewById(R.id.toolbar_setting);
        camera = (ImageView) findViewById(R.id.header_camera);
        camera.setImageResource(R.drawable.header_camera);
        video = (ImageView) findViewById(R.id.header_video);
        events = (ImageView) findViewById(R.id.header_event);
        header_logo = (ImageView) findViewById(R.id.header_logo);
        scroll_view = (NestedScrollView) findViewById(R.id.scroll_view);
        header_logo.setVisibility(View.VISIBLE);
        header_logo.setImageResource(R.drawable.list);
        events.setImageResource(R.drawable.profile_ic_event_active);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Fans"));
        tabLayout.addTab(tabLayout.newTab().setText("Club"));
        tabLayout.addTab(tabLayout.newTab().setText("Page3"));



        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }
        gpsTracker = new GPSTracker(Wall.this, "");
        if (Build.VERSION.SDK_INT >= 23) {
            if (gpsTracker.checkPermission()) {
                if (gpsTracker.checkPermissionCamera()) {

                } else {
                    gpsTracker.requestPermissionCamera();
                }
            } else {
                gpsTracker.requestPermission();
            }
        } else {

        }
        try {
            String image = PrefMangr.getInstance().getProfilepic();
            Log.d("image", image);

            Glide.with(Wall.this)
                    .load(image)
                    // .placeholder(R.drawable.ic_user)
                    // .error(R.drawable.ic_user)
                    .override(100, 100)

                    // .diskCacheStrategy(DiskCacheStrategy.NONE)
                    // .skipMemoryCache(true)
                    .into(ivsetting);



        } catch (Exception e) {
            e.printStackTrace();
        }
        intentn = getIntent();
        flag = intentn.getIntExtra("flag", 0);
        search = intentn.getStringExtra("search");

        View decorView = getWindow().getDecorView();

        vp = ViewFindUtils.find(decorView, R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), "0");
        vp.setAdapter(mAdapter);

        SlidingTabLayout tabLayout_2 = ViewFindUtils.find(decorView, R.id.tl_2);
        tabLayout_2.setViewPager(vp);
        tabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    fans.repostUser(Wall.this);
                }
                if (position == 1) {
                    clubs.repostUser(Wall.this);
                }
                if (position == 2) {
                    page3.repostUser(Wall.this);
                }

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (postsWall != null) {
                    postsWall.refresh = "1";
                    Log.d("refreshh", "333" + postsWall.refresh);

                    postsWall.userPost.removeAllViews();
                    postsWall.userPost.removeAllViewsInLayout();
                    postsWall.loading = true;
                    postsWall.previousTotal = 0;
                    postsWall.visibleThreshold = 5;
                    postsWall.firstVisibleItem = 0;
                    postsWall.visibleItemCount = 0;
                    postsWall.totalItemCount = 0;
                    postsWall.page = 0;

                    postsWall.userPostsArrayList.clear();
                    //list.clear();
                    postsWall.flagvalue1 = postsWall.flagvalue = 0;
                    postsWall.showAllPostsscroll(Wall.this, 0);
                }
            }
        });


        scroll_view.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                View view = (View) scroll_view.getChildAt(scroll_view.getChildCount() - 1);

                int diff = (view.getBottom() - (scroll_view.getHeight() + scroll_view
                        .getScrollY()));

                if (diff == 0) {

                    if (postsWall != null) {
                        postsWall.visibleItemCount = postsWall.userPost.getChildCount();
                        postsWall.totalItemCount = postsWall.layoutManager.getItemCount();
                        postsWall.firstVisibleItem = postsWall.layoutManager.findFirstVisibleItemPosition();
                        Log.d("visibleItemCount", "" + postsWall.visibleItemCount);
                        Log.d("totalItemCount", "" + postsWall.totalItemCount);
                        Log.d("firstVisibleItem", "" + postsWall.firstVisibleItem);
                        Log.d("LogEntity1.size()", "" + postsWall.userPostsArrayList.size());
                        Log.d("total_count", postsWall.total_count);
                        Log.d("page", "" + postsWall.page);

                        if (postsWall.loading) {
                            if (postsWall.totalItemCount > postsWall.previousTotal) {
                                postsWall.loading = false;
                                postsWall.previousTotal = postsWall.totalItemCount;
                            }
                        }
                        if (!postsWall.loading && (postsWall.totalItemCount - postsWall.visibleItemCount)
                                <= (postsWall.firstVisibleItem + postsWall.visibleThreshold)) {
                            Log.d("page1", "" + postsWall.page);
                            try {
                                if (postsWall.userPostsArrayList.size() < Integer.parseInt(postsWall.total_count)) {
                                    Log.d("page2", "" + page);

                                    postsWall.page = postsWall.page + 1;
                                    postsWall.showAllPostsscroll(Wall.this, postsWall.page);


                                } else {

                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            postsWall.loading = true;
                        }
                    }
                }
            }
        });




        header_logo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                heading_header.setVisibility(View.GONE);

                vp.setVisibility(View.VISIBLE);
                headerlayout.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                content_Post.setVisibility(View.VISIBLE);
                if (header_logo.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.list).getConstantState())) {
                    header_logo.setImageResource(R.drawable.grid);
                    vp.setVisibility(View.GONE);
                    helper.status = "allpost";
                    fragment = new Fans2();
                    fans2 = (Fans2) fragment;
                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_Post, fragment).commit();
                        FragmentTransaction mFragmentTransaction = getFragmentManager()
                                .beginTransaction();

                        mFragmentTransaction.addToBackStack(null);
                    } else {
                        Log.e("MainActivity", "Error in creating fragment");
                    }

                } else if (header_logo.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.grid).getConstantState())) {
                    header_logo.setImageResource(R.drawable.list);
                    // Posts.repost(Wall.this, 0);
                    fragment = new PostsWall();
                    postsWall = (PostsWall) fragment;


                    postsWall.page = 0;
                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_Post, fragment).commit();
                        FragmentTransaction mFragmentTransaction = getFragmentManager()
                                .beginTransaction();

                        mFragmentTransaction.addToBackStack(null);
                    } else {
                        Log.e("MainActivity", "Error in creating fragment");
                    }

                } else {

                }

            }
        });
        if (search != null) {
            if (ivhome.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_home_active).getConstantState())) {
                helper.backbutton = "1";
            }
            if (ivsearch.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_search_active).getConstantState())) {
                helper.backbutton = "2";
            }
            if (ivchat.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_chat_active).getConstantState())) {
                helper.backbutton = "4";
            }
              /*  if (ivsetting.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.default_user).getConstantState())) {
                    helper.backbutton="5";
                }*/
            if (ivrides.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_rides_active).getConstantState())) {
                helper.backbutton = "3";
            }
            ivrides.setImageResource(R.drawable.ic_rides_inactive);
            ivsearch.setImageResource(R.drawable.ic_search_active);
            ivchat.setImageResource(R.drawable.ic_chat);
            ivhome.setImageResource(R.drawable.ic_home);
            //  ivsetting.setImageResource(R.drawable.ic_user);
            vp.setVisibility(View.GONE);
            headerlayout.setVisibility(View.GONE);
            headerlayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            content_Post.setVisibility(View.GONE);
            ArrayList<NetWorkUserList> netWorkUserLists = new ArrayList<>();
            Search f = Search.newInstance(netWorkUserLists);
            fragment = new Search();
            objSearch = (Search) fragment;
            helper.backbutton = "2";
            /*Search.netWorkUserLists = new ArrayList<>();
            Search.repost(Wall.this);*/
            //  Search.swipeRefreshLayout.setRefreshing(true);

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                FragmentTransaction mFragmentTransaction = getFragmentManager()
                        .beginTransaction();

                mFragmentTransaction.addToBackStack(null);

            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
        } else {
            frameLayout.setVisibility(View.GONE);
            content_Post.setVisibility(View.VISIBLE);
            fragment = new PostsWall();
            postsWall = (PostsWall) fragment;

            postsWall.page = 0;
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_Post, fragment).commit();
                FragmentTransaction mFragmentTransaction = getFragmentManager()
                        .beginTransaction();

                mFragmentTransaction.addToBackStack(null);
            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
        }

        if (flag == 100) {
            if (ivhome.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_home_active).getConstantState())) {
                helper.backbutton = "1";
            }
            if (ivsearch.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_search_active).getConstantState())) {
                helper.backbutton = "2";
            }
            if (ivchat.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_chat_active).getConstantState())) {
                helper.backbutton = "4";
            }
              /*  if (ivsetting.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.default_user).getConstantState())) {
                    helper.backbutton="5";
                }*/
            if (ivrides.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_rides_active).getConstantState())) {
                helper.backbutton = "3";
            }
            helper.status = "";
            ivrides.setImageResource(R.drawable.ic_rides_inactive);
            ivsearch.setImageResource(R.drawable.ic_search);
            ivchat.setImageResource(R.drawable.ic_chat);
            ivhome.setImageResource(R.drawable.ic_home_active);
            //   ivsetting.setImageResource(R.drawable.ic_user);
            header_logo.setImageResource(R.drawable.grid);
            frameLayout.setVisibility(View.GONE);
            content_Post.setVisibility(View.VISIBLE);

            fragment = new Fans2();
            vp.setVisibility(View.GONE);
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_Post, fragment).commit();

                FragmentTransaction mFragmentTransaction = getFragmentManager()
                        .beginTransaction();

                mFragmentTransaction.addToBackStack(null);
            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
        }
        if (flag == 3) {

            if (ivhome.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_home_active).getConstantState())) {
                helper.backbutton = "1";
            }
            if (ivsearch.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_search_active).getConstantState())) {
                helper.backbutton = "2";
            }
            if (ivchat.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_chat_active).getConstantState())) {
                helper.backbutton = "4";
            }
              /*  if (ivsetting.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.default_user).getConstantState())) {
                    helper.backbutton="5";
                }*/
            if (ivrides.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_rides_active).getConstantState())) {
                helper.backbutton = "3";
            }
            helper.status = "";
            ivrides.setImageResource(R.drawable.ic_rides_active);
            ivsearch.setImageResource(R.drawable.ic_search);
            ivchat.setImageResource(R.drawable.ic_chat);
            ivhome.setImageResource(R.drawable.ic_home);
            //   ivsetting.setImageResource(R.drawable.ic_user);
            vp.setVisibility(View.GONE);
            headerlayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            fragment = new RidesFragmnts();
            content_Post.setVisibility(View.GONE);

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                FragmentTransaction mFragmentTransaction = getFragmentManager()
                        .beginTransaction();

                mFragmentTransaction.addToBackStack(null);

            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
        } else if (flag == 2) {
            if (ivhome.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_home_active).getConstantState())) {
                helper.backbutton = "1";
            }
            if (ivsearch.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_search_active).getConstantState())) {
                helper.backbutton = "2";
            }
            if (ivchat.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_chat_active).getConstantState())) {
                helper.backbutton = "4";
            }
              /*  if (ivsetting.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.default_user).getConstantState())) {
                    helper.backbutton="5";
                }*/
            if (ivrides.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.ic_rides_active).getConstantState())) {
                helper.backbutton = "3";
            }

            ivrides.setImageResource(R.drawable.ic_rides_inactive);
            ivsearch.setImageResource(R.drawable.ic_search_active);
            ivchat.setImageResource(R.drawable.ic_chat);
            ivhome.setImageResource(R.drawable.ic_home);
            //   ivsetting.setImageResource(R.drawable.ic_user);
            vp.setVisibility(View.GONE);
            headerlayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            content_Post.setVisibility(View.GONE);
            ArrayList<NetWorkUserList> netWorkUserLists = new ArrayList<>();
            Search f = Search.newInstance(netWorkUserLists);
            fragment = new Search();
            objSearch = (Search) fragment;
           /* Search.netWorkUserLists = new ArrayList<>();
            Search.repost(Wall.this);*/
            // Search.swipeRefreshLayout.setRefreshing(true);

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
        } else if (flag == 5) {
            ivrides.setImageResource(R.drawable.ic_rides_inactive);
            ivsearch.setImageResource(R.drawable.ic_search);
            ivchat.setImageResource(R.drawable.ic_chat);
            ivhome.setImageResource(R.drawable.ic_home);
            //  ivsetting.setImageResource(R.drawable.ic_user_active);
            vp.setVisibility(View.GONE);
            headerlayout.setVisibility(View.GONE);
            content_Post.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            fragment = new MyProfile();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                FragmentTransaction mFragmentTransaction = getFragmentManager()
                        .beginTransaction();

                mFragmentTransaction.addToBackStack(null);

            } else {
                helper.status = "";
            }
        }
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                heading_header.setVisibility(View.GONE);

                if (ivhome.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_home_active).getConstantState())) {
                    helper.backbutton = "1";
                }
                if (ivsearch.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_search_active).getConstantState())) {
                    helper.backbutton = "2";
                }
                if (ivchat.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_chat_active).getConstantState())) {
                    helper.backbutton = "4";
                }
               /* if (ivsetting.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.default_user).getConstantState())) {
                    helper.backbutton="5";
                }*/
                if (ivrides.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_rides_active).getConstantState())) {
                    helper.backbutton = "3";
                }

                ivrides.setImageResource(R.drawable.ic_rides_inactive);
                ivsearch.setImageResource(R.drawable.ic_search);
                ivchat.setImageResource(R.drawable.ic_chat);
                ivhome.setImageResource(R.drawable.ic_home_active);
                //   ivsetting.setImageResource(R.drawable.ic_user);
                vp.setVisibility(View.VISIBLE);
                headerlayout.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                content_Post.setVisibility(View.VISIBLE);

                scroll_view.fullScroll(ScrollView.FOCUS_UP);

            }
        });
        chat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                heading_header.setVisibility(View.GONE);

                if (ivhome.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_home_active).getConstantState())) {
                    helper.backbutton = "1";
                }
                if (ivsearch.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_search_active).getConstantState())) {
                    helper.backbutton = "2";
                }
                if (ivchat.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_chat_active).getConstantState())) {
                    helper.backbutton = "4";
                }

                if (ivrides.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_rides_active).getConstantState())) {
                    helper.backbutton = "3";
                }
                ivrides.setImageResource(R.drawable.ic_rides_inactive);
                ivsearch.setImageResource(R.drawable.ic_search);
                ivchat.setImageResource(R.drawable.ic_chat_active);
                ivhome.setImageResource(R.drawable.ic_home);
                //   ivsetting.setImageResource(R.drawable.ic_user);
                vp.setVisibility(View.GONE);
                headerlayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                content_Post.setVisibility(View.GONE);
                fragment = new Recent_Chat();
                recent_chat = (Recent_Chat) fragment;
                recent_chat.refreshRecentChat(Wall.this);
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    FragmentTransaction mFragmentTransaction = getFragmentManager()
                            .beginTransaction();

                    mFragmentTransaction.addToBackStack(null);

                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wall.this, EventsMain.class);
                startActivity(intent);
                finish();
            }
        });
        serrch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                heading_header.setVisibility(View.GONE);

                if (ivhome.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_home_active).getConstantState())) {
                    helper.backbutton = "1";
                }
                if (ivsearch.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_search_active).getConstantState())) {
                    helper.backbutton = "2";
                }
                if (ivchat.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_chat_active).getConstantState())) {
                    helper.backbutton = "4";
                }/*
                if (ivsetting.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.default_user).getConstantState())) {
                    helper.backbutton="5";
                }*/
                if (ivrides.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_rides_active).getConstantState())) {
                    helper.backbutton = "3";
                }
                ivrides.setImageResource(R.drawable.ic_rides_inactive);
                ivsearch.setImageResource(R.drawable.ic_search_active);
                ivchat.setImageResource(R.drawable.ic_chat);
                ivhome.setImageResource(R.drawable.ic_home);
                //  ivsetting.setImageResource(R.drawable.ic_user);
                vp.setVisibility(View.GONE);
                headerlayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                content_Post.setVisibility(View.GONE);

                ArrayList<NetWorkUserList> netWorkUserLists = new ArrayList<>();
                Search f = Search.newInstance(netWorkUserLists);
                fragment = new Search();
                objSearch = (Search) fragment;

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    FragmentTransaction mFragmentTransaction = getFragmentManager()
                            .beginTransaction();

                    mFragmentTransaction.addToBackStack(null);

                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });
        ivrides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                if (ivhome.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_home_active).getConstantState())) {
                    helper.backbutton = "1";
                }
                if (ivsearch.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_search_active).getConstantState())) {
                    helper.backbutton = "2";
                }
                if (ivchat.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_chat_active).getConstantState())) {
                    helper.backbutton = "4";
                }
               /* if (ivsetting.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.default_user).getConstantState())) {
                    helper.backbutton="5";
                }*/
                if (ivrides.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_rides_active).getConstantState())) {
                    helper.backbutton = "3";
                }
                helper.status = "";
                ivrides.setImageResource(R.drawable.ic_rides_active);
                ivsearch.setImageResource(R.drawable.ic_search);
                ivchat.setImageResource(R.drawable.ic_chat);
                ivhome.setImageResource(R.drawable.ic_home);
                //  ivsetting.setImageResource(R.drawable.ic_user);
                vp.setVisibility(View.GONE);
                headerlayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                content_Post.setVisibility(View.GONE);
                fragment = new RidesFragmnts();

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    FragmentTransaction mFragmentTransaction = getFragmentManager()
                            .beginTransaction();

                    mFragmentTransaction.addToBackStack(null);

                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                heading_header.setVisibility(View.GONE);

                if (ivhome.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_home_active).getConstantState())) {
                    helper.backbutton = "1";
                }
                if (ivsearch.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_search_active).getConstantState())) {
                    helper.backbutton = "2";
                }
                if (ivchat.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_chat_active).getConstantState())) {
                    helper.backbutton = "4";
                }
               /* if (ivsetting.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.default_user).getConstantState())) {
                    helper.backbutton="5";
                }*/
                if (ivrides.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.ic_rides_active).getConstantState())) {
                    helper.backbutton = "3";
                }
                ivrides.setImageResource(R.drawable.ic_rides_inactive);
                ivsearch.setImageResource(R.drawable.ic_search);
                ivchat.setImageResource(R.drawable.ic_chat);
                ivhome.setImageResource(R.drawable.ic_home);
                //  ivsetting.setImageResource(R.drawable.ic_user_active);
                vp.setVisibility(View.GONE);
                headerlayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                content_Post.setVisibility(View.GONE);

                fragment = new MyProfile();
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    FragmentTransaction mFragmentTransaction = getFragmentManager()
                            .beginTransaction();
                    mFragmentTransaction.addToBackStack(null);

                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                heading_header.setVisibility(View.GONE);

                helper.postType = "2";
                if (Build.VERSION.SDK_INT >= 23) {
                    if (gpsTracker.checkPermission()) {
                        if (gpsTracker.checkPermissionCamera()) {
                            final Dialog dialog = new Dialog(Wall.this, R.style.DialogSlideAnim);
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
                                    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                                    }
                                    dialog.dismiss();
                       /* Intent intent = new Intent();
                        intent.setType("video*//*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
                        dialog.dismiss();*/
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
                        } else {
                            gpsTracker.requestPermissionCamera();
                        }
                    } else {
                        ActivityCompat.requestPermissions(Wall.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);

                    }
                } else {


                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    helper.postType = "1";
                    if (gpsTracker.checkPermission()) {
                        if (gpsTracker.checkPermissionCamera()) {
                            final Dialog dialog = new Dialog(Wall.this, R.style.DialogSlideAnim);
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
                                    Crop.pickImage(Wall.this);
                                    dialog.dismiss();

                                   /* Intent intent = new Intent();
                                    intent.setType("image*//*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_RESULTS);
                                    frameLayout.setVisibility(View.GONE);
                                    content_Post.setVisibility(View.VISIBLE);
                                    dialog.dismiss();*/

                                }
                            });

                        } else {
                            gpsTracker.requestPermissionCamera();
                        }
                    } else {
                        ActivityCompat.requestPermissions(Wall.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);

                    }
                } else {
                }


            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void Showbadgecount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        // hideProgDialog();

                        JSONObject obj = null;
                        Log.d("Showbadgecount", response.toString());

                        try {
                            obj = new JSONObject(response.toString());
                            String chatcount = obj.getString("chatCount");

                            if (chatcount.equals("0")) {
                                txt_chatcount.setVisibility(View.GONE);
                            } else {
                                txt_chatcount.setVisibility(View.VISIBLE);
                                txt_chatcount.setText(chatcount);
                            }


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

                params.put("action", "Showbadgecount");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                Log.d("Showfriendrequest", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Wall.this);
        requestQueue.add(stringRequest);


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
                Uri photoURI = FileProvider.getUriForFile(Wall.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");

                    if (gpsTracker.checkPermissionCamera()) {

                    } else {
                        gpsTracker.requestPermissionCamera();
                    }

                } else {

                    gpsTracker.requestPermission();
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }

                break;

            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local camera .");
                } else {
                    gpsTracker.requestPermissionCamera();
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");

                    if (gpsTracker.checkPermissionCamera()) {

                    } else {
                        gpsTracker.requestPermissionCamera();
                    }

                } else {
                    gpsTracker.requestPermission();
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            File file = new File(imageUri.getPath());
            try {
                InputStream ims = new FileInputStream(file);

                bitmap = BitmapFactory.decodeStream(ims);
                bitmap = rotateImageIfRequired(Wall.this, bitmap, imageUri);
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                beginCrop(getImageUri(Wall.this, bitmap), w, h);

            } catch (FileNotFoundException e) {
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(Wall.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }


        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            int w = 500, h = 500;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                w = bitmap.getWidth();
                h = bitmap.getHeight();
                Log.d("hight", "hingt" + h + "width" + w);


            } catch (IOException e) {
                e.printStackTrace();
            }


            beginCrop(data.getData(), w, h);
        } else if (requestCode == REQUEST_CODE_HIGH_QUALITY_IMAGE && resultCode == RESULT_OK) {
            //image.setImageURI(mHighQualityImageUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mHighQualityImageUri);
                // beginCrop(getImageUri(Wall.this, bitmap));
            } catch (Exception e) {
                //handle exception
            }
           /* sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));*/
          /*  bitmap = (Bitmap) data.getExtras().get("data");
             bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
            //  beginCrop(getImageUri(Wall.this, bitmap));*/
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            Log.d("imagehight", w + "dgchj" + h);
            // loadData();
            Log.d("data", String.valueOf(getImageUri(Wall.this, bitmap)));


        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {

            Uri selectedImageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                // ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                bitmap = rotateImageIfRequired(Wall.this, bitmap, selectedImageUri);
                resultView.setImageBitmap(bitmap);
                vp.setVisibility(View.VISIBLE);
                headerlayout.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                content_Post.setVisibility(View.VISIBLE);
                // bitmap = Bitmap.createScaledBitmap(bitmap, 642, 642, true);
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                Log.d("hight", "hingt" + h + "width" + w);
                //  if (w > 500 & h > 500) {
                Log.d("width", String.valueOf(w));
                loadData("");

            } catch (IOException e) {
                e.printStackTrace();
            }


            // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (null != selectedImageUri) {
                // Get the path from the Uri
                String path = getPathFromURI(selectedImageUri);

                // Set the image in ImageView

                // bitmap = getBitmapFromURL(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

            }
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == -1) {
            Uri videoUri = data.getData();
            selectedPath = getPath(videoUri);
            bitmap = ThumbnailUtils.createVideoThumbnail(selectedPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            //  bitmap = Bitmap.createScaledBitmap(bitmap, 640, 640, false);
            textPath = selectedPath;
            tempFile = FileUtils.saveTempFile(System.currentTimeMillis() + "video.mp4", Wall.this, Uri.fromFile(new File(selectedPath)));

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
                thumb.compress(Bitmap.CompressFormat.JPEG, 80, out);
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
                    Toast.makeText(Wall.this, "Please select Video file.", Toast.LENGTH_SHORT).show();
                } else {

                    Log.e("sizeeeeeeeeeeeee", tempFile.length() + "");
                    beginCrop(getImageUri(Wall.this, bitmap), bitmap.getWidth(), bitmap.getHeight());
/*
                    if (tempFile.length() < 2394767) {
                        textPath = tempFile.getAbsolutePath();
                        selectedThumbPath = file.getAbsolutePath();
                        new UploadFileToServer("").execute();
                    } else {
                        //new VideoCompressor().execute();
                        new UploadFileToServer("").execute();
                    }*/
                }
            } else {
                Toast.makeText(Wall.this, "Please select Video file.", Toast.LENGTH_SHORT).show();
            }

        }
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                bitmap = ThumbnailUtils.createVideoThumbnail(selectedPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                //  bitmap = Bitmap.createScaledBitmap(bitmap, 640, 640, false);

                textPath = selectedPath;
                tempFile = FileUtils.saveTempFile(System.currentTimeMillis() + "video.mp4", Wall.this, Uri.fromFile(new File(selectedPath)));
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(tempFile.getAbsolutePath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

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
                    thumb.compress(Bitmap.CompressFormat.JPEG, 640, out);
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
                        Toast.makeText(Wall.this, "Please select Video file.", Toast.LENGTH_SHORT).show();
                    } else {

                        Log.e("sizeeeeeeeeeeeee", tempFile.length() + "");
                        //  showCustomDialog(bitmap);
                        beginCrop(getImageUri(Wall.this, bitmap), bitmap.getWidth(), bitmap.getHeight());

                      /*  if (tempFile.length() < 2394767) {
                            textPath = tempFile.getAbsolutePath();
                            selectedThumbPath = file.getAbsolutePath();
                            new UploadFileToServer().execute();
                        } else {
                            // new VideoCompressor().execute();
                            new UploadFileToServer().execute();
                        }*/
                    }
                } else {
                    Toast.makeText(Wall.this, "Please select Video file.", Toast.LENGTH_SHORT).show();
                }


            }
        }

    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri
            selectedImage) throws IOException {
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
                            this,
                            "Failed to create directory "
                                    + outputDir.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    outputDir = null;
                }
        }
        return outputDir;
    }

    public void showCustomDialog(final Bitmap img) {
        m_dialog = new Dialog(Wall.this, R.style.Dialog_No_Border);
        m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater m_inflater = LayoutInflater.from(Wall.this);
        View m_view = m_inflater.inflate(R.layout.custom_dialogcaption, null);
        m_llMain = (LinearLayout) m_view.findViewById(R.id.cadllMain);
        //Change the background of the dialog according to the layout.


        m_llMain.setBackgroundResource(R.drawable.btn_style_border_roundcorner);

        final TextView addcomment = (TextView) m_view.findViewById(R.id.addcomment);
        final EditText edt_comment = (EditText) m_view.findViewById(R.id.edt_comment);
        Button m_btnOk = (Button) m_view.findViewById(R.id.cadbtnOk);
        SquareImageView tempshow = (SquareImageView) m_view.findViewById(R.id.tempshow);

        BitmapDrawable ob = new BitmapDrawable(getResources(), img);
        tempshow.setBackgroundDrawable(ob);
        m_btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = edt_comment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(Wall.this, "Please add comment for this item", Toast.LENGTH_SHORT).show();
                } else {
                    new UploadFileToServer("").execute();
                }
                m_dialog.dismiss();


            }
        });


        m_dialog.setContentView(m_view);
        m_dialog.show();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
            dialog = new Dialog(Wall.this);
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
            new UploadFileToServer("").execute();
        }
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        private Dialog dialog;
        private ProgressBar progressBar;
        private ProgressBar progressBar1;
        private TextView txtPercentage;
        String postTitle;


        public UploadFileToServer(String comment) {
            this.postTitle = comment;

        }

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            dialog = new Dialog(Wall.this);
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
            // progressBar.setVisibility(View.VISIBLE);
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
            return uploadFile(textPath, PrefMangr.getInstance().getUserId(), imageBytes);
            //return uploadFile(selectedPath, PrefMangr.getInstance().getUserId(), imageBytes);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String selectedVideoPath, String users_id,
                                  byte[] selectThumbPath) {
            String responseString = null;
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 150000);  // allow 5 seconds to create the server connection
            HttpConnectionParams.setSoTimeout(httpParameters, 150000);
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
                entity.addPart("video", new FileBody(sourceFile));
                entity.addPart("user_id", new StringBody(PrefMangr.getInstance().getUserId(), "text/plain", Charset.forName("UTF-8")));
                entity.addPart("action", new StringBody("Postnews", "text/plain", Charset.forName("UTF-8")));
                entity.addPart("video_thumb_nail", new ByteArrayBody(selectThumbPath, "image/jpeg", "test1.jpg"));
                entity.addPart("post_title", new StringBody(postTitle, "text/plain", Charset.forName("UTF-8")));

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
            vp.setVisibility(View.VISIBLE);
            headerlayout.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);

            frameLayout.setVisibility(View.GONE);
            content_Post.setVisibility(View.VISIBLE);
            fragment = new PostsWall();
            postsWall = (PostsWall) fragment;

            postsWall.page = 0;
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_Post, fragment).commit();
                FragmentTransaction mFragmentTransaction = getFragmentManager()
                        .beginTransaction();

                mFragmentTransaction.addToBackStack(null);
            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }

            dialog.dismiss();

            try {
                //	if(flagValue==1){


                if (result.contains("Post added successsfullynj")) {

                    AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(Wall.this, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(Wall.this);
                    }
                    //alert.setTitle("Success");
                    alert.setMessage("Video Upload Successfully");


                    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog1, int arg1) {
                            // TODO Auto-generated method stub

                            startActivity(new Intent(Wall.this, Wall.class));

                            dialog1.dismiss();
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
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
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
                progress_dialog = new ProgressDialog(Wall.this, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(Wall.this);
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

    private void beginCrop(Uri source, int w, int h) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        // Crop.of(source, destination).asSquare().start(this);
        Crop.of(source, destination).withMaxSize(w, h).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            //  resultView.setImageURI(Crop.getOutput(result));
            String comment = result.getStringExtra("comment");
            Log.d("comment", comment);
            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }

            resultView.setImageBitmap(bitmap);
            vp.setVisibility(View.VISIBLE);
            headerlayout.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            content_Post.setVisibility(View.VISIBLE);
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            Log.d("width", String.valueOf(w));

            if (helper.postType.equals("1")) {
                loadData(comment);
            }
            if (helper.postType.equals("2")) {
                new UploadFileToServer(comment).execute();
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private AsyncLoadData asyncLoad;

    private void loadData(String comment) {
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData(comment);
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
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        String fanlist;

        public MyPagerAdapter(FragmentManager fm, String s) {
            super(fm);
            this.fanlist = s;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Log.d("fanlist", fanlist);
                    // Fans.repostUser(Wall.this);
                    Fans one = new Fans();
                    fans = (Fans) one;
                    Fans2 one2 = new Fans2();
                    if (fanlist.equals("1")) {
                        return one2;
                    }
                    if (fanlist.equals("0")) {
                        return one;
                    }

                case 1:

                    Clubs two = new Clubs();
                    clubs = (Clubs) two;

                    return two;
                case 2:
                    Page3 three = new Page3();
                    page3 = (Page3) three;
                    return three;
                default:
                    return null;
            }
        }
    }
//................................//
    private class AsyncLoadData extends AsyncTask<String, Integer, Void> {
        boolean flag = false;
        String email, mobile_number, name;
        private Dialog dialog;
        private ProgressBar progressBar;
        private ProgressBar progressBar1;
        private TextView txtPercentage;
        String postTitle;

        public AsyncLoadData(String comment) {

            postTitle = comment;
        }

// ndxixnhseixxidhx......
        @Override
        protected Void doInBackground(String... strings) {

            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
            progress_dialog_msg = "loading...";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();
            LoadApi api = new LoadApi();
            try {
                jobj = api.Action_profileSetting_Image("Postnews", PrefMangr.getInstance().getUserId(), imageBytes, postTitle);
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
            vp.setVisibility(View.VISIBLE);
            headerlayout.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);

            frameLayout.setVisibility(View.GONE);
            content_Post.setVisibility(View.VISIBLE);
            fragment = new PostsWall();
            postsWall = (PostsWall) fragment;

            postsWall.page = 0;
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_Post, fragment).commit();
                FragmentTransaction mFragmentTransaction = getFragmentManager()
                        .beginTransaction();

                mFragmentTransaction.addToBackStack(null);
            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }

            hideProgDialog();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        if (helper.backbutton.equals("4")) {
            backPressedToExitOnce = false;
            ivrides.setImageResource(R.drawable.ic_rides_inactive);
            ivsearch.setImageResource(R.drawable.ic_search);
            ivchat.setImageResource(R.drawable.ic_chat_active);
            ivhome.setImageResource(R.drawable.ic_home);
            // ivsetting.setImageResource(R.drawable.ic_user);
            vp.setVisibility(View.GONE);
            headerlayout.setVisibility(View.GONE);
            helper.backbutton = "4";
            frameLayout.setVisibility(View.VISIBLE);
            content_Post.setVisibility(View.GONE);
            fragment = new Recent_Chat();
            recent_chat = (Recent_Chat) fragment;
            recent_chat.refreshRecentChat(Wall.this);
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                FragmentTransaction mFragmentTransaction = getFragmentManager()
                        .beginTransaction();

                mFragmentTransaction.addToBackStack(null);

            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
        }
        if (helper.backbutton.equals("3")) {
            backPressedToExitOnce = false;
            helper.status = "";
            ivrides.setImageResource(R.drawable.ic_rides_active);
            ivsearch.setImageResource(R.drawable.ic_search);
            ivchat.setImageResource(R.drawable.ic_chat);
            ivhome.setImageResource(R.drawable.ic_home);
            //  ivsetting.setImageResource(R.drawable.ic_user);
            vp.setVisibility(View.GONE);
            headerlayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            content_Post.setVisibility(View.GONE);
            fragment = new RidesFragmnts();
            helper.backbutton = "3";
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                FragmentTransaction mFragmentTransaction = getFragmentManager()
                        .beginTransaction();

                mFragmentTransaction.addToBackStack(null);

            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
        }

        if (helper.backbutton.equals("2")) {
            backPressedToExitOnce = false;
            ivrides.setImageResource(R.drawable.ic_rides_inactive);
            ivsearch.setImageResource(R.drawable.ic_search_active);
            ivchat.setImageResource(R.drawable.ic_chat);
            ivhome.setImageResource(R.drawable.ic_home);
            //  ivsetting.setImageResource(R.drawable.ic_user);
            vp.setVisibility(View.GONE);
            headerlayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            content_Post.setVisibility(View.GONE);
            helper.backbutton = "2";
            ArrayList<NetWorkUserList> netWorkUserLists = new ArrayList<>();
            Search f = Search.newInstance(netWorkUserLists);
            fragment = new Search();
            objSearch = (Search) fragment;


        }

        if (helper.backbutton.equals("5")) {
            backPressedToExitOnce = false;
            ivrides.setImageResource(R.drawable.ic_rides_inactive);
            ivsearch.setImageResource(R.drawable.ic_search);
            ivchat.setImageResource(R.drawable.ic_chat);
            ivhome.setImageResource(R.drawable.ic_home);
            //  ivsetting.setImageResource(R.drawable.ic_user_active);
            vp.setVisibility(View.GONE);
            headerlayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            content_Post.setVisibility(View.GONE);
            helper.backbutton = "5";
            fragment = new MyProfile();
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                FragmentTransaction mFragmentTransaction = getFragmentManager()
                        .beginTransaction();
                mFragmentTransaction.addToBackStack(null);

            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }


        }

        if (helper.backbutton.equals("1")) {
            if (backPressedToExitOnce) {


                AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(Wall.this, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(Wall.this);
                }

                alert.setMessage("You want to exit from UnionRides?");


                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog1, int arg1) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                        startActivity(intent);
                        finish();
                        System.exit(0);
                        dialog1.dismiss();
                    }

                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog1, int arg1) {
                        // TODO Auto-generated method stub


                        dialog1.dismiss();
                    }

                });
                try {
                    Dialog dialog1 = alert.create();
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                this.backPressedToExitOnce = true;
                ivrides.setImageResource(R.drawable.ic_rides_inactive);
                ivsearch.setImageResource(R.drawable.ic_search);
                ivchat.setImageResource(R.drawable.ic_chat);
                ivhome.setImageResource(R.drawable.ic_home_active);
                // ivsetting.setImageResource(R.drawable.ic_user);
                vp.setVisibility(View.VISIBLE);
                headerlayout.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                content_Post.setVisibility(View.VISIBLE);
                // Posts.repost(Wall.this, 0);
                ArrayList<UserPosts> userPostsArrayList = new ArrayList<>();
                Posts f = Posts.newInstance(userPostsArrayList);
                fragment = new PostsWall();
                postsWall = (PostsWall) fragment;
                swipeRefreshLayout.setRefreshing(true);
                postsWall.page = 0;
                postsWall.showAllPostsscroll(Wall.this, 0);
                helper.backbutton = "1";
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_Post, fragment).commit();
                    FragmentTransaction mFragmentTransaction = getFragmentManager()
                            .beginTransaction();

                    mFragmentTransaction.addToBackStack(null);
                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //  backPressedToExitOnce = false;
                    }
                }, 2000);
            }

        }
    }

}