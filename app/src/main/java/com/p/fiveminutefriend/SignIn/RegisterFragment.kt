package com.p.fiveminutefriend.SignIn

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.p.fiveminutefriend.*
import com.p.fiveminutefriend.Model.User
import kotlinx.android.synthetic.main.fragment_register.*


/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_create_account_register.setOnClickListener({

            if (edit_email_register.text.isBlank()
                    || edit_first_name_register.text.isBlank()
                    || edit_last_name_register.text.isBlank()
                    || edit_username_register.text.isBlank()
                    || edit_password_register.text.isBlank()) {

                Toast.makeText(activity,
                        "Please fill in all fields.",
                        Toast.LENGTH_LONG)
                        .show()
            } else if (text_age_register.text == "Select"
                    || text_gender_register.text == "Select"
                    || text_language_register.text == "Select") {
                Toast.makeText(activity,
                        "Please select an age, gender and language.",
                        Toast.LENGTH_SHORT)
                        .show()
            } else if (edit_password_register.text.length < 6){
                Toast.makeText(activity,
                        "Password cannot be shorter than six characters.",
                        Toast.LENGTH_LONG)
                        .show()
            } else {
                val confirmationDialog = AlertDialog.Builder(activity)
                confirmationDialog.setTitle("Register Account?")
                        .setMessage("You will be logged in once registering.")
                        .setPositiveButton("Register") { dialog, which ->
                            registerUser(edit_first_name_register.text.toString().trim(),
                                    edit_last_name_register.text.toString().trim(),
                                    edit_username_register.text.toString().trim(),
                                    edit_email_register.text.toString().trim(),
                                    edit_password_register.text.toString().trim(),
                                    text_select_age_register.text.toString().toInt(),
                                    text_gender_register.text.toString().trim(),
                                    text_language_register.text.toString().trim())
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
                        }
                        .create()
                        .show()
            }
        })
        text_select_gender_register.setOnClickListener({
            createNumberPicker(0)
        })
        text_select_age_register.setOnClickListener({
            createNumberPicker(1)
        })
        text_select_language_register.setOnClickListener({
            createNumberPicker(2)
        })
    }

    private fun registerUser(firstName: String,
                             lastName: String,
                             username: String,
                             email: String,
                             password: String,
                             age: Int,
                             gender: String,
                             language: String) {

        val dbReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USERS)
        val firebaseAuth = FirebaseAuth.getInstance()
        var genderInt = 0

        when (gender) {
            "Male" -> genderInt = 0
            "Female" -> genderInt = 1
            "Other" -> genderInt = 2
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener({ task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                                dbReference
                                        .child(FirebaseAuth.getInstance().uid)
                                        .setValue(
                                        User(
                                                firebaseAuth.uid.toString(),
                                                firstName,
                                                lastName,
                                                username,
                                                email,
                                                language,
                                                age,
                                                genderInt))

                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG)
                                .show()
                    }
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
                                genderPicker.value == 0 -> text_select_gender_register.text = "Male"
                                genderPicker.value == 1 -> text_select_gender_register.text = "Female"
                                genderPicker.value == 2 -> text_select_gender_register.text = "Other"
                            }
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
                        }
                        .create()
                        .show()
            }
            1 -> {
                val ageValues = Array<String>(100, { "$it" })

                val agePicker = NumberPicker(activity)
                agePicker.minValue = 1
                agePicker.maxValue = 100
                agePicker.displayedValues = ageValues
                agePicker.wrapSelectorWheel = true

                val dialogBuilder = AlertDialog.Builder(activity)
                dialogBuilder.setView(agePicker)
                        .setTitle("Select Age")
                        .setPositiveButton("Ok") { dialog, which ->
                            text_select_age_register.text = (agePicker.value - 1).toString()
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
                                text_select_language_register.text = "English"
                            } else if (languagePicker.value == 1) {
                                text_select_language_register.text = "Swedish"
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
