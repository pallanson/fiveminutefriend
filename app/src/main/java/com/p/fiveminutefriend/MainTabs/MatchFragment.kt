package com.p.fiveminutefriend.MainTabs


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
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
import android.widget.Toast
import com.p.fiveminutefriend.Model.Match

import com.p.fiveminutefriend.R
import kotlinx.android.synthetic.main.fragment_match.*

/**
 * A simple [Fragment] subclass.
 */
class MatchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val database = FirebaseDatabase.getInstance().reference.child("Matches")
        val user = FirebaseAuth.getInstance().currentUser
        val key = user!!.uid
        val reference = database.child(key)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    button_match.text = "MATCHING"
                } else {
                    button_match.text = "MATCH"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        return inflater!!.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_match.setOnClickListener({
            /*
            val intent = Intent(activity, ChatActivity::class.java)
            val options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity,
                            button_match as View,
                            "timer")

            startActivity(intent, options.toBundle())*/
            val database = FirebaseDatabase.getInstance().reference.child("Matches")
            val user = FirebaseAuth.getInstance().currentUser
            val key = user!!.uid
            val reference = database.child(key)
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val match = Match(0, 100, 7, arrayListOf("English", "Swedish", "Language"), 25, 0, "English", FirebaseInstanceId.getInstance().token)
                    if (!dataSnapshot.exists()) {
                        reference.setValue(match)
                    } else {
                        reference.removeValue()
                    }
                }

                override fun onCancelled(p0: DatabaseError?) {

                }
            })
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
