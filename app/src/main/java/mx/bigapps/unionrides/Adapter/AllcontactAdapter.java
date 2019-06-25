package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.bigapps.unionrides.Activity.Chat_window;
import mx.bigapps.unionrides.Model.NetWorkUserList;
import mx.bigapps.unionrides.R;

/**
 * Created by seemtech2 on 05-10-2017.
 */

public class AllcontactAdapter extends RecyclerView.Adapter<AllcontactAdapter.ItemViewHolder> {

    Context context;
    Activity activity;

    ArrayList<NetWorkUserList> employer_list = new ArrayList<NetWorkUserList>();
    int mCurrentPlayingPosition = -1;


    public AllcontactAdapter(Context context, ArrayList<NetWorkUserList> employer_list) {
        this.context = context;
        this.employer_list = employer_list;


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvname, custom_employee_price, custom_employee_status;
        RelativeLayout container2_offer;
        Button btnaccpt;
        CircleImageView profile_image;
        ImageView clubimage;
        LinearLayout friendLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            btnaccpt = (Button) itemView.findViewById(R.id.btaccept);
            tvname = (TextView) itemView.findViewById(R.id.tvname);
            profile_image = (CircleImageView) itemView.findViewById(R.id.profile_image);
            clubimage = (ImageView) itemView.findViewById(R.id.clubimage);
            friendLayout = (LinearLayout) itemView.findViewById(R.id.friendlayout);

        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder viewHolder, final int position) {

        viewHolder.tvname.setText(employer_list.get(position).getFullname());
        try {
            String image = "" + employer_list.get(position).getProfileimage();
            if (!image.equals("")) {
                Picasso.with(context)
                        .load(employer_list.get(position).getProfileimage())
                        .noFade()
                        .error(R.drawable.loadinguni)
                        .into(viewHolder.profile_image);
                //  UrlImageViewHelper.setUrlDrawable(holder.find_people_img,find_peoplelist.get(position).getImage());
            } else {
                viewHolder.profile_image.setBackgroundResource(R.drawable.loadinguni);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (employer_list.get(position).getType().equals("rider")) {
            viewHolder.clubimage.setVisibility(View.GONE);
        }
        if (employer_list.get(position).getType().equals("rider")) {
            viewHolder.clubimage.setVisibility(View.GONE);

        }

        if (employer_list.get(position).getType().equals("pages")) {
            viewHolder.clubimage.setImageResource(R.drawable.pages);
            viewHolder.clubimage.setVisibility(View.VISIBLE);
        }
        if (employer_list.get(position).getType().equals("club")) {
            viewHolder.clubimage.setImageResource(R.drawable.club);
            viewHolder.clubimage.setVisibility(View.VISIBLE);
        }
        viewHolder.friendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(context, Chat_window.class);
                i2.putExtra("firstname", employer_list.get(position).getFullname());
                i2.putExtra("friend_id", employer_list.get(position).getUser_id());
                // i2.putExtra("project_id", recent_chatlist.get(i).getProject_id());
                i2.putExtra("status", "contactchat");
                context.startActivity(i2);
            }
        });

    }


    @Override
    public int getItemCount() {
        return employer_list.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.allcontactadapter, parent, false);
        final ItemViewHolder viewHolder = new ItemViewHolder(v);

        return viewHolder;
    }

}
