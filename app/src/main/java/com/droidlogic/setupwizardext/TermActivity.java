package com.droidlogic.setupwizardext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.droidlogic.stv.service.fvp.FVPManager;

import java.util.ArrayList;

public class TermActivity extends Activity implements View.OnClickListener {
    private Button termAccept;
    private Button termSkip;
    private Button termDetail;
    private FVPManager fvpManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getInstance().addActivity(this);
        termAccept = findViewById(R.id.bt_term_accept);
        termSkip = findViewById(R.id.bt_term_skip);
        termDetail = findViewById(R.id.bt_term_detail);
        termAccept.setOnClickListener(this);
        termSkip.setOnClickListener(this);
        termDetail.setOnClickListener(this);

        fvpManager = new FVPManager("FVPManager");
        final Uri CONTENT_URI = Uri.parse("content://com.google.android.tungsten.setupwraith.locales/localeprefs");

        ArrayList<String> preferredLocalesList = new ArrayList<>();

        // Perform any logic necessary to determine the list of preferred locales here. This could be things
        // such as reading the "ro.oem.key" system property, or determining user location.

        preferredLocalesList.add("es-ES");
        preferredLocalesList.add("en-US");
        preferredLocalesList.add("ko-KR");

        for (int rank = 0; rank < preferredLocalesList.size(); rank++) {
            String localeTag = preferredLocalesList.get(rank);

            ContentValues values = new ContentValues();
            values.put("locale", localeTag);
            values.put("rank", rank + 1);

            getContentResolver().insert(CONTENT_URI, values);
        }

        // Hide the other supported locales.
        ContentValues values = new ContentValues();
        values.put("locale", "*");
        values.put("rank", 1);
        getContentResolver().insert(CONTENT_URI, values);


        String url1 =
                "content://com.google.android.tungsten.setupwraith/setup_provider";
        Uri CONTENT_URI1 = Uri.parse(url1);

        try {
            Cursor cursor = TermActivity.this.getContentResolver().query(CONTENT_URI1, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();
                @SuppressLint("Range") String countryCode = cursor.getString(cursor.getColumnIndex("manually_selected_country"));
            } else {
                // shouldn't happen
            }
        } catch (Exception ex) {
            return ;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_term_accept:
            case R.id.bt_term_detail:
                ConfigAuthUtils.setToU(getContentResolver(), true);
                fvpManager.setTouStatus(1);
                Intent intent = new Intent();
                intent.setClass(TermActivity.this, ChannelSearchActivity.class);
                startActivity(intent);
		        // finish();
                // SetupUtility.onNext(getIntent(), this, Activity.RESULT_OK);
                break;
            case R.id.bt_term_skip:
                ConfigAuthUtils.setToU(getContentResolver(), false);
                fvpManager.setTouStatus(0);
                Intent intent2 = new Intent();
                intent2.setClass(TermActivity.this, ChannelSearchActivity.class);
                startActivity(intent2);
                break;
        }
    }

}