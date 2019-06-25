package mx.bigapps.unionrides.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import mx.bigapps.unionrides.Fragment.Awards;
import mx.bigapps.unionrides.Fragment.RidesPhotos;
import mx.bigapps.unionrides.Fragment.SpecRides;
import mx.bigapps.unionrides.Fragment.VideoRides;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.ViewFindUtils;
import mx.bigapps.unionrides.utils.helper;

public class Rides extends AppCompatActivity {

    TextView txtrides;
    ImageView camera, video,header_back;
    public static ImageView events,header_logo;
    LinearLayout home, serrch, rides, chat, setting;
    ImageView ivhome, ivsearch, ivrides, ivchat, ivsetting;
    SlidingTabLayout tl_2;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "PHOTOS", "VIDEOS", "SPECS", "AWARDS"
    };
    private MyPagerAdapter mAdapter;
    String Award;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);
        ivhome = (ImageView) findViewById(R.id.home_img);
        ivhome.setImageResource(R.drawable.navichome);
        ivsearch = (ImageView) findViewById(R.id.search_img);
        ivrides = (ImageView) findViewById(R.id.rides);
        ivrides.setImageResource(R.drawable.ic_rides_active);
        ivchat = (ImageView) findViewById(R.id.chat);
        ivsetting = (ImageView) findViewById(R.id.setting);
        home = (LinearLayout) findViewById(R.id.toolbar_home);
        serrch = (LinearLayout) findViewById(R.id.toolbar_search);
        rides = (LinearLayout) findViewById(R.id.toolbar_rides);
        //  chat = (LinearLayout) findViewById(R.id.toolbar_chat);
        setting = (LinearLayout) findViewById(R.id.toolbar_setting);

        header_back = (ImageView)findViewById(R.id.back);
        header_back.setImageResource(R.drawable.header_back_purple);
        header_back.setVisibility(View.VISIBLE);

        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtrides = (TextView) findViewById(R.id.txtrides);
        txtrides.setVisibility(View.VISIBLE);
        camera = (ImageView) findViewById(R.id.header_camera);
        camera.setImageResource(R.drawable.header_ride);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }


            });
        Intent intent = getIntent();
        Award = intent.getStringExtra("Award");
        video = (ImageView) findViewById(R.id.header_video);
        video.setVisibility(View.GONE);
        events = (ImageView) findViewById(R.id.header_event);
        events.setImageResource(R.drawable.header_add);
        events.setVisibility(View.VISIBLE);

        header_logo = (ImageView) findViewById(R.id.header_logo);
        header_logo.setImageResource(R.drawable.header_logo);
        header_logo.setVisibility(View.VISIBLE);


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Rides.this, Account_Setting.class));
            }
        });
        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }
        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());

        vp.setAdapter(mAdapter);
        if (helper.riderfrag.equals("0")) {
            vp.setCurrentItem(0);
        }
        if (helper.riderfrag.equals("1")) {
            vp.setCurrentItem(1);
        }
        if (helper.riderfrag.equals("2")) {
            vp.setCurrentItem(2);
        }
        if (helper.riderfrag.equals("3")) {
            vp.setCurrentItem(3);
        }
        SlidingTabLayout tabLayout_2 = ViewFindUtils.find(decorView, R.id.tl_2);
        tabLayout_2.setViewPager(vp);
        tabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {


            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                helper.riderStatus = String.valueOf(position);
                Log.d("riderp", helper.riderStatus);

            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.

            }
        });


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
       /* super.onBackPressed();
        if (helper.status.equals("otherprofile_ride")) {
            Intent intent = new Intent(Rides.this, PublicProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("flag", 1);
        } else if (helper.status.equals("myprofile_ride")) {
            Intent intent = new Intent(Rides.this, Wall.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("flag", 5);
            startActivity(intent);

        } else if (helper.backbutton.equals("1")) {
            Intent intent = new Intent(Rides.this, Wall.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("flag", 3);
            startActivity(intent);
        } else if (helper.backbutton.equals("2")) {
            Intent intent = new Intent(Rides.this, Wall.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("flag", 2);
            startActivity(intent);
        } else if (helper.backbutton.equals("5")) {
            Intent intent = new Intent(Rides.this, Wall.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("flag", 5);
            startActivity(intent);
        } else {
            Intent intent = new Intent(Rides.this, Wall.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("flag", 3);
            startActivity(intent);
        }*/
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
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
                    RidesPhotos one = new RidesPhotos();
                    Log.d("rider1", "1");
                    return one;
                case 1:
                    VideoRides two = new VideoRides();
                    Log.d("rider1", "2");
                    return two;
                case 2:
                    SpecRides three = new SpecRides();
                    Log.d("rider1", "3");
                    return three;
                case 3:
                    Awards four = new Awards();
                    Log.d("rider1", "4");
                    return four;
                default:
                    return null;
            }
        }
    }


}