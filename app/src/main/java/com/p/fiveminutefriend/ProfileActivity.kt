package com.p.fiveminutefriend

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val database = FirebaseDatabase.getInstance()
        val ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USERS)

        val tempUser = User(
                "123456",
                "Carl",
                "Allanson",
                "pallanson",
                "philip.allanson@gmail.com",
                "Swedish",
                26,
                2
        )

        edit_first_name.setText(tempUser.firstName)
        edit_last_name.setText(tempUser.lastName)
        usernameText.setText(tempUser.username)
        edit_email.setText(tempUser.email)
        //languageText.setText(tempUser.language)
        //edit_age.setText(tempUser.age)
        //text_gender.setText(tempUser.gender)

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