package com.robsahm.parking.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by alexrobsahm on 17/08/16.
 */
public class ToastUtil {
    private static ToastUtil instance;
    private Context context;
    private Toast toast = null;

    private ToastUtil(Context context) {
        this.context = context;
    }

    public static ToastUtil getInstance(Context context) {
        if (instance == null) {
            instance = new ToastUtil(context);
        }

        return instance;
    }

    public void show(int message, int length) {
        cancel();
        toast = Toast.makeText(context, message, length);
        toast.show();
    }

    public void show(String message, int length) {
        cancel();
        toast = Toast.makeText(context, message, length);
        toast.show();
    }

    public void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
