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
 * Created by dell on 11/16/2017.
 */

public class Car_Model_Adapter extends RecyclerView.Adapter<Car_Model_Adapter.ItemViewHolder> {

    Context context;
    Activity activity;

    ArrayList<Car_Brand_Entity> employer_list = new ArrayList<Car_Brand_Entity>();
    int mCurrentPlayingPosition = -1;




    public Car_Model_Adapter(Context context, ArrayList<Car_Brand_Entity> employer_list) {
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
    public void onBindViewHolder(final Car_Model_Adapter.ItemViewHolder itemViewHolder, final int position) {

itemViewHolder.brand_name.setText(employer_list.get(position).getModel());

        itemViewHolder.brand_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.model_name=employer_list.get(position).getModel();
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return employer_list.size();
    }

    @Override
    public Car_Model_Adapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_brand, parent, false);
        Car_Model_Adapter.ItemViewHolder viewHolder = new Car_Model_Adapter.ItemViewHolder(v);
        return viewHolder;
    }

}

