package com.p.fiveminutefriend.Services

import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.p.fiveminutefriend.ChatActivity

class AppMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val data = remoteMessage!!.data
        if (data["title"] == "You have a new match!") {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("matchId",data["matchId"])

            startActivity(intent)
        }
    }

}
