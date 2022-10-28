package com.droidlogic.setupwizardext;


import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ChannelSearchActivity extends Activity implements View.OnClickListener {
    private Button channelSkip;
    private Button btTerrestrial;
    private Button btCable;
    private Button btSatellite;
    private boolean isUK;
    private SearchDialog searchDialog;
    private TextView terrestrialNum;
    private TextView cableNum;
    private TextView satelliteNum;
    private boolean isSearch;
    private static final String FVP_ACTION = "com.android.tv.fvp.INTENT_ACTION";
    private static final String FVP_ACTION_TYPE = "scan_action";
    public static final String REQ_SCAN = "FVP_CHN_SCN";
    public static final String INTENT_REQUEST = "Request";
    private static final String MODE_SCN = "SCN";
    private static final int REQUEST_CODE_START_SETUP_ACTIVITY = 1;
    public static final String DTVKIT_INPUT_ID = "com.droidlogic.dtvkit.inputsource/.DtvkitTvInput/HW19";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_search);
        MyApplication.getInstance().addActivity(this);
        channelSkip = findViewById(R.id.bt_channel_skip);
        btTerrestrial = findViewById(R.id.bt_terrestrial);
        btCable = findViewById(R.id.bt_cable);
        btSatellite = findViewById(R.id.bt_satellite);
        terrestrialNum = findViewById(R.id.terrestrial_num);
        cableNum = findViewById(R.id.cable_num);
        satelliteNum = findViewById(R.id.satellite_num);
        channelSkip.setOnClickListener(this);
        btTerrestrial.setOnClickListener(this);
        btCable.setOnClickListener(this);
        btSatellite.setOnClickListener(this);
        isUK = getIntent().getBooleanExtra("country_tag" ,true);
        setNum();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_terrestrial:
                if (isUK) {
                    Intent intent = new Intent();
                    ComponentName componentName = new ComponentName("com.droidlogic.android.tv",
                            "com.android.tv.fvp.FvpIntentHandleActivity");
                    intent.setComponent(componentName);
                    intent.putExtra("StringsenderPackage", "com.droidlogic.setupwizardext");
                    intent.putExtra(INTENT_REQUEST, REQ_SCAN);
                    intent.putExtra("Mode", MODE_SCN);
                    startActivityForResult(intent, 1002);
                } else {
                    lunchDtvKitInputSource("terrestrial", 1001);
                }
                break;
            case R.id.bt_cable:
                lunchDtvKitInputSource("cable", 1003);
                break;
            case R.id.bt_satellite:
                lunchDtvKitInputSource("satellite", 1004);
                break;
            case R.id.bt_channel_skip:
                if (isSearch) {
                    Intent intent2 = new Intent();
                    intent2.setAction(FVP_ACTION);
                    intent2.putExtra("FVP_TYPE", FVP_ACTION_TYPE);
                    sendBroadcast(intent2);
                    if (isUK) {
                        Intent intent1 = new Intent();
                        intent1.setClass(this, InstallAppActivity.class);
                        startActivity(intent1);
                    } else {
                        MyApplication.instance.exit();
                    }
                } else {
                    searchDialog = new SearchDialog(ChannelSearchActivity.this, isUK);
                    searchDialog.showSearchDislog();
                }
                break;
        }
    }

    private void lunchDtvKitInputSource(String searchType, int requestcode) {
        TvInputManager tvInputManager = (TvInputManager) this.getSystemService(Context.TV_INPUT_SERVICE);
        TvInputInfo tvInputInfo = tvInputManager.getTvInputInfo(DTVKIT_INPUT_ID);
        Intent setupIntent = tvInputInfo.createSetupIntent();
        setupIntent.putExtra("search_type",searchType);
        startActivityForResult(setupIntent, requestcode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 || requestCode == 1002 || requestCode == 1003 ||
                requestCode == 1004) {
            isSearch = true;
            channelSkip.setText(getResources().getText(R.string.search_continue));
            setNum();
        }
    }

    private void setNum() {
        terrestrialNum.setText(String.valueOf(DbHelper.getChannelNum(this, "TYPE_DVB_T")));
        cableNum.setText(String.valueOf(DbHelper.getChannelNum(this, "TYPE_DVB_C")));
        satelliteNum.setText(String.valueOf(DbHelper.getChannelNum(this, "TYPE_DVB_S")));
    }
}