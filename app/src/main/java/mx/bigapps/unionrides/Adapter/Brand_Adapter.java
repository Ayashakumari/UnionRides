package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mx.bigapps.unionrides.Model.Car_Brand_Entity;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.helper;

/**
 * Created by dell on 11/20/2017.
 */

public class Brand_Adapter extends RecyclerView.Adapter<Brand_Adapter.ItemViewHolder> {

    Context context;
    Activity activity;

    ArrayList<Car_Brand_Entity> employer_list = new ArrayList<Car_Brand_Entity>();
    int mCurrentPlayingPosition = -1;




    public Brand_Adapter(Context context, ArrayList<Car_Brand_Entity> employer_list) {
        this.context = context;
        this.employer_list = employer_list;


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView brand_name;
        public ItemViewHolder(View itemView) {
            super(itemView);
           brand_name= (TextView) itemView.findViewById(R.id.brand_txt);
        }
    }

    @Override
    public void onBindViewHolder(final Brand_Adapter.ItemViewHolder itemViewHolder, final int position) {

        itemViewHolder.brand_name.setText(employer_list.get(position).getBrand_name());

        itemViewHolder.brand_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.brand_name=employer_list.get(position).getBrand_name();
                ((Activity)context).finish();
            }
        });


    }

    @Override
    public int getItemCount() {
        return employer_list.size();
    }

    @Override
    public Brand_Adapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_brand, parent, false);
        Brand_Adapter.ItemViewHolder viewHolder = new Brand_Adapter.ItemViewHolder(v);
        return viewHolder;
    }

}

