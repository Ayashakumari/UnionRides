package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import mx.bigapps.unionrides.Activity.SquareImageView;
import mx.bigapps.unionrides.Model.Added_Image_Entity;
import mx.bigapps.unionrides.R;

/**
 * Created by we on 10/10/2018.
 */

public class First_rideAdapter extends BaseAdapter {

    Context context;
    Activity activity;

    ArrayList<Added_Image_Entity> employer_list = new ArrayList<Added_Image_Entity>();
    int mCurrentPlayingPosition = -1;

    AdapterView.OnItemClickListener onItemClickListener;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String msg, pic_id, file_type;
    LayoutInflater mInflater;

    public First_rideAdapter(Context context, ArrayList<Added_Image_Entity> employer_list) {
        this.context = context;
        this.employer_list = employer_list;
        this.onItemClickListener = onItemClickListener;
        mInflater = LayoutInflater.from(context);


    }

    @Override
    public int getCount() {
        return employer_list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.rowreiderphoto, null);
        }
        SquareImageView image = (SquareImageView) convertView.findViewById(R.id.image);
        ImageView play = (ImageView) convertView.findViewById(R.id.play);
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progress);
        if (employer_list.get(position).getFile_type().equals("image")) {
            play.setVisibility(View.GONE);
        } else {
            play.setVisibility(View.VISIBLE);

        }
        return null;


    }
}
