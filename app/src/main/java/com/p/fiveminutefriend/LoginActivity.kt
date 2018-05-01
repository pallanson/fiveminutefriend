package com.p.fiveminutefriend

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton:Button
    private lateinit var emailOrUsername:EditText
    private lateinit var password:TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.button_sign_in_login)
        emailOrUsername = findViewById(R.id.edit_email_login)
        password = findViewById(R.id.edit_password_login)
        
    }
}
