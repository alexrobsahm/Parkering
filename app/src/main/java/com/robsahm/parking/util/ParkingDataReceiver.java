package com.robsahm.parking.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.robsahm.parking.BuildConfig;
import com.robsahm.parking.R;
import com.robsahm.parking.util.json.Response;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alex on 2015-11-08.
 */
public class ParkingDataReceiver extends AsyncTask<String, Void, Response> {

    private Context context;
    private ToastUtil toastUtil;

    public ParkingDataReceiver(Context context) {
        this.context = context;
        toastUtil = ToastUtil.getInstance(context);
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
        if (response == null) {
            toastUtil.show(R.string.no_parking_data, Toast.LENGTH_LONG);
            return;
        }

        if (response.features.size() == 1) {
            context.startActivity(CalendarUtil.getCalendarIntent(context, response.features.get(0).properties));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Vart står du parkerad?");
            builder.setItems(response.toCharSequence(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    context.startActivity(CalendarUtil.getCalendarIntent(context, response.features.get(i).properties));
                }
            });
            builder.create().show();
        }
    }
}
