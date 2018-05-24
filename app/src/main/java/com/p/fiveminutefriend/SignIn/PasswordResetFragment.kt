package com.p.fiveminutefriend.SignIn

import android.support.v4.app.Fragment
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.p.fiveminutefriend.R
import kotlinx.android.synthetic.main.fragment_password_reset.*

class PasswordResetFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_password_reset, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAuth = FirebaseAuth.getInstance()

        button_reset.setOnClickListener {
            val email = edit_email_reset.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(activity, "No -mail entered.", Toast.LENGTH_SHORT).show()
            } else {
                mAuth!!.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "Check your email to reset your password!",
                                        Toast.LENGTH_SHORT).show()
                                fragmentManager.popBackStackImmediate()
                            } else {
                                Toast.makeText(activity, "Failed to send password reset email!",
                                        Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        }

        toolbar_reset.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        toolbar_reset.setNavigationOnClickListener {
            fragmentManager.popBackStackImmediate()
        }
    }
}