package com.droidlogic.setupwizardext;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class ConfigAuthUtils {
    static String AUTHORITY          = "uk.co.freeview.fvpconfigauth.provider.FVPConfigAuth";
    static String signing_app        = "uk.co.freeview.signaidl";
    static String signing_action     = "uk.co.freeview.signaidl.SignAuthority";
    static String systemready_action = "uk.co.freeview.mdsclient.action.SYSTEM_READY";

    public static void setToU(ContentResolver resolver, boolean accept) {
        Uri uri = Uri.parse("content://" + AUTHORITY + "/tou");
        String value = "0";
        if (accept) {
            value = "1";
        }

        ContentValues values = new ContentValues();
        values.put("tou", value);
        setContentValues(resolver, uri, values);
    }

    public static void setNid(ContentResolver resolver, int nid) {
        Uri uri = Uri.parse("content://" + AUTHORITY + "/nid");
        ContentValues newValues = new ContentValues();
        newValues.put("id", 1);
        newValues.put("nid", nid);
        resolver.insert(uri, newValues);
    }

    public static void setTvInfo(ContentResolver resolver, String hbbtv_ver, String user_agent, String auth_url) {
        Uri uri = Uri.parse("content://" + AUTHORITY + "/tvinfo");

        ContentValues values = new ContentValues();
        values.put("hbbtv_version", hbbtv_ver);
        values.put("user_agent",    user_agent);
        values.put("auth_url",      auth_url);
        setContentValues(resolver, uri, values);
    }

    public static void setSigning(ContentResolver resolver) {
        Uri uri = Uri.parse("content://" + AUTHORITY + "/signing");
        ContentValues values = new ContentValues();
        values.put("application", signing_app);
        values.put("clazz",    signing_action);
        setContentValues(resolver, uri, values);
    }

    public static void setReceivers(ContentResolver resolver) {
        int i;
        Uri uri = Uri.parse("content://" + AUTHORITY + "/receivers");

        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                String type = cursor.getString(1);
                updateReceiver(resolver, uri, type, cursor.getLong(0));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Intent getSystemReadyIntent(ContentResolver resolver) {
        String packName  = "uk.co.freeview.mdsclient";
        String className = "uk.co.freeview.mdsclient.receiver.SystemBroadcastReceiver";
        Uri uri          = Uri.parse("content://" + AUTHORITY + "/systemmessages");

        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                packName  = cursor.getString(1);
                className = cursor.getString(2);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Long timstamp = System.currentTimeMillis()/1000;
        Intent intent = new Intent(systemready_action);
        intent.setClassName(packName, className);
        intent.putExtra("Type" , "fvp/system");
        intent.putExtra("Detail" , timstamp.toString());
        return intent;
    }

    private static void setContentValues(ContentResolver resolver, Uri uri, ContentValues values) {
        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                long rowId = cursor.getLong(0);
                resolver.update(uri, values, "id=" + rowId, null);
            }else {
                resolver.insert(uri, values);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    static void updateReceiver(ContentResolver resolver, Uri uri, String type, long id) {
        String where = "id=" + id;
        ContentValues newValues = new ContentValues();
        newValues.put("requesttype",    "fvp/request");

        if (type.equals("FVP_AIT_URL")) {
            newValues.put("receiver",       "com.droidlogic.android.tv");
            newValues.put("requestaction",  "com.android.tv.fvp.INTENT_ACTION");
        }else if(type.equals("FVP_TOU_MSG")) {
            newValues.put("receiver",       "com.droidlogic.android.tv");
            newValues.put("requestaction",  "com.android.tv.fvp.INTENT_ACTION");
        }else if(type.equals("FVP_CHN_SCN")) {
            newValues.put("receiver", "com.droidlogic.android.tv");
            newValues.put("requestaction", "com.android.tv.fvp.INTENT_ACTION");
        }else if(type.equals("FVP_EPG_REQ")) {
            newValues.put("receiver", "com.droidlogic.android.tv");
            newValues.put("requestaction", "com.android.tv.fvp.INTENT_ACTION");
        }else if(type.equals("FVP_LIV_REQ")) {
            newValues.put("receiver", "com.droidlogic.android.tv");
            newValues.put("requestaction", "com.android.tv.fvp.INTENT_ACTION");
        }else if(type.equals("FVP_MNR_ARE")) {
            newValues.put("receiver", "com.droidlogic.android.tv");
            newValues.put("requestaction", "com.android.tv.fvp.INTENT_ACTION");
        }else if(type.equals("FVP_CHS_RSP")) {
            newValues.put("receiver", "com.droidlogic.android.tv");
            newValues.put("requestaction", "com.android.tv.fvp.INTENT_ACTION");
        }
        resolver.update(uri, newValues, where, null);
    }
}
