package mx.bigapps.unionrides.Application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Created by dev on 3/20/2017.
 */


public class AppController extends Application {


    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    // private static AppController mInstance;
    public static Context mContext;
    private static SharedPreferences mSharedPreferences;
    private static AppController mInstance;

    public static SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }


    private void initSharedPreferences() {
        PrefMangr.initInstance();
        mSharedPreferences = getSharedPreferences("CASIO_PREF", MODE_PRIVATE);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        initSharedPreferences();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(AppController.this);
    }
}