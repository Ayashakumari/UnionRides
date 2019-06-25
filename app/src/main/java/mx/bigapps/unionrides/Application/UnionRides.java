package mx.bigapps.unionrides.Application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dev on 6/27/2016.
 */
public class UnionRides extends Application {

    public static Context mContext;
    private static SharedPreferences mSharedPreferences;
    private static UnionRides mInstance;

    public static SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initSharedPreferences();
    }

    private void initSharedPreferences() {
        PrefMangr.initInstance();
        mSharedPreferences = getSharedPreferences("CASIO_PREF", MODE_PRIVATE);
    }

}
