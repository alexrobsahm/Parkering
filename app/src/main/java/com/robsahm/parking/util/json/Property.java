package com.robsahm.parking.util.json;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alexrobsahm on 14/11/15.
 */
public class Property implements Parcelable {
    private String ADDRESS;
    private int START_TIME;
    private int END_TIME;
    private String START_WEEKDAY;
    private int START_MONTH;
    private int START_DAY;
    private int END_MONTH;
    private int END_DAY;

    protected Property(Parcel in) {
        ADDRESS = in.readString();
        START_TIME = in.readInt();
        END_TIME = in.readInt();
        START_WEEKDAY = in.readString();
        START_MONTH = in.readInt();
        START_DAY = in.readInt();
        END_MONTH = in.readInt();
        END_DAY = in.readInt();
    }

    public static final Creator<Property> CREATOR = new Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };

    public String toString() {
        return ADDRESS;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ADDRESS);
        parcel.writeInt(START_MONTH);
        parcel.writeInt(START_TIME);
        parcel.writeInt(END_TIME);
        parcel.writeString(START_WEEKDAY);
        parcel.writeInt(START_MONTH);
        parcel.writeInt(START_DAY);
        parcel.writeInt(END_MONTH);
        parcel.writeInt(END_DAY);
    }

    public String getAddress() {
        return ADDRESS;
    }

    public int getStartTime() {
        return START_TIME;
    }

    public int getEndTime() {
        return END_TIME;
    }

    public String getStartWeekday() {
        return START_WEEKDAY;
    }

    public int getStartMonth() {
        return START_MONTH;
    }

    public int getStartDay() {
        return START_DAY;
    }
    public int getEndMonth() {
        return END_MONTH;
    }
    public int getEndDay() {
        return END_DAY;
    }
}
