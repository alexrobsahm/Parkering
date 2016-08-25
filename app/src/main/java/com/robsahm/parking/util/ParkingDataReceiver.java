package com.robsahm.parking.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.robsahm.parking.BuildConfig;
import com.robsahm.parking.R;
import com.robsahm.parking.util.json.Property;
import com.robsahm.parking.util.json.Response;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alex on 2015-11-08.
 */
public class ParkingDataReceiver extends AsyncTask<String, Void, Response> {

    private Activity activity;
    private ToastUtil toastUtil;

    public ParkingDataReceiver(Activity activity) {
        this.activity = activity;
        toastUtil = ToastUtil.getInstance(activity);
    }

    @Override
    protected Response doInBackground(String... params) {
        try {
            URL url = new URL(BuildConfig.API_URL +
                "?radius=100" +
                "&lat=" + params[0] +
                "&lng=" + params[1] +
                "&outputFormat=json" +
                "&apiKey=" + BuildConfig.API_KEY);
            return new Gson().fromJson(
                    IOUtils.toString(url.openStream()),
                    Response.class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(final Response response) {
        if (response == null || response.features.size() == 0) {
            toastUtil.show(R.string.no_parking_data, Toast.LENGTH_SHORT);
            waitForToastBeforeFinish();
            return;
        }

        if (response.features.size() == 1) {
            sendIntent(response.features.get(0).properties);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.parking_list_dialog_header);
            builder.setItems(response.toCharSequence(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sendIntent(response.features.get(i).properties);
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    activity.finish();
                }
            });
            builder.create().show();
        }
    }

    private void sendIntent(Property properties) {
        Intent calendarIntent = CalendarUtil.getCalendarIntent(activity, properties);

        if (calendarIntent != null) {
            activity.startActivity(calendarIntent);
        } else {
            waitForToastBeforeFinish();
        }
    }

    private void waitForToastBeforeFinish() {
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000); //Toast.LENGTH_SHORT
                    activity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
