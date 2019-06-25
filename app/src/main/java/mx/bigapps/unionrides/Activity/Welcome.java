package mx.bigapps.unionrides.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import mx.bigapps.unionrides.R;

/**
 * Created by admin on 01-11-2017.
 */
public class Welcome extends AppCompatActivity {
    TextView welcome_login, welcome_createAcct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        welcome_createAcct = (TextView) findViewById(R.id.welcome_createAcct);
        welcome_login = (TextView) findViewById(R.id.welcome_login);
        welcome_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, MemberLogin.class);
                startActivity(intent);

            }
        });
        welcome_createAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome.this, CreateAccount.class));
            }
        });
    }
}
