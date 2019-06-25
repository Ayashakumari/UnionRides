package mx.bigapps.unionrides.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mx.bigapps.unionrides.Adapter.EventsListAdapter;
import mx.bigapps.unionrides.R;

public class PublicProfileEvents extends AppCompatActivity {
    ImageView camera, video, events, header_ride;
    RecyclerView lvEventsList;
    ArrayList photolist;
    EventsListAdapter publicProfileEvents;
    LinearLayoutManager layoutManager;
    TextView events_txt, post_txt, txtrides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile_events);
        camera = (ImageView) findViewById(R.id.header_camera);
        camera.setImageResource(R.drawable.back);

        video = (ImageView) findViewById(R.id.header_video);
        video.setVisibility(View.GONE);
        events = (ImageView) findViewById(R.id.header_event);
        events.setVisibility(View.GONE);
        header_ride = (ImageView) findViewById(R.id.header_ride);
        events_txt = (TextView) findViewById(R.id.events_txt);
        post_txt = (TextView) findViewById(R.id.post_txt);
        txtrides = (TextView) findViewById(R.id.txtrides);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        header_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PublicProfileEvents.this, PublicProfile.class));
            }
        });
        txtrides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PublicProfileEvents.this, PublicProfile.class));
            }
        });
        events_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PublicProfileEvents.this, PublicProfileEvents.class));
            }
        });

        lvEventsList = (RecyclerView) findViewById(R.id.lvEventsList);
        layoutManager = new LinearLayoutManager(PublicProfileEvents.this);
        lvEventsList.setLayoutManager(layoutManager);
        photolist = new ArrayList();
        photolist.add("zdv");
        photolist.add("vdv");
        photolist.add("");
        photolist.add("");
        photolist.add("");

        publicProfileEvents = new EventsListAdapter(PublicProfileEvents.this, photolist);
        lvEventsList.setAdapter(publicProfileEvents);


    }
}
