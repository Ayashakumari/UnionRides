package mx.bigapps.unionrides.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gcm.GCMRegistrar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.GCMIntentService;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.AppConstants;


/**
 * Created by AMIT on 6/17/2017.
 */

public class Splash_Screen extends AppCompatActivity {

    int i = 0;
    Timer timer;
    MyReceiver receiver;
    String regId, show_dialog;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash);
        HttpsTrustManager.allowAllSSL();
        Intent intent = new Intent();
        intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
        sendBroadcast(intent);
        TimerTask task = new MYTimerClass();
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 700, 1000);
        receiver = new MyReceiver();
        IntentFilter intentfilter = new IntentFilter(GCMIntentService.REGISTER_ACTION);
        registerReceiver(receiver, intentfilter);

        regId = PrefMangr.getInstance().getDeviceId();
        Log.d("rrrrrrrrrrrr111111", "" + regId);
        printKeyHash(this);



    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

//            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=========", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    class MYTimerClass extends TimerTask {


        @Override
        public void run() {
            // TODO Auto-generated method stub
            i++;


            Log.d("timer", "count " + i);
            if (i == 2) {
                timer.cancel();


                try {
                    Log.d("usetrId", ">>" + PrefMangr.getInstance().getUserId());
                    Log.d("usetrname", ">>" + PrefMangr.getInstance().getUserName());

                    if (PrefMangr.getInstance().getUserId() == null) {


                        //PrefMangr.getInstance().setDeviceId(regId);
                        Intent intent = new Intent(Splash_Screen.this, Welcome.class);
                        intent.putExtra("device_token", regId);
                        startActivity(intent);


                    } else {
                           /* regId = GCMRegistrar.getRegistrationId(Splash_Screen.this);
                            GCMRegistrar.register(Splash_Screen.this, AppConstants.SENDER_ID);*/
                        // PrefMangr.getInstance().setDeviceId(regId);
                        Log.d("44444444444", ">>" + regId);
                        Intent intent = new Intent(Splash_Screen.this, Wall.class);
                        intent.putExtra("device_token", regId);
                        startActivity(intent);


                    }


                } catch (Exception e) {
                    // TODO: handle exception
                }
                /*} else {


                    regId = GCMRegistrar.getRegistrationId(Splash_Screen.this);
                    GCMRegistrar.register(Splash_Screen.this, AppConstants.SENDER_ID);
                    Log.d("33333333333333", "" + regId);
                    try {


                 *//*       preferences2 = getSharedPreferences(Prefs_remember_value, MODE_PRIVATE);
                        editor = preferences2.edit();
                        editor.putString("regId", regId);*//*

                        PrefMangr.getInstance().setDeviceId(regId);
                        Log.d("55555555555555", ">>" + regId);
                        // editor.commit();
                        Intent intent = new Intent(Splash_Screen.this, Welcome.class);
                        intent.putExtra("device_token", regId);
                        startActivity(intent);

                    } catch (Exception e) {
                        // TODO: handle exception
                    }

               */
                //}
            }
        }
    }

    private class MyReceiver extends BroadcastReceiver

    {


        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            String regId = intent.getStringExtra(AppConstants.REGISTRATION_ID);


            Intent intentt = new Intent(Splash_Screen.this, Welcome.class);
            intentt.putExtra("device_token", regId);
            startActivity(intentt);


        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {

            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }

    }
}

