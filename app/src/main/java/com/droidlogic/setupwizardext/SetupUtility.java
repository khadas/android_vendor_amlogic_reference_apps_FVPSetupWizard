package com.droidlogic.setupwizardext;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class SetupUtility {

    private static final String EXTRA_THEME = "theme";

    // Google Wizard Manager Constants
    private static final String ACTION_NEXT = "com.android.wizard.NEXT";
    private static final String EXTRA_ACTION_ID = "actionId";
    private static final String EXTRA_SCRIPT_URI = "scriptUri";
    private static final String EXTRA_RESULT_CODE = "com.android.setupwizard.ResultCode";

    // GoogleSetupWizard
    private static final String GOOGLE_SETUP_PACKAGE = "com.google.android.setupwizard";
    private static final String GOOGLE_SETUP_HOME = "com.google.android.setupwizard.SetupWizardActivity";

    public static void onNext(Intent intent, Activity activity, int resultCode) {
        Intent nextIntent = new Intent(ACTION_NEXT);
        nextIntent.putExtra(EXTRA_SCRIPT_URI, intent.getStringExtra(EXTRA_SCRIPT_URI));
        nextIntent.putExtra(EXTRA_ACTION_ID, intent.getStringExtra(EXTRA_ACTION_ID));
        nextIntent.putExtra(EXTRA_THEME, intent.getStringExtra(EXTRA_THEME));
        nextIntent.putExtra(EXTRA_RESULT_CODE, resultCode);
        activity.startActivityForResult(nextIntent, 10000); //Request Code doesn't really matter
    }
}
