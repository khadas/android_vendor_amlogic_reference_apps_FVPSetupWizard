package com.droidlogic.setupwizardext;


import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class InstallManager {

    private static final String TAG = "InstallManager";

    /*******************************************************  android P 静默安装适配 *****************************************************************/
    private final static String ACTION = "wait.install.result";
    private final static SynchronousQueue<Intent> mInstallResults = new SynchronousQueue<>();

    /**
     * @param context
     * @param filePath
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static synchronized boolean installForP(Context context, final String filePath) {

        boolean success = false;
        File apkFile = new File(filePath);
        PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams sessionParams = new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
        sessionParams.setSize(apkFile.length());
        int sessionId = createSession(packageInstaller, sessionParams);

        if (sessionId != -1) {
            boolean copySuccess = copyInstallFile(packageInstaller, sessionId, filePath);

            if (copySuccess) {
                boolean installSuccess = execInstallCommand(context, packageInstaller, sessionId, filePath);
                if (installSuccess) {
                    success = true;
                }
            }
        }
        return success;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static int createSession(PackageInstaller packageInstaller, PackageInstaller.SessionParams sessionParams) {
        int sessionId = -1;
        try {
            sessionId = packageInstaller.createSession(sessionParams);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionId;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean copyInstallFile(PackageInstaller packageInstaller, int sessionId, String apkFilePath) {

        InputStream in = null;
        OutputStream out = null;
        PackageInstaller.Session session = null;
        boolean success = false;
        try {
            File apkFile = new File(apkFilePath);
            session = packageInstaller.openSession(sessionId);
            out = session.openWrite("base.apk", 0, apkFile.length());
            in = new FileInputStream(apkFile);
            int total = 0, c;
            byte[] buffer = new byte[65536];
            while ((c = in.read(buffer)) != -1) {
                total += c;
                out.write(buffer, 0, c);
            }
            session.fsync(out);
            Log.i(TAG, "streamed " + total + " bytes");
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            session.close();
        }
        return success;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean execInstallCommand(Context context, PackageInstaller packageInstaller, int sessionId, String filePath) {
        PackageInstaller.Session session = null;
        boolean success = false;
        try {
            session = packageInstaller.openSession(sessionId);
            Intent intent = new Intent();
            intent.setAction(ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            session.commit(pendingIntent.getIntentSender());

            final Intent result = mInstallResults.poll(2, TimeUnit.SECONDS);

            if (result != null) {
                final int status = result.getIntExtra(PackageInstaller.EXTRA_STATUS,
                        PackageInstaller.STATUS_FAILURE);

                if (status == PackageInstaller.STATUS_SUCCESS) {
                    success = true;
                } else {
                    Log.e(TAG, result.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE));
                }
            } else {
                success = false;
            }

        } catch (Exception e) {
            e.printStackTrace();


        } finally {
            session.close();
        }

        return success;
    }


}
