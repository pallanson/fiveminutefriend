package com.p.fiveminutefriend

import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.*
import com.p.fiveminutefriend.SignIn.LoginActivity
import kotlinx.android.synthetic.main.fragment_password_change.*


class PasswordChangeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_password_change, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        button_change_password.setOnClickListener({
            val email = text_email_password.text.toString().trim()
            val oldPassword = text_old_password.text.toString().trim()
            val newPassword = text_new_password.text.toString().trim()
            val newNewPassword = text_new_verify_password.text.toString().trim()

            if (newPassword == newNewPassword) {
                FirebaseAuth.getInstance().currentUser!!.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast
                                        .makeText(activity, "Reset password successfully! Please login again", Toast.LENGTH_SHORT)
                                        .show()
                                val intent = Intent(activity, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(activity, "The new passwords do NOT match.", Toast.LENGTH_SHORT)
                                        .show()
                            }
                        }
            }
        })

        toolbar_password_change.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        toolbar_password_change.setNavigationOnClickListener {
            fragmentManager.popBackStackImmediate()
        }
    }
}
