package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mx.bigapps.unionrides.Activity.StretchVideoView;
import mx.bigapps.unionrides.R;


/**
 * Created by seemtech2 on 05-10-2017.
 */

public class Video_Adapter extends RecyclerView.Adapter<Video_Adapter.ItemViewHolder> {

    Context context;
    Activity activity;

    ArrayList<Uri> employer_list = new ArrayList<Uri>();
    int mCurrentPlayingPosition = -1;


    public Video_Adapter(Context context, ArrayList<Uri> employer_list) {
        this.context = context;
        this.employer_list = employer_list;


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView custom_employee_title, custom_employee_price, custom_employee_status;
        RelativeLayout container2_offer;
        StretchVideoView videoView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            videoView = (StretchVideoView) itemView.findViewById(R.id.videos);
        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {
        itemViewHolder.videoView.setVideoURI(employer_list.get(position));
        itemViewHolder.videoView.start();

    }

    @Override
    public int getItemCount() {
        return employer_list.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.videolist, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);

        // Uri uri = Uri.parse("http://abhiandroid-8fb4.kxcdn.com/ui/wp-content/uploads/2016/04/videoviewtestingvideo.mp4");


        return viewHolder;
    }

}
