package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mx.bigapps.unionrides.R;


/**
 * Created by seemtech2 on 10-08-2017.
 */

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ItemViewHolder> {

    Context context;
    String text;
    int count = 0;
    int flag = 0;
    Activity activity;
    int value;
    ArrayList<String> storelist = new ArrayList<String>();


    public EventsListAdapter(Context context, ArrayList<String> storelist) {
        this.context = context;
        this.storelist = storelist;


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, postedby, date, time, location;
        LinearLayout lvStoreList;
        ImageView imgStore;

        public ItemViewHolder(View itemView) {
            super(itemView);


            lvStoreList = (LinearLayout) itemView.findViewById(R.id.lvStoreList);
            imgStore = (ImageView) itemView.findViewById(R.id.imgStore);
            eventName = (TextView) itemView.findViewById(R.id.eventName);
            postedby = (TextView) itemView.findViewById(R.id.postedby);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            location = (TextView) itemView.findViewById(R.id.location);


        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {


    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventsdetaillist, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return 10;
    }


}
