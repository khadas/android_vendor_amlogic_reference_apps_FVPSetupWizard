package com.droidlogic.setupwizardext;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.droidlogic.setupwizardext.utils.InstallUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InstallAppActivity extends Activity implements View.OnClickListener{
    private String TAG = "InstallAPP";
    private Button btInstallFinish;
    private TextView tvProcess;
    private SeekBar seekBar;
    private String[] apkFileList = null;
    private int i = 1;
    private File[] fList;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    Log.d(TAG, "seekbar");
                    tvProcess.setText(i + "/" + fList.length);
                    seekBar.setProgress(i++);
                    if (i == fList.length + 1) {
                        btInstallFinish.setFocusable(true);
                        mHandler.removeMessages(1);
                        MyApplication.instance.exit();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_app);
        MyApplication.getInstance().addActivity(this);
        seekBar = findViewById(R.id.seekBar);
        btInstallFinish = findViewById(R.id.bt_install_finish);
        tvProcess = findViewById(R.id.tv_process);
        btInstallFinish.setFocusable(false);
        btInstallFinish.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File("/product/preinstall/");
                    fList = file.listFiles();
                    Log.d(TAG, fList.length+"--");
                    for (int i = 0; i< fList.length; i++) {
                        if (fList[i].isFile()) {
                            String fileName = fList[i].getName();
                            Log.d(TAG, fileName);
                            copyFilePath(fileName);
                        }
                    }

                } catch (Exception e) {

                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_install_finish:
                MyApplication.instance.exit();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public String copyFilePath(String fileName) {

        try {
            File filesDir = this.getFilesDir();
            if (!filesDir.exists()) {
                filesDir.mkdirs();
            }
            File outFile = new File(filesDir, fileName);
            String outFilename = outFile.getAbsolutePath();
            Log.i(TAG, "outFile is " + outFilename);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    Log.e(TAG, "outFile not exist!(" + outFilename + ")");
                    return null;
                }
            }
            InputStream is = new FileInputStream("/product/preinstall/"+fileName);

            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            boolean result = InstallManager.installForP(this,outFile.getPath());
            Log.d(TAG, outFile.getPath());
            mHandler.sendEmptyMessage(1);
            return outFile.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
