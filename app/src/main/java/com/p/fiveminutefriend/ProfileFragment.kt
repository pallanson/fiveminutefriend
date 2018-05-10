package com.p.fiveminutefriend

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = FirebaseAuth.getInstance().uid
        val userReference = FirebaseDatabase.getInstance().reference.child("Users").child(uid)

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var genderString = ""

                when (dataSnapshot.child("gender").value.toString().toInt()) {
                    0 -> genderString = "Male"
                    1 -> genderString = "Female"
                    2 -> genderString = "Other"
                }

                var firstName = dataSnapshot.child("firstName").value.toString()
                var lastName = dataSnapshot.child("lastName").value.toString()
                var username = dataSnapshot.child("username").value.toString()
                var email = dataSnapshot.child("email").value.toString()
                var age = dataSnapshot.child("age").value.toString()
                var language = dataSnapshot.child("language").value.toString()


                text_name_profile.text = firstName + " " + lastName
                text_username_profile.text = username
                text_email_profile.text = email
                //TODO: Replace hardcoded string with date when implemented
                text_birthday_profile.text = "February 3, 1992 (" + age + " years old)"
                text_gender_profile.text = genderString
                text_language_profile.text = language
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "Error Retrieving Profile", Toast.LENGTH_SHORT)
            }
        })

        button_edit_profile.setOnClickListener({
            val editProfileFragment = EditProfileFragment()
            val manager = fragmentManager

            val transaction = manager.beginTransaction()
            transaction
                    .replace(R.id.layout_profile_view, editProfileFragment)
                    .addToBackStack("Edit")
                    .commit()
        })

        toolbar_profile.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar_profile.setNavigationOnClickListener {
            //fab_new_chat_main_activity.show()
            fragmentManager.popBackStackImmediate()
        }
    }
}