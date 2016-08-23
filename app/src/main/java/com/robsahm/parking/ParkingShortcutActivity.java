package com.robsahm.parking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ParkingShortcutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent shortcutIntent = new Intent(getApplicationContext(), ParkingActivity.class);
        Intent.ShortcutIconResource iconResource =
                Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
        setResult(RESULT_OK, addIntent);
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
