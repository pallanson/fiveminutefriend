package com.p.fiveminutefriend.MainTabs


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.p.fiveminutefriend.Adapters.ContactsFragmentListAdapter
import com.p.fiveminutefriend.Model.Contact
import com.p.fiveminutefriend.R
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.fragment_recent.*


class ContactsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contacts = getContacts()

        val manager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL,
                false)

        recyclerview_contacts.itemAnimator = DefaultItemAnimator()
        val adapter = ContactsFragmentListAdapter(contacts, activity)
        recyclerview_contacts.adapter = adapter
        recyclerview_contacts.layoutManager = manager

    }

    private fun getContacts() : List<Contact> {
        val cursor = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC")

        cursor.moveToFirst()

        val contacts = ArrayList<Contact>()
        while(cursor.moveToNext()) {
            contacts.add(Contact(cursor.getString(cursor.getColumnIndex(ContactsContract
                            .CommonDataKinds
                            .Phone
                            .DISPLAY_NAME)),
                            cursor.getString(cursor.getColumnIndex(ContactsContract
                                    .CommonDataKinds
                                    .Phone
                                    .NUMBER))))
        }
        cursor.close()
        return contacts

    }

}// Required empty public constructor
