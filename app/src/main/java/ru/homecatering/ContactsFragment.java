package ru.homecatering;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import static android.content.Context.LOCATION_SERVICE;

public class ContactsFragment extends Fragment
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private LocationListener listener = initLocationListener();
    private LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    private Geofence geoFenceSmall;
    private Geofence geoFenceBig;
    private GoogleApiClient.ConnectionCallbacks connectionCallBack = initConnectionCallback();
    private static final int PERMISSION_REQUEST_CODE = 26;
    private String provider;
    private static final double MY_LATITUDE = 55.94221769;
    private static final double MY_LONGITUDE = 37.61245531;
    private static final float RADIUS_SMALL = 20000;
    private static final float RADIUS_BIG = 100000;

    private BroadcastReceiver broadcastReceiver = initBroadcastReceiver();

    private TextView geoInfo;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getLocation();
        View content = inflater.inflate(R.layout.contacts_fragment, container, false);
        geoInfo = content.findViewById(R.id.geo_info);

        IntentFilter geoFilter = new IntentFilter("ru.homecatering.geo");
        getActivity().registerReceiver(broadcastReceiver, geoFilter);
        return content;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        super.onStart();
        if (provider != null) {
            Log.i("GEO", provider);
            locationManager.requestLocationUpdates(provider, 10000, 10, listener, Looper.getMainLooper());
        }

        googleApiClient.connect();
        Log.d("GeoFence", "connect to googleApiClient");
    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeUpdates(listener);
        googleApiClient.disconnect();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
            createGoogleApiClient();
        } else {
            requestLocationPermissions();
        }
    }

    private void createGoogleApiClient() {
        geoFenceSmall = new Geofence.Builder()
                .setRequestId("small")
                .setTransitionTypes(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT | GeofencingRequest.INITIAL_TRIGGER_DWELL)
                .setCircularRegion(MY_LATITUDE, MY_LONGITUDE, RADIUS_SMALL)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(100)
                .build();
        geoFenceBig = new Geofence.Builder()
                .setRequestId("big")
                .setTransitionTypes(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT | GeofencingRequest.INITIAL_TRIGGER_DWELL)
                .setCircularRegion(MY_LATITUDE, MY_LONGITUDE, RADIUS_BIG)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(100)
                .build();
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(connectionCallBack)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {   // Отработает при неудаче
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d("GeoFence", "connection failed listener");
                    }
                })
                .build();
    }

    private GoogleApiClient.ConnectionCallbacks initConnectionCallback() {
        return new GoogleApiClient.ConnectionCallbacks() {
            @SuppressLint("MissingPermission")
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
                builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
                builder.addGeofence(geoFenceSmall);
                builder.addGeofence(geoFenceBig);
                GeofencingRequest geoFenceRequest = builder.build();
                Intent geoService = new Intent(getActivity(), GeoFenceService.class);
                PendingIntent pendingIntent = PendingIntent
                        .getService(getActivity(), 0, geoService, PendingIntent.FLAG_UPDATE_CURRENT);
                GeofencingClient geoClient = LocationServices.getGeofencingClient(getActivity());
                geoClient.addGeofences(geoFenceRequest, pendingIntent);
                Log.d("GeoFence", "add geo fence");
            }

            @Override
            public void onConnectionSuspended(int i) {
            }
        };
    }

    private BroadcastReceiver initBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String zone = intent.getStringExtra("zone");
                switch (zone) {
                    case "none":
                        geoInfo.setText(R.string.out_of_geo);
                        break;
                    case "small":
                        geoInfo.setText(R.string.within_small_geo);
                        break;
                    case "big":
                        geoInfo.setText(R.string.within_big_geo);
                        break;
                }
            }
        };
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
                createGoogleApiClient();
            }
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        provider = locationManager.getBestProvider(criteria, true);
    }

    private LocationListener initLocationListener() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String latitude = Double.toString(location.getLatitude());
                String longitude = Double.toString(location.getLongitude());
                String accuracy = Float.toString(location.getAccuracy());
                Log.i("GEO", latitude);
                Log.i("GEO", longitude);
                Log.i("GEO", accuracy);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }
}
