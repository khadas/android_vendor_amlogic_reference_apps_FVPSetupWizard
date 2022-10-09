package com.droidlogic.setupwizardext;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {

    private List<Activity> mList = new LinkedList<>();

    public static MyApplication instance;
    public MyApplication(){};
    public synchronized static MyApplication getInstance(){
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        for (Activity activity:mList) {
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
