package com.p.fiveminutefriend.MainTabs


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.renderscript.Sampler
import android.support.v4.app.Fragment
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ListView
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

    lateinit var testCheckbox: CheckBox

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
                        val defaultLanguages = setOf<String>("English", "Swedish", "Language")
                        val match = Match(
                                preferences.getInt("minAge", 0),
                                preferences.getInt("maxAge", 100),
                                preferences.getInt("matchGender", 7),
                                preferences.getStringSet("languages", defaultLanguages) as List<String>,
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
            val genderList = arrayOf<CharSequence>("English", "Swedish", "Language")
            val genderChecked = BooleanArray(false, false, false)

            val matchSettingsDialog = AlertDialog.Builder(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                matchSettingsDialog.setView(R.layout.dialog_match_settings)
            }

            matchSettingsDialog.setMultiChoiceItems(genderList, genderChecked, {
                fun onClick(dialogInterface: DialogInterface, id : Int, checked : Boolean) {

                }
            })

            var matchGender = 0
            var minAge = 0
            var maxAge = 0
            var matchLanguage = arrayListOf<String>()
            matchSettingsDialog.setTitle("Match Settings")
                    .setPositiveButton("Ok", {
                                    if (id == 0) {
                                        matchGender += 1
                                    }
                                    if (id == 1) {
                                        matchGender += 2
                                    }
                                    if (id == 2) {
                                        matchGender += 4
                                    }
                                    if(id != 0 || id != 1 || id != 2)
                                        matchGender = 7
                            }) { _, _ ->
                        var matchGender = 0
                        var minAge = 0
                        var maxAge = 0
                        var matchLanguage = arrayListOf<String>()

                        // Get Gender Selection
                        /*if (checkbox_male_match_settings.isChecked) {
                            matchGender += 1
                        } else
                            matchGender += 0
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
                        }*/

                        // Get Age Selection

                        text_select_minAge_filter.setOnClickListener({
                            createNumberPicker(0)
                        })
                        text_select_maxAge_filter.setOnClickListener({
                            createNumberPicker(1)
                        })

                        minAge = text_select_minAge_filter.text.toString().toInt()
                        maxAge = text_select_maxAge_filter.text.toString().toInt()

                        // Get Language Selection

                        if (checkbox_english_match_settings.isChecked)
                            matchLanguage.add("English")
                        if (checkbox_swedish_match_settings.isChecked)
                            matchLanguage.add("Swedish")

                        editor.putInt("matchGender", matchGender)
                        editor.putInt("minAge", minAge)
                        editor.putInt("maxAge", maxAge)
                        editor.putStringSet("languages", matchLanguage as Set<String>)
                        editor.apply()

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
