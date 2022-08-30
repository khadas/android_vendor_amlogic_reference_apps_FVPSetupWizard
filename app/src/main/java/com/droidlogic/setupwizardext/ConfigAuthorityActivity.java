package com.droidlogic.setupwizardext;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfigAuthorityActivity extends Activity implements View.OnClickListener{
    private Button btFinish;
    private Button btAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_authority);
        btFinish = findViewById(R.id.bt_finish);
        btAuth = findViewById(R.id.bt_auth);
        btFinish.setOnClickListener(this);
        btAuth.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_auth:
            case R.id.bt_finish:
                Intent intent = new Intent();
                intent.setClass(ConfigAuthorityActivity.this, InstallAppActivity.class);
                startActivity(intent);
                ConfigAuthUtils.setToU(getContentResolver(), true);
                // finish();
                break;
        }
    }
}