package com.p.fiveminutefriend.MainTabs


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
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
import com.p.fiveminutefriend.FiltersFragment
import com.p.fiveminutefriend.Model.Match
import com.p.fiveminutefriend.R
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_match.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */

class MatchFragment : Fragment(){

    var canMatch = true
    var nextMatchTime : Long = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val database = FirebaseDatabase.getInstance().reference.child("Matches")
        val handler = Handler()
        val user = FirebaseAuth.getInstance().currentUser
        val key = user?.uid
        val delay : Long = 500
        val canMatchReference = FirebaseDatabase.getInstance().reference.child("Users").child(key).child("canMatch")
        val reference = database.child(key)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (button_match != null) {
                    if (dataSnapshot.exists()) {
                        button_match.text = "MATCHING"
                    } else {
                        button_match.text = "MATCH"
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        canMatchReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.exists()) {
                    nextMatchTime = p0?.value as Long
                    handler.postDelayed(object : Runnable {
                        override fun run() {
                            canMatch =  nextMatchTime < System.currentTimeMillis()
                            button_match?.isEnabled = canMatch
                            if (!canMatch) {
                                val timer = nextMatchTime - System.currentTimeMillis()
                                if (timer > 0) {
                                    button_match?.isEnabled = canMatch
                                    button_match?.text = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(timer) % TimeUnit.HOURS.toMinutes(1),
                                            TimeUnit.MILLISECONDS.toSeconds(timer) % TimeUnit.MINUTES.toSeconds(1))
                                    handler.postDelayed(this, delay)
                                }
                                else {
                                    button_match?.text = "MATCH"
                                }
                            }
                            else {
                                button_match?.text = "MATCH"
                            }
                        }
                    }, delay)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        return inflater?.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor: SharedPreferences.Editor = preferences.edit()
        val user = FirebaseAuth.getInstance().currentUser
        val key = user?.uid

        val userRef = FirebaseDatabase.getInstance().reference.child("Users/$key")
        userRef.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    editor.putInt("myAge", p0.child("age").value.toString().toInt())
                    editor.putInt("myGender", p0.child("gender").value.toString().toInt())
                    editor.putString("myLanguage", p0.child("language").value.toString().trim())
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })

        button_match.setOnClickListener({
            val database = FirebaseDatabase.getInstance().reference.child("Matches")

            val reference = database.child(key)
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        val defaultLanguages = arrayListOf<String>("English", "Swedish", "Language")
                        val match = Match(
                                preferences.getInt("minAge", 0),
                                preferences.getInt("maxAge", 100),
                                preferences.getInt("matchGender", 7),
                                defaultLanguages,
                                preferences.getInt("myAge", 26),
                                preferences.getInt("myGender", 2),
                                preferences.getString("myLanguage", "English"),
                                FirebaseInstanceId.getInstance().token,
                                nextMatchTime)
                        editor.apply()
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
            val filtersFragment = FiltersFragment()
            val manager = fragmentManager

            val transaction = manager.beginTransaction()
            transaction
                    .replace(R.id.layout_match,
                            filtersFragment)
                    .addToBackStack("Password Reset")
                    .commit()
        })
    }
}