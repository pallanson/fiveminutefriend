package com.p.fiveminutefriend

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.view.ViewGroupCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (container != null) {
            container.removeAllViews();
        }
        return inflater!!.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USERS)
        val firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.uid

        //TODO: Set child to UID once login is connected.
        //val userReference = dbReference.child("owcn9gCAoyR279y4UEO756qey5B2")
        val userReference = FirebaseDatabase.getInstance().reference.child("Users").child("owcn9gCAoyR279y4UEO756qey5B2")

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                edit_first_name.setText(dataSnapshot.child("firstName").value.toString())
                edit_last_name.setText(dataSnapshot.child("lastName").value.toString())
                edit_email.setText(dataSnapshot.child("email").value.toString())
                edit_age.setText(dataSnapshot.child("age").value.toString())
                setLanguage()
                setGender()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        /*text_change_password.setOnClickListener({
            val PasswordChangeFragment = PasswordChangeFragment()
            val manager = getSupportFragmentManager()

            val transaction = manager.beginTransaction()
            transaction
                    .replace(R.id.layout_profile_view, PasswordChangeFragment)
                    .addToBackStack("Change Password")
                    .commit()
        })*/

        button_save_changes.setOnClickListener({
            dbReference.child(/*uid*/"QdJjQe9ZIEOd9xZUbkV5ZIJTesN2").setValue(
                    User(
                            uid.toString(),
                            edit_first_name.toString(),
                            edit_last_name.toString(),
                            null,
                            edit_email.toString(),
                            spinner_language.toString(),
                            26,//edit_age.toString().toInt(),
                            1//spinner_gender.toString().toInt()
                    )
            )
        })
    }

    fun setLanguage() {
        //TODO: Set Current Language on Picker
    }

    fun setGender() {
        //TODO: Set Current Gender on Picker
    }
}