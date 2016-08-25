package com.robsahm.parking;

import android.app.Activity;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.provider.CalendarContract;

import com.robsahm.parking.util.CalendarUtil;

public class Settings extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
            ListPreference calendarList = (ListPreference) findPreference("settings_calendar_id");
            CharSequence[][] calendarData = CalendarUtil.getCalendarData(getActivity(), new String[]{
                    CalendarContract.Calendars._ID,
                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME});
            calendarList.setEntryValues(calendarData[0]);
            calendarList.setEntries(calendarData[1]);
        }
    }
}
