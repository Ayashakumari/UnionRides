package mx.bigapps.unionrides.Activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import mx.bigapps.unionrides.Model.ChatMessage;
import mx.bigapps.unionrides.R;

/**
 * Created by dev on 01-11-2017.
 */

public class Bottom_menu extends Activity {


    public void b_header() {




            ImageView a = (ImageView) findViewById(R.id.header_setting);
            a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(getApplicationContext(), Settings.class));
                }
            });
            ImageView b = (ImageView) findViewById(R.id.header_chat);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), ChatMessage.class));

                }
            });
        }




    }







