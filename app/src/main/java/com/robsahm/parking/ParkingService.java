package com.robsahm.parking;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.robsahm.parking.util.ParkingDataReceiver;
import com.robsahm.parking.util.ToastUtil;

public class ParkingService extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private ToastUtil toastUtil;
    private ParkingDataReceiver parkingDataReceiver;

    public ParkingService() {
        super("ParkingService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        parkingDataReceiver = new ParkingDataReceiver(this);
        toastUtil = ToastUtil.getInstance(this);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(5000);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        toastUtil.show(R.string.gps_start, Toast.LENGTH_SHORT);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        toastUtil.show(connectionResult.getErrorMessage(), Toast.LENGTH_LONG);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (System.currentTimeMillis() - location.getTime() < 300000) {
            parkingDataReceiver.execute(
                    String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()));
        }
    }
}
