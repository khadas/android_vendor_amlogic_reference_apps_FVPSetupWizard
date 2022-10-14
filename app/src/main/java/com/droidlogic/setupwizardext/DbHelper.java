package com.droidlogic.setupwizardext;

import android.content.Context;
import android.database.Cursor;
import android.media.tv.TvContract;

public class DbHelper {
    public static final String DTVKIT_INPUT_ID = "com.droidlogic.dtvkit.inputsource/.DtvkitTvInput/HW19";
    public static int getChannelNum(Context context, String type) {
        int num = 0;
        String selection = TvContract.Channels.COLUMN_TYPE + "=\'"+type+"\'";
        Cursor cursor = context.getContentResolver().query(
                TvContract.buildChannelsUriForInput(DTVKIT_INPUT_ID), null, selection, null, null
        );
        while (cursor.moveToNext()) {
            num++;
        }
        cursor.close();
        return num;
    }
}
