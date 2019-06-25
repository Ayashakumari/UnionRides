package mx.bigapps.unionrides.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import mx.bigapps.unionrides.Activity.Chat_window;
import mx.bigapps.unionrides.R;

/**
 * Created by dev on 01-11-2017.
 */

public class Recent_Chat_Adapter extends RecyclerView.Adapter<Recent_Chat_Adapter.MyViewHolder> {

    ArrayList<String> personNames;
    Context context;

    public Recent_Chat_Adapter(Context context, ArrayList<String> personNames) {
        this.context = context;
        this.personNames = personNames;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recycle, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return personNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout chatlayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ImageView mimageView = (ImageView) itemView.findViewById(R.id.chat_pic);
            chatlayout = (RelativeLayout) itemView.findViewById(R.id.chatlayout);
            chatlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Chat_window.class);
                    context.startActivity(intent);
                }
            });
            Bitmap mbitmap = ((BitmapDrawable) itemView.getResources().getDrawable(R.drawable.photo)).getBitmap();
            Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
            Canvas canvas = new Canvas(imageRounded);
            Paint mpaint = new Paint();
            mpaint.setAntiAlias(true);
            mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 100, 100, mpaint);
            // Round Image Corner 100 100 100 100

            mimageView.setImageBitmap(imageRounded);
        }
    }
}
