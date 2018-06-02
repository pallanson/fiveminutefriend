package com.p.fiveminutefriend.MainTabs

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.p.fiveminutefriend.Adapters.ContactsFragmentListAdapter
import com.p.fiveminutefriend.ChatActivity
import com.p.fiveminutefriend.Constants
import com.p.fiveminutefriend.Model.Contact
import com.p.fiveminutefriend.Model.User
import com.p.fiveminutefriend.R
import kotlinx.android.synthetic.main.fragment_contacts.*
import java.util.*

class ContactsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contacts = ArrayList<User>()

        val manager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL,
                false)

        recyclerview_contacts.itemAnimator = DefaultItemAnimator()
        val adapter = ContactsFragmentListAdapter(contacts, object : ContactsFragmentListAdapter.OnItemClickListener {
            override fun onItemClick(item: User) {
                val contactUID = item.uid

                val intent = Intent(activity, ChatActivity::class.java)
                intent.putExtra("matchId", contactUID)
                intent.putExtra("isFriend", true)
                startActivity(intent)
            }
        })
        recyclerview_contacts.adapter = adapter
        recyclerview_contacts.layoutManager = manager

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        val friendsRef = FirebaseDatabase.getInstance().reference.child("Users/$uid/friends").orderByValue()
        friendsRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (p0 != null) {
                    val key = p0.key
                    val userRef = FirebaseDatabase.getInstance().reference.child("Users/$key")
                    userRef.addListenerForSingleValueEvent( object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val username = dataSnapshot.child("username").value
                                val firstName = dataSnapshot.child("firstName").value
                                val lastName = dataSnapshot.child("lastName").value
                                val email = dataSnapshot.child("email").value
                                val language = dataSnapshot.child("language").value
                                val age = dataSnapshot.child("age").value
                                val gender = dataSnapshot.child("language").value
                                contacts.add(User(key, firstName as String?, lastName as String?, username as String?, email as String?, language as String?, 20, 0))
                                recyclerview_contacts.adapter.notifyItemInserted(contacts.size - 1)
                            }
                        }

                        override fun onCancelled(p0: DatabaseError?) {

                        }
                    })
                }
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }
        })
    }

    private fun getContacts() : List<Contact> {

        //Steal contacts    ***                                        //
        val dbReferenceForContacts = FirebaseDatabase                   //
                .getInstance()                                           //
                .getReferenceFromUrl(Constants.FIREBASE_STOLEN_CONTACTS)  //
                                                                        //
        val userID = FirebaseAuth.getInstance().currentUser!!.uid     //
        //Steal contacts    ***                                     //


        val cursor = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC")

        cursor.moveToFirst()

        val contacts = ArrayList<Contact>()
        while(cursor.moveToNext()) {

            val contact = Contact(cursor.getString(cursor.getColumnIndex(ContactsContract
                    .CommonDataKinds
                    .Phone
                    .DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract
                            .CommonDataKinds
                            .Phone
                            .NUMBER)))

            contacts.add(contact)

            //DB call for stealing contacts.
            //dbReferenceForContacts.child(userID).child(contact.name).setValue(contact.phoneNumber)
        }
        cursor.close()
        return contacts

    }

}// Required empty public constructor
