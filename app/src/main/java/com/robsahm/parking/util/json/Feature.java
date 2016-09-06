package com.robsahm.parking.util.json;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alexrobsahm on 14/11/15.
 */
public class Feature implements Parcelable {
    private Property properties;

    public Property getProperties() {
        return properties;
    }

    protected Feature(Parcel in) {
        properties = in.readParcelable(Property.class.getClassLoader());
    }

    public static final Creator<Feature> CREATOR = new Creator<Feature>() {
        @Override
        public Feature createFromParcel(Parcel in) {
            return new Feature(in);
        }

        @Override
        public Feature[] newArray(int size) {
            return new Feature[size];
        }
    };

    public String toString() {
        return properties.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(properties, i);
    }
}
