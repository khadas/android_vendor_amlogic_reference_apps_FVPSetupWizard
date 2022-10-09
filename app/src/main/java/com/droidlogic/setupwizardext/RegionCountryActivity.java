package com.droidlogic.setupwizardext;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.droidlogic.dtvmgr.atomic.AmlDtvSubtitleAtomic;

import java.util.ArrayList;
import java.util.List;

public class RegionCountryActivity extends Activity {
    private ListView countryList;
    private TextView countryDescribe;
    private CountryAdapter countryAdapter;
    private List<String> mList = new ArrayList<>();
    public static final String REQ_TOU = "FVP_TOU_MSG";
    public static final String INTENT_REQUEST = "Request";
    public static final String UK = "United Kingdom";

    private String TAG = "RegionCountry";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_country);
        MyApplication.getInstance().addActivity(this);
        countryList = findViewById(R.id.country_list);
        countryDescribe = findViewById(R.id.country_describe);
        initData();
        countryList.setSelector(R.drawable.bt_selector);
        countryAdapter = new CountryAdapter(this, mList);
        countryList.setAdapter(countryAdapter);

        countryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, mList.get(i));
                Intent intent = new Intent();
                if (mList.get(i).equals(UK)) {
                    ComponentName componentName = new ComponentName("com.droidlogic.android.tv",
                            "com.android.tv.fvp.FvpIntentHandleActivity");
                    intent.putExtra(INTENT_REQUEST, REQ_TOU);
                    intent.putExtra("StringsenderPackage", "com.droidlogic.setupwizardext");
                    intent.putExtra("Mode", "TOU");
                    intent.setComponent(componentName);
                    startActivityForResult(intent, 1000);
                } else {
                    intent.putExtra("country_tag", false);
                    intent.setClass(RegionCountryActivity.this, ChannelSearchActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }
    private void initData() {
        mList = AmlDtvSubtitleAtomic.getINSTANCE().getCountryDisplayList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        if (requestCode == 1000) {
            Intent intent = new Intent();
            intent.putExtra("country_tag", true);
            intent.setClass(RegionCountryActivity.this, ChannelSearchActivity.class);
            startActivity(intent);
        }
    }
}