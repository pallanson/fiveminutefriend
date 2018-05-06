package com.p.fiveminutefriend

import android.app.AlertDialog
import android.support.v4.app.Fragment
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.view.ViewGroupCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //TODO: Fix the activity not returning after hitting back button
        if (container != null) {
            container.removeAllViews()
        }
        return inflater!!.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USERS)
        val firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.uid

        var username: String? = ""

        val userReference = FirebaseDatabase.getInstance().reference.child("Users").child(uid)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Convert Gender int to String
                var genderString = ""

                when (dataSnapshot.child("gender").value.toString().toInt()) {
                    0 -> genderString = "Male"
                    1 -> genderString = "Female"
                    2 -> genderString = "Other"
                }
                edit_first_name.setText(dataSnapshot.child("firstName").value.toString())
                edit_last_name.setText(dataSnapshot.child("lastName").value.toString())
                username = dataSnapshot.child("username").value.toString()
                edit_email.setText(dataSnapshot.child("email").value.toString())
                text_select_age_profile.text = dataSnapshot.child("age").value.toString()
                text_select_gender_profile.text = genderString
                text_select_language_profile.text = dataSnapshot.child("language").value.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        text_select_gender_profile.setOnClickListener({
            createNumberPicker(0)
        })
        text_select_age_profile.setOnClickListener({
            createNumberPicker(1)
        })
        text_select_language_profile.setOnClickListener({
            createNumberPicker(2)
        })

        //TODO: Fix SupportFragmentManager in order to go to another fragment
        /*text_change_password.setOnClickListener({
            val PasswordChangeFragment = PasswordChangeFragment()
            val manager = getSupportFragmentManager()

            val transaction = manager.beginTransaction()
            transaction
                    .replace(R.id.layout_profile_view, PasswordChangeFragment)
                    .addToBackStack("Change Password")
                    .commit()
        })*/

        button_save_changes.setOnClickListener({
            var genderInt = 0

            when (text_select_gender_profile.text.toString()) {
                "Male" -> genderInt = 0
                "Female" -> genderInt = 1
                "Other" -> genderInt = 2
            }
            dbReference.child(uid).setValue(
                    User(
                            uid.toString(),
                            edit_first_name.text.toString().trim(),
                            edit_last_name.text.toString().trim(),
                            username,
                            edit_email.text.toString().trim(),
                            text_select_language_profile.text.toString().trim(),
                            text_select_age_profile.text.toString().toInt(),
                            genderInt
                    )
            )
            Toast.makeText(activity, "Changes Saved", Toast.LENGTH_SHORT).show()
        })
    }

    private fun createNumberPicker(type: Int) {
        when (type) {
            0 -> {
                val genderValues = arrayOf("Male", "Female", "Other")
                val genderPicker = NumberPicker(activity)
                genderPicker.minValue = 0
                genderPicker.maxValue = genderValues.size - 1
                genderPicker.displayedValues = genderValues
                genderPicker.wrapSelectorWheel = true

                val dialogBuilder = AlertDialog.Builder(activity)
                dialogBuilder.setView(genderPicker)
                        .setTitle("Select Gender")
                        .setPositiveButton("Ok") { dialog, which ->
                            when {
                                genderPicker.value == 0 -> text_select_gender_profile.text = "Male"
                                genderPicker.value == 1 -> text_select_gender_profile.text = "Female"
                                genderPicker.value == 2 -> text_select_gender_profile.text = "Other"
                            }
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
                        }
                        .create()
                        .show()
            }
            1 -> {
                val ageValues = Array(100, { "$it" })

                val agePicker = NumberPicker(activity)
                agePicker.minValue = 1
                agePicker.maxValue = 100
                agePicker.displayedValues = ageValues
                agePicker.wrapSelectorWheel = true

                val dialogBuilder = AlertDialog.Builder(activity)
                dialogBuilder.setView(agePicker)
                        .setTitle("Select Age")
                        .setPositiveButton("Ok") { dialog, which ->
                            text_select_age_profile.text = (agePicker.value - 1).toString()
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
                        }
                        .create()
                        .show()

            }
            2 -> {
                val languageValues = arrayOf("English", "Swedish")
                val languagePicker = NumberPicker(activity)

                languagePicker.minValue = 0
                languagePicker.maxValue = languageValues.size - 1
                languagePicker.displayedValues = languageValues
                languagePicker.wrapSelectorWheel = false

                val dialogBuilder = AlertDialog.Builder(activity)
                dialogBuilder.setView(languagePicker)
                        .setTitle("Select Language")
                        .setPositiveButton(android.R.string.ok) { dialog, which ->
                            if (languagePicker.value == 0) {
                                text_select_language_profile.text = "English"
                            } else if (languagePicker.value == 1) {
                                text_select_language_profile.text = "Swedish"
                            }
                        }
                        .setNegativeButton(android.R.string.cancel) { dialog, which ->
                            Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
                        }
                        .create()
                        .show()
            }
            else -> return
        }
    }
}