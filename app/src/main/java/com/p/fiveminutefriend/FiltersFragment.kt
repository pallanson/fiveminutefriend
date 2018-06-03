package com.p.fiveminutefriend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.AlertDialog
import android.content.ContentValues
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dialog_match_settings.*

class FiltersFragment : Fragment() {

    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.dialog_match_settings, container, false)
    }

    private var LOCATION_REQUEST_CODE = 101

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Location Scrape
        getLocationUpdates()

        text_select_minAge_filter.setOnClickListener({
            createNumberPicker(0)
        })
        text_select_maxAge_filter.setOnClickListener({
            createNumberPicker(1)
        })

        button_save_filters.setOnClickListener({

            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val editor: SharedPreferences.Editor = preferences.edit()

            var matchGender = 0
            val minAge : Int?
            val maxAge : Int?
            val matchLanguage = arrayListOf<String>()

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

    private fun getLocationUpdates() {
        if(Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    activity.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
                val locationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setExpirationDuration(2000)
                        .setNumUpdates(5)
                        .setInterval(0)

                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult ?: return
                        for (location in locationResult.locations){
                            locationToFirebase(
                                    location.longitude.toString(),
                                    location.latitude.toString())
                        }
                    }
                }
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

            } else {
                ActivityCompat.requestPermissions(activity,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION),
                        LOCATION_REQUEST_CODE)
            }
        }
    }

    private fun locationToFirebase(lat : String, long : String) {

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_USERS)
                .child(userId)
                .child("latitude")
                .push()
                .setValue(lat)
        FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_USERS)
                .child(userId)
                .child("longitude")
                .push()
                .setValue(long)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            LOCATION_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity,
                            "To get better matches, please accept location permissions",
                            Toast.LENGTH_SHORT).show()
                }
                else {
                    Log.v(ContentValues.TAG, "Permission Granted by User")
                }
            }
        }

    }
}