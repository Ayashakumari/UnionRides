package mx.bigapps.unionrides.Activity;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mx.bigapps.unionrides.Adapter.PopupAdapterMarker;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Model.AlleventData;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;


//import com.google.android.gms.contextmanager.Relation;

public class MapView_Activity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback {
    TextView toolbar_txt, select_service;
    public static final String TAG = MapView_Activity.class.getSimpleName();

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    TextView locationSearch;
    String location;
    RelativeLayout search_layout;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    ImageView camera;
    Double latitude = null, longitude = null;
    GPSTracker gpsTracker;
    Marker marker;
    String editEvent, eventID;
    Button sectbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        sectbtn=(Button)findViewById(R.id.sectbtn);
        camera = (ImageView) findViewById(R.id.header_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapView_Activity.this, Add_Events.class));
                finish();
            }
        });
        locationSearch = (TextView) findViewById(R.id.locationSearch);
        search_layout = (RelativeLayout) findViewById(R.id.search_layout);
        gpsTracker = new GPSTracker(this);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        Intent intent = getIntent();
        editEvent = intent.getStringExtra("editEvent");
        eventID = intent.getStringExtra("eventID");
        setUpMapIfNeeded();
sectbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (editEvent != null) {


        } else {
            Intent intent = new Intent(MapView_Activity.this, Add_Events.class);

            intent.putExtra("change", String.valueOf(marker.getTitle() + ", " + String.valueOf(marker.getSnippet())));
            finish();
        }
    }
});
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        search_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //create_edittxt6.setText("Please select date");
                try {
                    Intent intent =
                            new PlaceAutocomplete
                                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(MapView_Activity.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag))
                    .getMapAsync(this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int RESULT_OK = -1;

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
                Log.d("lat", String.valueOf(place.getLatLng()));
                locationSearch.setText(place.getAddress());
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                List<Address> addressList = null;
                location = locationSearch.getText().toString();

                if (location != null && !location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);
                        Log.d("addressList", String.valueOf(address));
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAdminArea()));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        MarkerOptions options = new MarkerOptions()
                                .position(latLng)
                                .title("I am here!");
                        mMap.addMarker(options);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void onMapSearch(View view) {

        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null && !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
                final Address address = addressList.get(0);
                Log.d("searchlocation", String.valueOf(address));
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAdminArea()));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                String str = marker.getTitle();
                Log.d("addressstr", str);
                PrefMangr.getInstance().setLocation(str);
                PrefMangr.getInstance().setlatitude(String.valueOf(address.getLatitude()));
                PrefMangr.getInstance().setlogitude(String.valueOf(address.getLongitude()));
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String str = marker.getTitle();
                        Log.d("addressstr", str);
                        PrefMangr.getInstance().setLocation(str);
                        PrefMangr.getInstance().setlatitude(String.valueOf(address.getLatitude()));
                        PrefMangr.getInstance().setlogitude(String.valueOf(address.getLongitude()));
                        Intent intent = new Intent(MapView_Activity.this, Add_Events.class);
                        intent.putExtra("location", str);
                        finish();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(MapView_Activity.this, "Enter Location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {

                mMap.clear();


                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                String cityName = null, locatity = null;
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(latLng.latitude
                            ,
                            latLng.longitude, 1);
                    Log.d("list1", addresses.toString());
                    if (addresses.size() > 0)
                        System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getSubAdminArea();
                    locatity = addresses.get(0).getLocality();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(cityName)
                        .snippet(locatity);
                mMap.addMarker(options);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                String str = marker.getTitle();
                PrefMangr.getInstance().setLocation(str);
                PrefMangr.getInstance().setlatitude(String.valueOf(latLng.latitude));
                PrefMangr.getInstance().setlogitude(String.valueOf(latLng.longitude));
                Intent intent = new Intent(MapView_Activity.this, Add_Events.class);
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String str = marker.getTitle();
                        Log.d("addresses", str);
                        PrefMangr.getInstance().setLocation(str);
                        PrefMangr.getInstance().setlatitude(String.valueOf(latLng.latitude));
                        PrefMangr.getInstance().setlogitude(String.valueOf(latLng.longitude));
                        Intent intent = new Intent(MapView_Activity.this, Add_Events.class);

                        finish();
                    }
                });
                Log.d("address", cityName);

            }
        });
        setUpMap();
    }


    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng point) {
                mMap.clear();
                if (marker != null) {
                    marker.remove();
                }
                String s = null;
                String cityName = "";
                String address = null;
                String postalcode = "";
                String sublocality = "";
                String addmin = "";
                final MarkerOptions markerOptions = new MarkerOptions();

                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(point.latitude,
                            point.longitude, 1);
                    Log.d("list2", addresses.toString());
                    if (addresses.size() > 0)
                        System.out.println(addresses.get(0).getLocality());
                    address = addresses.get(0).getAddressLine(1) + "," + addresses.get(0).getAddressLine(2) + "," + addresses.get(0).getAddressLine(3);
                    Log.d("address", address);
                    cityName = addresses.get(0).getAddressLine(0);
                    addmin = addresses.get(0).getAddressLine(2);
                    postalcode = addresses.get(0).getAddressLine(3);
                    sublocality = addresses.get(0).getAddressLine(1);
                    if (addmin == null) {
                        addmin = "";
                    }
                    if (postalcode == null) {
                        postalcode = "";
                    }
                    if (sublocality == null) {
                        sublocality = "";
                    }
                    s = sublocality + " "

                            + addmin + " " + postalcode;
                    Log.d("markerplace", s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //  mMap.addMarker(new MarkerOptions().position(point).title(s).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                markerOptions.position(point);
                // markerOptions.position(adapter.getItem(0).getLatLng());
                markerOptions.snippet(s);
                markerOptions.title(cityName);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                marker = mMap.addMarker(markerOptions);
                marker.showInfoWindow();
                mMap.setInfoWindowAdapter(new PopupAdapterMarker(getLayoutInflater()));
                PrefMangr.getInstance().setLocation(String.valueOf(marker.getTitle() + ", " + String.valueOf(marker.getSnippet())));

                PrefMangr.getInstance().setlatitude(String.valueOf(point.latitude));
                PrefMangr.getInstance().setlogitude(String.valueOf(point.longitude));
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        if (editEvent != null) {
                            PrefMangr.getInstance().setLocation(String.valueOf(marker.getTitle() + ", " + String.valueOf(marker.getSnippet())));

                            PrefMangr.getInstance().setlatitude(String.valueOf(point.latitude));
                            PrefMangr.getInstance().setlogitude(String.valueOf(point.longitude));
                            editEventProfile(eventID);

                        } else {
                            Intent intent = new Intent(MapView_Activity.this, Add_Events.class);
                            PrefMangr.getInstance().setLocation(String.valueOf(marker.getTitle() + ", " + String.valueOf(marker.getSnippet())));
                            PrefMangr.getInstance().setlatitude(String.valueOf(point.latitude));
                            PrefMangr.getInstance().setlogitude(String.valueOf(point.longitude));
                            intent.putExtra("change", String.valueOf(marker.getTitle() + ", " + String.valueOf(marker.getSnippet())));
                            finish();
                        }


                    }
                });

            }
        });
    }

    private void handleNewLocation(Location location) {
        mMap.clear();
        Log.d(TAG, location.toString());
        String s = null;
        String cityName = "";
        String address = null;
        String postalcode = "";
        String sublocality = "";
        String addmin = "";
        final double currentLatitude;
        final double currentLongitude;
        if (latitude == null) {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        } else {
            currentLatitude = latitude;
            currentLongitude = longitude;
        }

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(currentLatitude,
                    currentLongitude, 1);
            Log.d("list3", addresses.toString());
            if (addresses.size() > 0)
                System.out.println(addresses.get(0).getLocality());

            cityName = addresses.get(0).getAddressLine(0);
            addmin = addresses.get(0).getAddressLine(2);
            postalcode = addresses.get(0).getAddressLine(3);
            sublocality = addresses.get(0).getAddressLine(1);
           /* if (addmin == null) {
                addmin = "";
            }
            if (postalcode == null) {
                postalcode = "";
            }
            if (sublocality == null) {
                sublocality = "";
            }*/
            s = cityName

                    ;
            Log.d("markerplace", s);
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(cityName);
                   // .snippet(s);
            PrefMangr.getInstance().setLocation(String.valueOf(cityName));
            PrefMangr.getInstance().setlatitude(String.valueOf(currentLatitude));
            PrefMangr.getInstance().setlogitude(String.valueOf(currentLongitude));
            marker = mMap.addMarker(options);
            marker.showInfoWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrefMangr.getInstance().setLocation(String.valueOf(marker.getTitle()));
        PrefMangr.getInstance().setlatitude(String.valueOf(currentLatitude));
        PrefMangr.getInstance().setlogitude(String.valueOf(currentLongitude));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {


                if (editEvent != null) {
                    PrefMangr.getInstance().setLocation(String.valueOf(marker.getSnippet()));
                    PrefMangr.getInstance().setlatitude(String.valueOf(currentLatitude));
                    PrefMangr.getInstance().setlogitude(String.valueOf(currentLongitude));
                    editEventProfile(eventID);

                } else {
                    Intent intent = new Intent(MapView_Activity.this, Add_Events.class);
                    PrefMangr.getInstance().setLocation(String.valueOf(marker.getTitle()));
                    PrefMangr.getInstance().setlatitude(String.valueOf(currentLatitude));
                    PrefMangr.getInstance().setlogitude(String.valueOf(currentLongitude));
                    intent.putExtra("change", String.valueOf(marker.getTitle() + ", " + String.valueOf(marker.getSnippet())));
                    finish();
                }

            }
        });
    }

    private void editEventProfile(final String eventID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;

                        ArrayList<AlleventData> list = new ArrayList<AlleventData>();
                        JSONObject obj = null;
                        Log.d("Editevent", response.toString());

                        try {
                            obj = new JSONObject(response.toString());
                            msg = obj.getString("msg");
                            Log.d("msg", msg);
                            Intent intent = new Intent(MapView_Activity.this, EventsMain.class);
                            startActivity(intent);

                        } catch (JSONException e1) {

                            e1.printStackTrace();
                        }


                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Editevent");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("event_id", eventID);
                params.put("event_location", PrefMangr.getInstance().getLocation());
                params.put("latitude", PrefMangr.getInstance().getlatitude());
                params.put("longitude", PrefMangr.getInstance().getlogitude());

                Log.d("Editevent", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(MapView_Activity.this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MapView_Activity.this, Add_Events.class));
        finish();
    }


}
