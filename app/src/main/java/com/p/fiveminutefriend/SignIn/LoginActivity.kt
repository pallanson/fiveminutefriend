package com.p.fiveminutefriend.SignIn

import android.os.Bundle
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.p.fiveminutefriend.MainActivity
import com.p.fiveminutefriend.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

        button_sign_in_login.setOnClickListener({
            performLogin(edit_email_login.text.toString().trim(),
                    edit_password_login.text.toString().trim())
        })
    }

     fun performLogin(emailOrUsername: String?, password: String?) {
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
}
