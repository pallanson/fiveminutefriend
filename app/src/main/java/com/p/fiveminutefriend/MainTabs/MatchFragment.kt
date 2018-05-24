package com.p.fiveminutefriend.MainTabs


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import android.widget.Toast
import com.p.fiveminutefriend.Model.Match

import com.p.fiveminutefriend.R
import kotlinx.android.synthetic.main.dialog_match_settings.*
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
        return inflater!!.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var match : Match

        match = Match(0, 100, 7, arrayListOf("English", "Swedish", "Language"),
                25, 0, "English", FirebaseInstanceId.getInstance().token)

        text_select_minAge_filter.setOnClickListener({
            createNumberPicker(0)
        })
        text_select_maxAge_filter.setOnClickListener({
            createNumberPicker(1)
        })

        button_match.setOnClickListener({
            val database = FirebaseDatabase.getInstance().reference.child("Matches")
            val user = FirebaseAuth.getInstance().currentUser
            val key = user!!.uid
            val reference = database.child(key)
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
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
                        var matchGender = 0
                        var minAge = 0
                        var maxAge = 0
                        var matchLanguage = arrayListOf<String>()

                        // Get Gender Selection

                        if (checkbox_male_match_settings.isChecked) {
                            matchGender += 1
                        }
                        if (checkbox_female_match_settings.isChecked) {
                            matchGender += 2
                        }
                        if (checkbox_other_match_settings.isChecked) {
                            matchGender += 4
                        }
                        if (!checkbox_male_match_settings.isChecked &&
                                !checkbox_female_match_settings.isChecked &&
                                !checkbox_other_match_settings.isChecked) {
                            matchGender = 7
                        }

                        // Get Age Selection

                        minAge = text_select_minAge_filter.text.toString().toInt()
                        maxAge = text_select_maxAge_filter.text.toString().toInt()

                        // Get Language Selection

                        if (checkbox_english_match_settings.isChecked)
                            matchLanguage.add("English")
                        if (checkbox_swedish_match_settings.isChecked)
                            matchLanguage.add("Swedish")

                        match = Match(minAge, maxAge, matchGender, matchLanguage,
                                25, 0, "English", FirebaseInstanceId.getInstance().token)
                        Toast.makeText(context, "Settings Changed", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
        })
    }

    private fun createNumberPicker(type: Int) {
        when (type) {
            0 -> {
                val minAgeValues = Array<String>(100, { "$it" })

                val agePicker = NumberPicker(activity)
                agePicker.minValue = 1
                agePicker.maxValue = 100
                agePicker.displayedValues = minAgeValues
                agePicker.wrapSelectorWheel = true

                val dialogBuilder = AlertDialog.Builder(activity)
                dialogBuilder.setView(agePicker)
                        .setTitle("Select Age")
                        .setPositiveButton("Ok") { dialog, which ->
                            text_select_minAge_filter.text = (agePicker.value - 1).toString()
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
                        }
                        .create()
                        .show()

            }
            1 -> {
                val maxAgeValues = Array<String>(100, { "$it" })

                val agePicker = NumberPicker(activity)
                agePicker.minValue = 1
                agePicker.maxValue = 100
                agePicker.displayedValues = maxAgeValues
                agePicker.wrapSelectorWheel = true

                val dialogBuilder = AlertDialog.Builder(activity)
                dialogBuilder.setView(agePicker)
                        .setTitle("Select Age")
                        .setPositiveButton("Ok") { dialog, which ->
                            text_select_maxAge_filter.text = (agePicker.value - 1).toString()
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
                        }
                        .create()
                        .show()

            }
            else -> return
        }
    }
}
