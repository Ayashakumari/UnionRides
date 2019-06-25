package mx.bigapps.unionrides.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mx.bigapps.unionrides.R;

/**
 * Created by dev on 2/6/2017.
 */

public class GetLocationMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnInfoWindowClickListener {
    GoogleMap mGoogleMap;
    private List<Marker> originMarkers = new ArrayList<>();
    GoogleApiClient mGoogleapiclient;
    LocationRequest mLocationRequest;
    double latitude;
    double longitude;
    String lat, lng;
    String sourceLocation = " ";
    LatLng source;
    Geocoder geoCoder;
    Location mLastLocation;
    Marker marker;
    int index = 0;
    ListView locationList;
    private static final String GOOGLE_API_KEY = "AIzaSyCb52oiCw6NvuOJ9pgonFnU_rfsV_C3p24";
    double latitude1 = 28.6219;
    double longitude1 = 77.0878;
    private int PROXIMITY_RADIUS = 1000;
    ArrayList locations;
    ArrayList places;
    GPSTracker gpsTracker;
    ArrayList latitudeArray;
    ArrayList longitudeArray;
    ImageView header_search, header_basket, header_logo;
    TextView cartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        geoCoder = new Geocoder(this, Locale.getDefault());
        gpsTracker = new GPSTracker(GetLocationMap.this);
        Log.d("lat44444", String.valueOf(gpsTracker.getLongitude()));
        Log.d("lang555555", String.valueOf(gpsTracker.getLatitude()));
        lat = String.valueOf(gpsTracker.getLatitude());
        lng = String.valueOf(gpsTracker.getLongitude());


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleapiclient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleapiclient.connect();
        mGoogleMap.setOnInfoWindowClickListener(this);
        mGoogleMap.setMyLocationEnabled(true);
        final MarkerOptions markerOptions = new MarkerOptions();
        source = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        Log.d("location", sourceLocation);
        markerOptions.position(source);
        markerOptions.title(sourceLocation);
        //  markerOptions.snippet(loaction);
        markerOptions.snippet("Sweets & Cake");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerred));
        mGoogleMap.addMarker(markerOptions);


    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // mLocationRequest.setInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleapiclient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        final MarkerOptions markerOptions = new MarkerOptions();
        mLastLocation = location;
        if (marker != null) {
            marker.remove();
        }
        String filterAddress = " ";
        if (location == null) {
            Toast.makeText(this, "Can't get current location", Toast.LENGTH_LONG).show();
        } else {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            //LatLng source;
            source = new LatLng(latitude, longitude);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 12.0f));

            try {
                List<Address> Sourceaddresses =
                        geoCoder.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();

                if (Sourceaddresses.size() > 0) {
                    for (int j = 0; j < Sourceaddresses.get(0).getMaxAddressLineIndex(); j++)
                        filterAddress += Sourceaddresses.get(0).getAddressLine(j) + " ";
                    Address address = Sourceaddresses.get(0);
                    sb.append(address.getSubLocality()).append(",");
                    sb.append(address.getLocality()).append(",");
                    sb.append(address.getCountryName()).append(".");
                }
                sourceLocation = sb.toString();
                Log.d("location", sourceLocation);
                markerOptions.position(source);
                markerOptions.title(sourceLocation);
                //  markerOptions.snippet(loaction);
                markerOptions.snippet("Sweets & Cake");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerred));
                mGoogleMap.addMarker(markerOptions);


            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
            if (mGoogleapiclient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleapiclient, this);
            }

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    dialog.dismiss();
                    break;
            }
        }
    };

   /* @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You want to exit?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }*/

}
