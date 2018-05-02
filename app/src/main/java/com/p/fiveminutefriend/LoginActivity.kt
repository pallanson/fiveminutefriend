package com.p.fiveminutefriend

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton:Button
    private lateinit var emailOrUsername:EditText
    private lateinit var password:TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ///HERROOO
        val database = FirebaseDatabase.getInstance()
        val ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USERS)
        ref.child("I am Martin").setValue("HERROO")
        ///IMM HERE


        loginButton = findViewById(R.id.button_sign_in_login)
        emailOrUsername = findViewById(R.id.edit_email_login)
        password = findViewById(R.id.edit_password_login)


        loginButton.setOnClickListener({
            performLogin(emailOrUsername.text.toString().trim(),
                    password.text.toString().trim())
        })
    }

    private fun performLogin(emailOrUsername: String?, password: String?) {
        if (emailOrUsername !is String || emailOrUsername!!.isEmpty()) {
            Toast.makeText(this,
                    "email or username invalid",
                    Toast.LENGTH_SHORT).show()
            return
        }
        if (password !is String || password!!.isEmpty()) {
            Toast.makeText(this,
                    "password invalid",
                    Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(emailOrUsername, password)
                .addOnCompleteListener({

                    Toast.makeText(this,
                            FirebaseAuth.getInstance().currentUser!!.email.toString(),
                            Toast.LENGTH_SHORT).show()
                })
    }

}
