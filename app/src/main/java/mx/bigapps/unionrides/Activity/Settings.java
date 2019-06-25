package mx.bigapps.unionrides.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import mx.bigapps.unionrides.Adapter.Blocklist_Adapter;
import mx.bigapps.unionrides.R;

/**
 * Created by dev on 01-11-2017.
 */

public class Settings extends Activity {
    ImageView settings_back;

    @Override
    protected void onCreate(Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        setContentView(R.layout.settings);
        settings_back = (ImageView) findViewById(R.id.settings_back);
        settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Wall.class));
            }
        });
        RelativeLayout acct = (RelativeLayout) findViewById(R.id.acct_setting);
        acct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Account_Setting.class));


            }
        });
        RelativeLayout cp = (RelativeLayout) findViewById(R.id.settings_cp);
        cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Change_password.class));
            }
        });
        RelativeLayout settings_block = (RelativeLayout) findViewById(R.id.settings_block);
        settings_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BlockList.class));
            }
        });
    }

}
