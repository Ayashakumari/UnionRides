package mx.bigapps.unionrides.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import mx.bigapps.unionrides.Fragment.Allevnts;
import mx.bigapps.unionrides.Fragment.Myevents;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.ViewFindUtils;

public class EventsMain extends AppCompatActivity {
    ImageView camera, video, events, header_logo;
    TextView header_txt;
    LinearLayout home, serrch, rides, chat, setting;
    ImageView ivhome, ivsearch, ivrides, ivchat, ivsetting;
    SlidingTabLayout tl_2;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "ALL", "MY EVENTS"
    };
    private MyPagerAdapter mAdapter;
    RelativeLayout fanslayout;
    String event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_main);

        tl_2 = (SlidingTabLayout) findViewById(R.id.tl_2);
        ivhome = (ImageView) findViewById(R.id.home_img);
        ivhome.setImageResource(R.drawable.ic_home_active);
        ivsearch = (ImageView) findViewById(R.id.search_img);
        ivrides = (ImageView) findViewById(R.id.rides);
        ivchat = (ImageView) findViewById(R.id.chat);
        ivsetting = (ImageView) findViewById(R.id.setting);
        ivsetting.setImageResource(R.drawable.default_user);
        home = (LinearLayout) findViewById(R.id.toolbar_home);
        serrch = (LinearLayout) findViewById(R.id.toolbar_search);
        rides = (LinearLayout) findViewById(R.id.toolbar_rides);
        // chat = (LinearLayout) findViewById(R.id.toolbar_chat);
        setting = (LinearLayout) findViewById(R.id.toolbar_setting);

        camera = (ImageView) findViewById(R.id.header_camera);
        camera.setImageResource(R.drawable.back);
        video = (ImageView) findViewById(R.id.header_video);
        video.setVisibility(View.GONE);
        header_txt = (TextView) findViewById(R.id.header_txt);
        header_txt.setText("Events");
        header_txt.setVisibility(View.VISIBLE);
        header_logo = (ImageView) findViewById(R.id.header_logo);
        header_logo.setVisibility(View.GONE);
        events = (ImageView) findViewById(R.id.header_event);
        events.setImageResource(R.drawable.header_add);
        Intent intent = getIntent();
        event = intent.getStringExtra("event");
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventsMain.this, Wall.class));
            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventsMain.this, Add_Events.class));
            }
        });
        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }
        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);
        SlidingTabLayout tabLayout_2 = ViewFindUtils.find(decorView, R.id.tl_2);
        tabLayout_2.setViewPager(vp);
        if (event != null) {
            vp.setCurrentItem(3);
            tabLayout_2.setCurrentTab(3);
        }
        tabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });


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
                    Allevnts one = new Allevnts();
                    return one;
                case 1:
                    Myevents two = new Myevents();
                    return two;
                default:
                    return null;
            }
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
        super.onBackPressed();
        startActivity(new Intent(EventsMain.this, Wall.class));
    }
}
