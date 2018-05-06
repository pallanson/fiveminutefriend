package com.p.fiveminutefriend

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.fragment_password_change.*

class PasswordChangeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {
        //TODO: Fix the activity (edit profile fragment) not returning after hitting back button
        if (container != null) {
            container.removeAllViews()
        }
        return inflater!!.inflate(R.layout.fragment_password_change, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var user = FirebaseAuth.getInstance().currentUser

        button_change_password.setOnClickListener({
            val email = text_email_password.text.toString().trim()
            val oldPassword = text_old_password.toString().trim()
            val newPassword = text_new_password.toString().trim()
            val newNewPassword = text_new_verify_password.toString().trim()

            //TODO: This needs to be fixed.
            var credential = EmailAuthProvider.getCredential(email, oldPassword)
            /*if (newPassword?.equals(newNewPassword)) {*/
                user!!.reauthenticate(credential)?.addOnCompleteListener({ task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Password Change Successful",Toast.LENGTH_SHORT)
                                .show()
                    } else {
                        Toast.makeText(activity, "Authentication Failed", Toast.LENGTH_SHORT)
                                .show()
                    }
                })
            } /*else {
                Toast.makeText(activity, "New Passwords Do Not Match", Toast.LENGTH_SHORT)
                        .show()
            }
        }*/)
    }
}

private fun Any.addOnCompleteListener(function: (Task<AuthResult>) -> Unit) {}
