package mx.bigapps.unionrides.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import mx.bigapps.unionrides.Model.ChatHistory_Entity;
import mx.bigapps.unionrides.R;

/**
 * Created by lenovo on 3/21/2017.
 */
public class Chat_Adapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ChatHistory_Entity> messages;
    private LayoutInflater mlayLayoutInflater;
    ViewHolder holder;
    int position2;
    static public Calendar metFromdate1 = Calendar.getInstance();
    static public Calendar metTodate1 = Calendar.getInstance();
    static public Calendar metFromdate2 = Calendar.getInstance();
    static public Calendar metTodate2 = Calendar.getInstance();
    public static String date_selected = "", date_selected1 = "", date_selected2 = "", date_selected3 = "";
    int mCurrentPlayingPosition = -1;

    class ViewHolder {
        TextView chat_send_time;
        TextView chat_send_text, date_txt;
        TextView chat_recieve_text;
        TextView chat_recieve_time;
        ImageView chat_send_image, chat_recieve_image;
        RelativeLayout send_relative, recieve_relative, relative_response;
    }

    public Chat_Adapter(Activity activity, ArrayList<ChatHistory_Entity> chat_list) {
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.messages = chat_list;
        this.mlayLayoutInflater = LayoutInflater.from(this.activity);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {

            convertView = mlayLayoutInflater.inflate(R.layout.custom_chat_list, null);
            holder = new ViewHolder();

            holder.chat_send_time = (TextView) convertView.findViewById(R.id.chat_send_time);
            holder.chat_recieve_time = (TextView) convertView.findViewById(R.id.chat_receive_time);
            holder.chat_send_text = (TextView) convertView.findViewById(R.id.chat_send_text);
            holder.date_txt = (TextView) convertView.findViewById(R.id.date_txt);
            holder.chat_recieve_text = (TextView) convertView.findViewById(R.id.chat_reciev_text);
            holder.send_relative = (RelativeLayout) convertView.findViewById(R.id.send_relative);
            holder.recieve_relative = (RelativeLayout) convertView.findViewById(R.id.recieve_relative);
            holder.relative_response = (RelativeLayout) convertView.findViewById(R.id.relative_response);
            holder.date_txt.setTag(1);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        try {
            if (messages.get(position).getNo().equals("2")) {
                holder.date_txt.setVisibility(View.VISIBLE);
                holder.relative_response.setVisibility(View.VISIBLE);
                final Calendar c2 = Calendar.getInstance();
                int year = c2.get(Calendar.YEAR);
                int month = c2.get(Calendar.MONTH);
                int day = c2.get(Calendar.DAY_OF_MONTH);
                metTodate2.set(Calendar.YEAR, year);
                metTodate2.set(Calendar.MONTH, month);
                metTodate2.set(Calendar.DAY_OF_MONTH, day);
                final Calendar c3 = Calendar.getInstance();
                c3.add(Calendar.DATE, -1);
                int year1 = c3.get(Calendar.YEAR);
                int month1 = c3.get(Calendar.MONTH);
                int day1 = c3.get(Calendar.DAY_OF_MONTH);
                metTodate2.set(Calendar.YEAR, year1);
                metTodate2.set(Calendar.MONTH, month1);
                metTodate2.set(Calendar.DAY_OF_MONTH, day1 - 1);

                String str1;
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                str1 = df1.format(c2.getTime());
//                Log.d("str1", str1);
                String str2;
                SimpleDateFormat df7 = new SimpleDateFormat("yyyy-MM-dd");
                str2 = df7.format(c3.getTime());
//                Log.d("str2", str2);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    metFromdate2.setTime(dateFormat.parse(messages.get(position).getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                date_selected2 = df.format(metFromdate2.getTime());
//                Log.d("date_selected", date_selected2);
                if (date_selected2.equals(str1)) {
                    SimpleDateFormat df2 = new SimpleDateFormat("hh:mm");
                    date_selected3 = df2.format(metFromdate2.getTime());
                    holder.date_txt.setText("Today");

                } else if (date_selected2.equals(str2)) {
                    SimpleDateFormat df2 = new SimpleDateFormat("hh:mm");
                    date_selected3 = df2.format(metFromdate2.getTime());
                    holder.date_txt.setText("Yesterday");

                } else {
                    SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
                    date_selected3 = df2.format(metFromdate2.getTime());
                    holder.date_txt.setText(date_selected3);
                }

            } else {
                holder.relative_response.setVisibility(View.GONE);
                holder.date_txt.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        String status = messages.get(position).getStatus();

        if (status.equals("sent")) {


            holder.send_relative.setVisibility(View.VISIBLE);
            holder.recieve_relative.setVisibility(View.GONE);
            holder.chat_send_text.setVisibility(View.VISIBLE);
            final Calendar c2 = Calendar.getInstance();
            int year = c2.get(Calendar.YEAR);
            int month = c2.get(Calendar.MONTH);
            int day = c2.get(Calendar.DAY_OF_MONTH);
            metTodate1.set(Calendar.YEAR, year);
            metTodate1.set(Calendar.MONTH, month);
            metTodate1.set(Calendar.DAY_OF_MONTH, day);

            String str1;
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            str1 = df1.format(c2.getTime());
//            Log.d("str1",str1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            try {
                metFromdate1.setTime(dateFormat.parse(messages.get(position).getTime()));
                Log.d("timee", messages.get(position).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date_selected = df.format(metFromdate1.getTime());
//            Log.d("date_selected",date_selected);
            if (date_selected.equals(str1)) {
                SimpleDateFormat df2 = new SimpleDateFormat("hh:mm a");
                date_selected1 = df2.format(metFromdate1.getTime());

            } else {
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                date_selected1 = df2.format(metFromdate1.getTime());
            }
            holder.chat_send_time.setText(messages.get(position).getTimee());
            holder.chat_send_text.setText(messages.get(position).getMessage());
        } else {

            holder.send_relative.setVisibility(View.GONE);
            holder.recieve_relative.setVisibility(View.VISIBLE);
//            itemViewHolder.chat_recieve_image.setVisibility(View.GONE);
            holder.chat_recieve_text.setVisibility(View.VISIBLE);
            final Calendar c2 = Calendar.getInstance();
            int year = c2.get(Calendar.YEAR);
            int month = c2.get(Calendar.MONTH);
            int day = c2.get(Calendar.DAY_OF_MONTH);
            metTodate1.set(Calendar.YEAR, year);
            metTodate1.set(Calendar.MONTH, month);
            metTodate1.set(Calendar.DAY_OF_MONTH, day);

            String str1;
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            str1 = df1.format(c2.getTime());
//            Log.d("str1",str1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
            try {
                metFromdate1.setTime(dateFormat.parse(messages.get(position).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date_selected = df.format(metFromdate1.getTime());
//            Log.d("date_selected",date_selected);
            if (date_selected.equals(str1)) {
                SimpleDateFormat df2 = new SimpleDateFormat("hh:mm a");
                date_selected1 = df2.format(metFromdate1.getTime());

            } else {
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                date_selected1 = df2.format(metFromdate1.getTime());
            }


            holder.chat_recieve_time.setText(messages.get(position).getTimee());
            holder.chat_recieve_text.setText(messages.get(position).getMessage());


        }
        try {
            if (messages.get(position).getNo().equals("2")) {
                holder.date_txt.setVisibility(View.VISIBLE);
                holder.relative_response.setVisibility(View.VISIBLE);
                final Calendar c2 = Calendar.getInstance();
                int year = c2.get(Calendar.YEAR);
                int month = c2.get(Calendar.MONTH);
                int day = c2.get(Calendar.DAY_OF_MONTH);
                metTodate2.set(Calendar.YEAR, year);
                metTodate2.set(Calendar.MONTH, month);
                metTodate2.set(Calendar.DAY_OF_MONTH, day);
                final Calendar c3 = Calendar.getInstance();
                c3.add(Calendar.DATE, -1);
                int year1 = c3.get(Calendar.YEAR);
                int month1 = c3.get(Calendar.MONTH);
                int day1 = c3.get(Calendar.DAY_OF_MONTH);
                metTodate2.set(Calendar.YEAR, year1);
                metTodate2.set(Calendar.MONTH, month1);
                metTodate2.set(Calendar.DAY_OF_MONTH, day1 - 1);

                String str1;
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                str1 = df1.format(c2.getTime());
//                Log.d("str1", str1);
                String str2;
                SimpleDateFormat df7 = new SimpleDateFormat("yyyy-MM-dd");
                str2 = df7.format(c3.getTime());
//                Log.d("str2", str2);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
                try {
                    metFromdate2.setTime(dateFormat.parse(messages.get(position).getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                date_selected2 = df.format(metFromdate2.getTime());
//                Log.d("date_selected", date_selected2);
                if (date_selected2.equals(str1)) {
                    SimpleDateFormat df2 = new SimpleDateFormat("hh:mm a");
                    date_selected3 = df2.format(metFromdate2.getTime());
                    holder.date_txt.setText("Today");

                } else if (date_selected2.equals(str2)) {
                    SimpleDateFormat df2 = new SimpleDateFormat("hh:mm a");
                    date_selected3 = df2.format(metFromdate2.getTime());
                    holder.date_txt.setText("Yesterday");

                } else {
                    SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
                    date_selected3 = df2.format(metFromdate2.getTime());
                    holder.date_txt.setText(date_selected3);
                }


            } else {

                holder.relative_response.setVisibility(View.GONE);
                holder.date_txt.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            Log.d("exception", String.valueOf(e));
            e.printStackTrace();
        }

        return convertView;


    }


}

