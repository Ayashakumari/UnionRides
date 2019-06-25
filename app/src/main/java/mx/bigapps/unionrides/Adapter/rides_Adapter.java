package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import mx.bigapps.unionrides.Activity.Rides;
import mx.bigapps.unionrides.R;

/**
 * Created by seemtech2 on 05-10-2017.
 */

public class rides_Adapter extends BaseAdapter {

    Context context;
    Activity activity;

    ArrayList<String> employer_list = new ArrayList<String>();
    int mCurrentPlayingPosition = -1;
    LayoutInflater mInflater;

    public rides_Adapter(Context context, ArrayList<String> employer_list) {
        this.context = context;
        this.employer_list = employer_list;


        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return employer_list.size();
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
            convertView = mInflater.inflate(R.layout.ridelist, null);
            ImageView imageView=(ImageView)convertView.findViewById(R.id.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Rides.class);
                    context.startActivity(intent);
                }
            });
            ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
          //  final ImageView star = (ImageView) convertView.findViewById(R.id.star);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlert("Do You Want to delete this Ride");
                }
            });
          /* star.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (star.getDrawable().getConstantState().equals
                           (context.getResources().getDrawable(R.drawable.star).getConstantState())) {
                       star.setImageResource(R.drawable.star_empty);
                   }
                   else {
                       star.setImageResource(R.drawable.star);
                   }

               }
           });*/
        }

        return convertView;
    }

    private void showAlert(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                employer_list.remove(0);
                notifyDataSetChanged();

                dialog.dismiss();
            }

        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView delete, star;

        public ItemViewHolder(View itemView) {
            super(itemView);


        }
    }


}
