package com.droidlogic.setupwizardext;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class SearchDialog extends BaseDialog implements View.OnClickListener{
    private Button SearchOK;
    private Button SearchCancle;
    private Context context;
    private boolean isUK;
    private CountrySetHelper countrySetHelper;
    public static final String UK = "gbr";
    private static final String FVP_ACTION = "com.android.tv.fvp.INTENT_ACTION";
    private static final String FVP_ACTION_TYPE = "scan_action";

    public SearchDialog(Context context, boolean isUK) {
        super(context);
        this.context = context;
        this.isUK = isUK;
        setCancelable(false);
    }

    @Override
    protected void setDialogLocation() {

    }


    @Override
    protected void initView() {
        SearchOK = findViewById(R.id.search_dialog_ok);
        SearchCancle = findViewById(R.id.search_dialog_cancle);
        SearchOK.setOnClickListener(this);
        SearchCancle.setOnClickListener(this);
        countrySetHelper = new CountrySetHelper(context);
    }

    @Override
    int getLayoutId() {
        return R.layout.dialog_search;
    }

    public void showSearchDislog() {
        if (!isShowing()) {
            show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_dialog_ok:
                Intent intent2 = new Intent();
                intent2.setAction(FVP_ACTION);
                intent2.putExtra("FVP_TYPE", FVP_ACTION_TYPE);
                context.sendBroadcast(intent2);
                if (countrySetHelper.getCurrentCountryIso3Name().equals(UK)) {
                    Intent intent1 = new Intent();
                    intent1.setClass(context, InstallAppActivity.class);
                    context.startActivity(intent1);
                } else {
                    MyApplication.instance.exit();
                }
                dismiss();
                break;
            case R.id.search_dialog_cancle:

                dismiss();
                break;
        }
    }
}
