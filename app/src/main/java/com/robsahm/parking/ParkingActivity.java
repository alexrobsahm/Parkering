package com.robsahm.parking;

import android.app.Activity;
import android.content.Intent;

public class ParkingActivity extends Activity {

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ParkingService.class);
        this.startService(intent);
        finish();
    }
}