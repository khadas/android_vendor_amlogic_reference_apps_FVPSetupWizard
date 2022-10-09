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

    public SearchDialog(Context context) {
        super(context);
        this.context = context;
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
                Intent intent = new Intent();
                intent.setClass(mContext, InstallAppActivity.class);
                context.startActivity(intent);
                dismiss();
                break;
            case R.id.search_dialog_cancle:
                dismiss();
                break;
        }
    }
}
