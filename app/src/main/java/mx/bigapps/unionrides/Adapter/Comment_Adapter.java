package mx.bigapps.unionrides.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mx.bigapps.unionrides.Model.Comments;
import mx.bigapps.unionrides.R;

/**
 * Created by dev on 01-11-2017.
 */

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.MyViewHolder> {
    ArrayList<Comments> personNames;
    Context context;

    public Comment_Adapter(Context context, ArrayList<Comments> personNames) {
        this.context = context;
        this.personNames = personNames;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_comment_recycle, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.comnttxt.setText(personNames.get(position).getComments());
        holder.user_name.setText(personNames.get(position).getFullname());
        holder.time.setText((personNames.get(position).getDatetime()));
        try {
            String image = "" + personNames.get(position);
            if (!image.equals("")) {
                Picasso.with(context)
                        .load(personNames.get(position).getProfileimage())
                        .noFade()
                        .error(R.drawable.userimages)
                        .into(holder.userImage);
                //  UrlImageViewHelper.setUrlDrawable(holder.find_people_img,find_peoplelist.get(position).getImage());
            } else {
                holder.userImage.setBackgroundResource(R.drawable.userimages);
            }
            holder.userImage.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return personNames.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView comnttxt, user_name, time;
        PorterShapeImageView userImage;


        public MyViewHolder(final View itemView) {
            super(itemView);
            comnttxt = (TextView) itemView.findViewById(R.id.comnttxt);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            time = (TextView) itemView.findViewById(R.id.time);
            userImage = (PorterShapeImageView) itemView.findViewById(R.id.commentimg);

        }
    }
}


