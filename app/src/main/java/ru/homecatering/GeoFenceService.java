package ru.homecatering;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeoFenceService extends IntentService {


    public GeoFenceService() {
        super("GeoFenceService");
    }

    @Override
    public void onCreate() {
        Log.d("GeoFence", "on create");
        super.onCreate();
    }


    public GeoFenceService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("GeoFence", "on handle");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        List<Geofence> triggeredGeoFences = geofencingEvent.getTriggeringGeofences();
        int transitionType = geofencingEvent.getGeofenceTransition();
        Log.i("GEO ", transitionType + "");
        sendData(triggeredGeoFences);
    }

    private void sendData(List<Geofence> triggeredGeoFences) {
        String zone = "none", id;
        if (triggeredGeoFences != null) {
            for (Geofence gf : triggeredGeoFences) {
                id = gf.getRequestId();
                Log.i("GEO ", id);
                if (id.equals("small")) {
                    zone = "small";
                    break;
                } else if (id.equals("big")) zone = "big";
            }
        }
        Intent intent = new Intent("ru.homecatering.geo");
        intent.putExtra("zone", zone);
        sendBroadcast(intent);
    }
}
