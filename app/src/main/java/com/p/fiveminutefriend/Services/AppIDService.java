package com.p.fiveminutefriend.Services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class AppIDService extends FirebaseInstanceIdService {

    public static String myMessageToken;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FMF", "Refreshed token: " + refreshedToken);
        myMessageToken = refreshedToken;
    }

}
