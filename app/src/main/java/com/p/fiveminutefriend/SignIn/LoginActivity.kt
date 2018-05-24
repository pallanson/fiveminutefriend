package com.p.fiveminutefriend.SignIn

import android.Manifest
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.p.fiveminutefriend.LoginPreferences
import com.p.fiveminutefriend.MainActivity
import com.p.fiveminutefriend.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val CONTACTS_REQUEST_CODE = 101
    private val TAG = "Permissions"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        isPermissionGranted()

        var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor: SharedPreferences.Editor = preferences.edit()
        var saveLogin = preferences.getBoolean("saveLogin", false)

        // If savedLogin preference is true, fill in Login information
        if (saveLogin) {
            val name = preferences.getString("username", "")
            val pass = preferences.getString("password", "")
            edit_email_login.setText(name)
            edit_password_login.setText(pass)
            checkBox_remember_login.setChecked(true)
        }

        text_register_login.setOnClickListener({
            val registerFragment = RegisterFragment()
            val manager = supportFragmentManager

            val transaction = manager.beginTransaction()
            transaction
                    .replace(R.id.layout_login_content_login,
                            registerFragment)
                    .addToBackStack("Register")
                    .commit()
        })

        text_reset_password.setOnClickListener({
            val passwordResetFragment = PasswordResetFragment()
            val manager = supportFragmentManager

            val transaction = manager.beginTransaction()
            transaction
                    .replace(R.id.layout_login_content_login,
                            passwordResetFragment)
                    .addToBackStack("Password Reset")
                    .commit()
        })

        button_sign_in_login.setOnClickListener({
            // If 'Remember Me' is checked, save information to SavedPreferences
            if (checkBox_remember_login.isChecked()) {
                editor.putBoolean("saveLogin", true)
                editor.putString("username", edit_email_login.text.toString())
                editor.putString("password", edit_password_login.text.toString())
                editor.apply()
            } else {
                // Clear SavedPreferences if checkbox is unchecked
                editor.clear()
                editor.apply()
            }
            performLogin()
        })
    }

     fun performLogin() {
         val emailOrUsername = edit_email_login.text.toString()
         val password = edit_password_login.text.toString()

         if (emailOrUsername !is String || emailOrUsername!!.isEmpty()) {
                Toast.makeText(this,
                        "Invalid Username or Email",
                        Toast.LENGTH_SHORT).show()
                return
            }

            if (password !is String || password!!.isEmpty()) {
                Toast.makeText(this,
                        "Invalid Password",
                        Toast.LENGTH_SHORT).show()
                return
            }

            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(emailOrUsername, password)
                .addOnCompleteListener({
                    if (it.isSuccessful) {
                        Toast.makeText(this,
                                FirebaseAuth.getInstance().currentUser!!.email.toString(),
                                Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("uid", FirebaseAuth.getInstance().currentUser!!.uid)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this, "Incorrect Username or Password",
                                Toast.LENGTH_SHORT).show()
                    }
                })
        }

    private fun isPermissionGranted() : Boolean {
        return if(Build.VERSION.SDK_INT >= 23) {
            if(this.checkSelfPermission(android.Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED &&
                    this.checkSelfPermission(android.Manifest.permission.WRITE_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_CONTACTS),
                        CONTACTS_REQUEST_CODE)
                false
            }
        }
        else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            CONTACTS_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission Denied by User")
                }
                else {
                    Log.v(TAG, "Permission Granted by User")
                }
            }
        }

    }


}

