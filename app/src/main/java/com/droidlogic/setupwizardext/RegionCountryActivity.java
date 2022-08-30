package com.droidlogic.setupwizardext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class RegionCountryActivity extends Activity {
    private ListView countryList;
    private TextView countryDescribe;
    private CountryAdapter countryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_country);
        countryList = findViewById(R.id.country_list);
        countryDescribe = findViewById(R.id.country_describe);
        countryList.setSelector(R.drawable.bt_selector);
        countryAdapter = new CountryAdapter(this);
        countryList.setAdapter(countryAdapter);

        countryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(RegionCountryActivity.this, TermActivity.class);
                startActivity(intent);
                // finish();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }

}