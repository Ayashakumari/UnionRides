package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.bigapps.unionrides.Activity.PublicProfile;
import mx.bigapps.unionrides.Activity.Wall;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.UserList;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.helper;

/**
 * Created by seemtech2 on 05-10-2017.
 */

public class Photo_Adapter extends RecyclerView.Adapter<Photo_Adapter.ItemViewHolder> {

    Context context;
    Activity activity;

    ArrayList<UserList> employer_list = new ArrayList<UserList>();
    int mCurrentPlayingPosition = -1;


    public Photo_Adapter(Context context, ArrayList<UserList> employer_list) {
        this.context = context;
        this.employer_list = employer_list;


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView fanname;
        CircleImageView profileimg;

        public ItemViewHolder(View itemView) {
            super(itemView);
            fanname = (TextView) itemView.findViewById(R.id.fanname);
            profileimg = (CircleImageView) itemView.findViewById(R.id.profile2_img);

        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {
        itemViewHolder.fanname.setText(employer_list.get(position).getNickname().toString());
        try {
            String image = "" + employer_list.get(position);
            if (!image.equals("")) {
                Picasso.with(context)
                        .load(employer_list.get(position).getProfileimage())
                        .noFade()
                        .error(R.drawable.userimages)
                        .into(itemViewHolder.profileimg);
                //  UrlImageViewHelper.setUrlDrawable(holder.find_people_img,find_peoplelist.get(position).getImage());
            } else {
                itemViewHolder.profileimg.setBackgroundResource(R.drawable.userimages);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        itemViewHolder.profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employer_list.get(position).getUser_id().equals(PrefMangr.getInstance().getUserId())){
                    Intent intent = new Intent(context, Wall.class);
                intent.putExtra("flag",5);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intent);
                }
                else {

                    Intent intent = new Intent(context, PublicProfile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    helper.user_id = employer_list.get(position).getUser_id();
                    helper.back_status = "Posts";
                    context.startActivity(intent);
                   /* Intent intent = new Intent(context, PublicProfile.class);
                    helper.user_id = employer_list.get(position).getUser_id();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intent);*/
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return employer_list.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.peoplelist, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);

        return viewHolder;
    }

}
