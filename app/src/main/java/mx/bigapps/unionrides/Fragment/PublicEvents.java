package mx.bigapps.unionrides.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mx.bigapps.unionrides.Adapter.EventsListAdapter;
import mx.bigapps.unionrides.R;

public class PublicEvents extends Fragment {
    RecyclerView lvEventsList;
    ArrayList photolist;
    EventsListAdapter publicProfileEvents;
    LinearLayoutManager layoutManager;

    public PublicEvents() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_public_events, container, false);
        lvEventsList = (RecyclerView) view.findViewById(R.id.lvEventsList);
        layoutManager = new LinearLayoutManager(getActivity());
        lvEventsList.setLayoutManager(layoutManager);
        photolist = new ArrayList();
        photolist.add("zdv");
        photolist.add("vdv");
        photolist.add("");
        photolist.add("");
        photolist.add("");

        publicProfileEvents = new EventsListAdapter(getActivity(), photolist);
        lvEventsList.setAdapter(publicProfileEvents);
        lvEventsList.smoothScrollToPosition(photolist.size()-1);
        return view;
    }


}
