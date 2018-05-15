package com.p.fiveminutefriend.MainTabs


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import android.widget.Button
import android.widget.Toast
import com.p.fiveminutefriend.ChatActivity
import com.p.fiveminutefriend.Model.Match

import com.p.fiveminutefriend.R
import com.p.fiveminutefriend.Services.AppIDService
import com.p.fiveminutefriend.Services.AppMessagingService
import kotlinx.android.synthetic.main.fragment_match.*

/**
 * A simple [Fragment] subclass.
 */
class MatchFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_match.setOnClickListener({
            val database = FirebaseDatabase.getInstance().reference.child("Matches")
            val user = FirebaseAuth.getInstance().currentUser
            val key = user!!.uid
            val reference = database.child(key)
            val match = Match(0, 100, 7, arrayListOf("English", "Swedish", "Language"), 25, 0, "English", FirebaseInstanceId.getInstance().token);
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        reference.setValue(match)
                        button_match.text = "MATCHING"
                    } else {
                        reference.removeValue()
                        button_match.text = "MATCH"
                    }
                }

                override fun onCancelled(p0: DatabaseError?) {

                }
            })
            val intent = Intent(activity, ChatActivity::class.java)
            val options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity,
                            button_match as View,
                            "timer")

            startActivity(intent, options.toBundle())
        })

        fab_filter_match.setOnClickListener({
            val matchSettingsDialog = AlertDialog.Builder(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                matchSettingsDialog.setView(R.layout.dialog_match_settings)
            }
            matchSettingsDialog.setTitle("Match Settings")
                    .setPositiveButton("Ok") { _, _ ->
                        Toast.makeText(context, "Settings Changed", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel") {_, _ ->
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
        })
    }

}// Required empty public constructor
