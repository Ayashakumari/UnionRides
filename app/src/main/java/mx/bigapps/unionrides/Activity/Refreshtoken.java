package mx.bigapps.unionrides.Activity;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import mx.bigapps.unionrides.Application.PrefMangr;

public class Refreshtoken implements Runnable {
    @Override
    public void run() {
        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Clear current saved token


        // Check for success of empty token
        String tokenCheck = PrefMangr.getInstance().getDeviceId();

        String refreshtoken = FirebaseInstanceId.getInstance().getToken();
        PrefMangr.getInstance().setDeviceId(refreshtoken);
        Log.d("Refreshed token:", "" + refreshtoken);

    }
}
