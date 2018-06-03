package com.p.fiveminutefriend.MainTabs


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_match.*

/**
 * A simple [Fragment] subclass.
 */

class MatchFragment : Fragment(){

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
        var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        var editor: SharedPreferences.Editor = preferences.edit()
        val user = FirebaseAuth.getInstance().currentUser
        val key = user!!.uid

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
                                FirebaseInstanceId.getInstance().token)
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
