package com.robsahm.parking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.robsahm.parking.util.CalendarUtil;
import com.robsahm.parking.util.json.Feature;
import com.robsahm.parking.util.json.Response;

public class DialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Intent intent = getIntent();
        final Response response = intent.getParcelableExtra("response");
        ArrayAdapter<Feature> arrayAdapter = new ArrayAdapter<Feature>(this, android.R.layout.simple_list_item_1, response.getFeatures());
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent calendarIntent = CalendarUtil.getCalendarIntent(getApplicationContext(), response.getFeature(i).getProperties());

                if (calendarIntent != null) {
                    startActivity(calendarIntent);
                }

                finish();
            }
        });
    }
}
