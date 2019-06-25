package mx.bigapps.unionrides.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by dell on 11/15/2017.
 */

public class Commom_Method {
    public static void showAlert(Activity mActivity, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();

        // Setting Dialog Title
		/*alertDialog.setTitle(mActivity.getResources().getString(
				R.string.sliderheading));
		*/// alertDialog.setIcon(mActivity.getResources().getDrawable(R.drawable.ic_launcher));
        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo;
        try {
            netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }
    public static void printLog(String key,String value){
        Log.d(key,"===="+value);
    }
}
