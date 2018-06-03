package com.p.fiveminutefriend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.AlertDialog
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.widget.NumberPicker
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_match_settings.*


class FiltersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.dialog_match_settings, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_select_minAge_filter.setOnClickListener({
            createNumberPicker(0)
        })
        text_select_maxAge_filter.setOnClickListener({
            createNumberPicker(1)
        })

        button_save_filters.setOnClickListener({

            var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            var editor: SharedPreferences.Editor = preferences.edit()

            var matchGender = 0
            var minAge : Int?
            var maxAge : Int?
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



            if (text_select_minAge_filter.text.toString().equals("Select")) {
                minAge = 0
            } else
                minAge = text_select_minAge_filter.text.toString().toInt()
            if (text_select_maxAge_filter.text.toString().equals("Select")) {
                maxAge = 100
            } else
                maxAge = text_select_maxAge_filter.text.toString().toInt()

            // Get Language Selection

            if (checkbox_english_match_settings.isChecked)
                matchLanguage.add("English")
            if (checkbox_swedish_match_settings.isChecked)
                matchLanguage.add("Swedish")

            editor.putInt("matchGender", matchGender)
            editor.putInt("minAge", minAge)
            editor.putInt("maxAge", maxAge)
            editor.apply()

            Toast.makeText(context, "Settings Changed", Toast.LENGTH_SHORT).show()
            fragmentManager.popBackStackImmediate()
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