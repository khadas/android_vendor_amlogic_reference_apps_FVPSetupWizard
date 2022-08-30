package com.droidlogic.setupwizardext;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChannelSearchActivity extends Activity implements View.OnClickListener {
    private Button channelSkip;
    private Button btTerrestrial;
    private Button btCable;
    private Button btSatellite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_channel_search);
        channelSkip = findViewById(R.id.bt_channel_skip);
        btTerrestrial = findViewById(R.id.bt_terrestrial);
        btCable = findViewById(R.id.bt_cable);
        btSatellite = findViewById(R.id.bt_satellite);
        channelSkip.setOnClickListener(this);
        btTerrestrial.setOnClickListener(this);
        btCable.setOnClickListener(this);
        btSatellite.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_channel_skip:
            case R.id.bt_terrestrial:
            case R.id.bt_cable:
            case R.id.bt_satellite:
                Intent intent = new Intent();
                intent.setClass(ChannelSearchActivity.this, InstallAppActivity.class);
                startActivity(intent);
                ConfigAuthUtils.setNid(getContentResolver(), 65535);
                ConfigAuthUtils.setTvInfo(getContentResolver(), "1.6.1",
                        "Mozilla/5.0 (Linux; Andr0id 11; T3) AppleWebKit/537.36 " +
                                "(KHTML, like Gecko) Chrome/102.0.5005.52 " +
                                "Safari/537.36 OPR/46.0.2207.0 OMI/4.23.0.244.master " +
                                "HbbTV/1.6.1 (+DRM; Amlogic; T965D4; 1.0; 1.0; T3;) " +
                                "FVC/8.0 (Amlogic; T3;)", "https://auth.uat.freeviewplay.net");
                ConfigAuthUtils.setSigning(getContentResolver());
                ConfigAuthUtils.setReceivers(getContentResolver());
                // finish();
                break;
        }
    }
}