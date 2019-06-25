package mx.bigapps.unionrides.Activity;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import mx.bigapps.unionrides.Application.PrefMangr;

public class DeleteTokenService  extends IntentService
{
    public static final String TAG = DeleteTokenService.class.getSimpleName();

    public DeleteTokenService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        try
        {
            // Check for current token
            String originalToken = PrefMangr.getInstance().getDeviceId();
            Log.d(TAG, "Token before deletion: " + originalToken);

            // Resets Instance ID and revokes all tokens.
            FirebaseInstanceId.getInstance().deleteInstanceId();

            // Clear current saved token
            saveTokenToPrefs("");

            // Check for success of empty token
            String tokenCheck =  PrefMangr.getInstance().getDeviceId();
            Log.d(TAG, "Token deleted. Proof: " + tokenCheck);

            // Now manually call onTokenRefresh()
            Log.d(TAG, "Getting new token");
            FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + FirebaseInstanceId.getInstance().getToken());

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void saveTokenToPrefs(String _token)
    {
        // Access Shared Preferences
        PrefMangr.getInstance().setDeviceId(_token);
    }


}