package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

import mx.bigapps.unionrides.R;

/**
 * Created by seemtech2 on 05-10-2017.
 */

public class MyProfile_Adapter extends BaseAdapter{

    Context context;
    Activity activity;

    ArrayList<String> employer_list = new ArrayList<String>();
    int mCurrentPlayingPosition = -1;
    LayoutInflater mInflater;

    public MyProfile_Adapter(Context context, ArrayList<String> employer_list) {
        this.context = context;
        this.employer_list = employer_list;


        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.myprofilephotolist, null);
        }

        return convertView;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView custom_employee_title, custom_employee_price, custom_employee_status;
        RelativeLayout container2_offer;
        VideoView videoView;

        public ItemViewHolder(View itemView) {
            super(itemView);

        }
    }




}
