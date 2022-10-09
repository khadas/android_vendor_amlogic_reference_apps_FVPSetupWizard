package com.droidlogic.setupwizardext;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public abstract class BaseDialog extends Dialog {
    protected Context mContext;
    public BaseDialog(@NonNull Context context) {
        this(context, R.style.dialog_sen5);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (getWindow() != null)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setDialogLocation();
        initView();
    }

    protected abstract void setDialogLocation();

    protected abstract void initView();

    abstract int getLayoutId();
}
