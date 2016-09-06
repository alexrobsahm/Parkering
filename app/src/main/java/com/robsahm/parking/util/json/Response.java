package com.robsahm.parking.util.json;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by alexrobsahm on 14/11/15.
 */
public class Response implements Parcelable {
    private List<Feature> features;

    protected Response(Parcel in) {
        features = in.createTypedArrayList(Feature.CREATOR);
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(features);
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public Feature getFeature(int i) {
        return features.get(i);
    }
}
