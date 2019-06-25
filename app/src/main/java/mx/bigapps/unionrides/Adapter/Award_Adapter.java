package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import mx.bigapps.unionrides.Model.Award_List_Entity;
import mx.bigapps.unionrides.R;

/**
 * Created by seemtech2 on 05-10-2017.
 */

public class Award_Adapter extends RecyclerView.Adapter<Award_Adapter.ItemViewHolder> {

    Context context;
    Activity activity;

    ArrayList<Award_List_Entity> employer_list = new ArrayList<Award_List_Entity>();
    int mCurrentPlayingPosition = -1;
    ProgressBar progressBar;

    public Award_Adapter(Context context, ArrayList<Award_List_Entity> employer_list) {
        this.context = context;
        this.employer_list = employer_list;


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        TextView award_date_txt, award_name_txt;

        public ItemViewHolder(View itemView) {
            super(itemView);
            award_date_txt = (TextView) itemView.findViewById(R.id.award_date_txt);
            award_name_txt = (TextView) itemView.findViewById(R.id.award_name_txt);
        }
    }

    @Override
    public void onBindViewHolder(final Award_Adapter.ItemViewHolder itemViewHolder, final int position) {

        // itemViewHolder.image.setImageBitmap(employer_list.get(position));
        itemViewHolder.award_date_txt.setText(employer_list.get(position).getAward_date());
        itemViewHolder.award_name_txt.setText(employer_list.get(position).getAward_name());

    }

    @Override
    public int getItemCount() {
        return employer_list.size();
    }

    @Override
    public Award_Adapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_award, parent, false);
        Award_Adapter.ItemViewHolder viewHolder = new Award_Adapter.ItemViewHolder(v);

        // viewHolder.image.setImageBitmap(employer_list.get());


        return viewHolder;
    }

}
