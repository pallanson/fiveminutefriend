package com.p.fiveminutefriend

import android.app.Dialog
import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val database = FirebaseDatabase.getInstance()
        val ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USERS)

        ref.child("I am Martin").setValue("HERROO")

        button_sign_in_login.setOnClickListener({
            performLogin(edit_email_login.text.toString().trim(),
                    edit_password_login.text.toString().trim())
        })
    }

     fun performLogin(emailOrUsername: String?, password: String?) {
            startActivity(Intent(this, ChatActivity::class.java))
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
                    Toast.makeText(this,
                            FirebaseAuth.getInstance().currentUser!!.email.toString(),
                            Toast.LENGTH_SHORT).show()
                })
        }
}

