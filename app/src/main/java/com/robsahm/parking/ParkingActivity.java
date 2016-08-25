package com.robsahm.parking;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.robsahm.parking.util.ParkingDataReceiver;
import com.robsahm.parking.util.ToastUtil;

public class ParkingActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private ToastUtil toastUtil;
    private ParkingDataReceiver parkingDataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parkingDataReceiver = new ParkingDataReceiver(this);
        toastUtil = ToastUtil.getInstance(this);

        locationRequest = new LocationRequest()
                .setNumUpdates(1)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(5000);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(AppIndex.API).build();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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
    public void onLocationChanged(Location location) {
        parkingDataReceiver.execute(
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        toastUtil.show(connectionResult.getErrorMessage(), Toast.LENGTH_LONG);
    }

    @Override
    public void onStop() {
        super.onStop();
        toastUtil.cancel();
        googleApiClient.disconnect();
        finish();
    }
}
