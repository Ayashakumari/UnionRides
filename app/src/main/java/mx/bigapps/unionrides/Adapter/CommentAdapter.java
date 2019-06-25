package mx.bigapps.unionrides.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mx.bigapps.unionrides.Model.Cover_Photo_List;
import mx.bigapps.unionrides.R;

/**
 * Created by we on 10/18/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    ArrayList<Cover_Photo_List> employer_list;
    Context context;
    String comment;


    public CommentAdapter(Context context, ArrayList<Cover_Photo_List> employer_list) {
        this.context = context;

        this.employer_list = employer_list;
    }


    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentlist, parent, false);
        CommentAdapter.MyViewHolder vh = new CommentAdapter.MyViewHolder(v);
        return vh;


    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        Cover_Photo_List listPos = employer_list.get(position);
        holder.nice_car.setText(listPos.getComments());
        holder.nameUser.setText(listPos.getComment_id());


    }

    @Override
    public int getItemCount() {
        return employer_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameUser, nice_car, two_minutes;
        TextView comments_user;
        ImageView user_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            nameUser = (TextView) itemView.findViewById(R.id.allen_kneth);
            nice_car = (TextView) itemView.findViewById(R.id.nice_car);
            two_minutes = (TextView) itemView.findViewById(R.id.two_minutes);
            user_image = (ImageView) itemView.findViewById(R.id.user_image);

            comments_user = (TextView) itemView.findViewById(R.id.comments_user);


        }
    }
}
