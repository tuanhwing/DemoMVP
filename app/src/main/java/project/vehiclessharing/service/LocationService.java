package project.vehiclessharing.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Tuan on 11/08/2017.
 */

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("service_aaaaaaa","create");
        buildGoogleApiClient();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MyService", "Received start id " + startId + ": " + intent);
//        isRunning = true;
        return START_STICKY; // run until explicitly stopped.
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("service_location: ","connect");

        try {
            configureRequest();//configure request
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, this);
            LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        } catch(Exception e){
            Log.d("service_location", "connect - " + e.getMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("service_susspended", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("service_location: ",String.valueOf(connectionResult.toString()));
    }

    /**
     * Build googleAPI Client
     */
    public synchronized void buildGoogleApiClient() {
        try {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            Log.d("bug1111","1");
            googleApiClient.connect();
        } catch (Exception e){
            Log.e("",String.valueOf(e.getMessage()));
        }
    }

    /**
     * configure request update value Location
     */
    private void configureRequest(){
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000); //5 seconds
        locationRequest.setFastestInterval(3000); //3 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onLocationChanged(Location location) {

    }
}
