package com.p.fiveminutefriend.Services

import android.util.Log

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class AppIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("FMF", "Refreshed token: " + refreshedToken!!)
        myMessageToken = refreshedToken
    }

    companion object {
        var myMessageToken: String? = null
    }

}
