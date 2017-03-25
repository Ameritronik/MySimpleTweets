package com.codepath.apps.mysimpletweets.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.codepath.apps.mysimpletweets.activities.TimelineActivity;

import java.io.IOException;

/**
 * Created by hkanekal on 3/17/2017.
 */

public class checknetwork {
    Context mContext;
    public checknetwork(Context mContext) {
        this.mContext = mContext;
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        return false;
    }

    public boolean HaveCloud() {
        boolean IsNetworkAvailable = this.isNetworkAvailable();
        boolean Am_I_On_Line = this.isOnline();
        String NetworkErrorMessage="";
        if( !IsNetworkAvailable ) {
            NetworkErrorMessage = "Check for network: Please turn on WiFi.";
            new TimelineActivity().showToast(mContext, NetworkErrorMessage);
        } else if (!Am_I_On_Line) {
            NetworkErrorMessage = "No access to cloud. Check for SSIDs";
            new TimelineActivity().showToast(mContext, NetworkErrorMessage);
        }
        return (IsNetworkAvailable && Am_I_On_Line );
    }
}
