package com.p.fiveminutefriend

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val uid = FirebaseAuth.getInstance().uid
        val userReference = FirebaseDatabase.getInstance().reference.child("Users").child(uid)

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var firstName = dataSnapshot.child("firstName").value.toString()
                var lastName = dataSnapshot.child("lastName").value.toString()
                var username = dataSnapshot.child("username").value.toString()
                var email = dataSnapshot.child("email").value.toString()
                var age = dataSnapshot.child("age").value.toString()


                text_name_profile.setText(firstName + " " + lastName)
                text_username_profile.setText(username)
                text_email_profile.setText(email)
                //TODO: Replace hardcoded string with date when implemented
                text_birthday_profile.setText("February 3, 1992 (" + age + " years old)")
                //languageText.setText(tempUser.language)
                //text_gender.setText(tempUser.gender)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })



        button_edit_profile.setOnClickListener({
            val EditProfileFragment = EditProfileFragment()
            val manager = supportFragmentManager

            val transaction = manager.beginTransaction()
            transaction
                    .replace(R.id.layout_profile_view, EditProfileFragment)
                    .addToBackStack("Edit Profile")
                    .commit()
        })
    }
}