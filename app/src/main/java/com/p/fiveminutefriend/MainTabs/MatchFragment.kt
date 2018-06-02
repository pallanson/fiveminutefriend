package com.p.fiveminutefriend.MainTabs


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
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
import com.google.android.gms.location.*
import com.p.fiveminutefriend.Constants
import com.p.fiveminutefriend.Model.Match

import com.p.fiveminutefriend.R
import kotlinx.android.synthetic.main.fragment_match.*

/**
 * A simple [Fragment] subclass.
 */
class MatchFragment : Fragment() {

    private val LOCATION_REQUEST_CODE = 101

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

    @SuppressLint("MissingPermission")
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
                    val match = Match(0,
                            100,
                            7,
                            arrayListOf("English",
                                    "Swedish",
                                    "Language"),
                            25,
                            0,
                            "English",
                            FirebaseInstanceId.getInstance().token)
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
            getLocationUpdates()

            val matchSettingsDialog = AlertDialog.Builder(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                matchSettingsDialog.setView(R.layout.dialog_match_settings)
            }
            matchSettingsDialog.setTitle("Match Settings")
                    .setPositiveButton("Ok") { _, _ ->
                        Toast.makeText(context, "Settings Changed", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
        })
    }

    private fun getLocationUpdates() {
        if(Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    activity.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
                val locationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setExpirationDuration(500)
                        .setNumUpdates(1)
                        .setInterval(0)

                fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                        object: LocationCallback() {
                            override fun onLocationResult(result: LocationResult?) {
                                super.onLocationResult(result)
                                Toast.makeText(activity,
                                        "Latitude: " + result!!.lastLocation.latitude.toString() + " || Longitude: " + result.lastLocation.longitude.toString(),
                                        Toast.LENGTH_LONG).show()
                                locationToFirebase(result!!.lastLocation.latitude.toString(),
                                        result.lastLocation.longitude.toString())

                            }
                        }, null)
            } else {
                ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION),
                        LOCATION_REQUEST_CODE)
            }
        }
    }

    private fun locationToFirebase(lat : String, long : String) {

        //Todo: Uload latitude and longitude to firebase, please help me!!!!
        //Cannot remember for the life of me how to do it
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
                    Log.v(TAG, "Permission Granted by User")
                }
            }
        }

    }
}


// Required empty public constructor
