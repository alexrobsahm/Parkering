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
    private long startTime;

    public ParkingService() {
        super("ParkingService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startTime = System.currentTimeMillis();

        parkingDataReceiver = new ParkingDataReceiver(this);
        toastUtil = ToastUtil.getInstance(this);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(100);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        toastUtil.show(R.string.gps_start, Toast.LENGTH_LONG);
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (isLastLocationNewerThanTwoMinutes(location)) {
            fetchParkingInfo(location);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        toastUtil.show(connectionResult.getErrorMessage(), Toast.LENGTH_LONG);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getAccuracy() < 50) {
            fetchParkingInfo(location);
        } else if (location.getAccuracy() < 50) {
            fetchParkingInfo(location);
        } else {
            if(timeOut()) {
                googleApiClient.disconnect();
                toastUtil.show(R.string.no_parking_data, Toast.LENGTH_SHORT);
            } else {
                toastUtil.show(R.string.gps_search_continues, Toast.LENGTH_LONG);
            }
        }
    }

    private void fetchParkingInfo(Location location) {
        googleApiClient.disconnect();
        parkingDataReceiver.execute(
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()));
    }

    private boolean isLastLocationNewerThanTwoMinutes(Location location) {
        return location != null && System.currentTimeMillis() - location.getTime() < 120000;
    }

    private boolean timeOut() {
        return System.currentTimeMillis() - startTime > 30000;
    }
}
