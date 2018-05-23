package com.p.fiveminutefriend

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

class ContactProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_contact_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var uid = String()
        val bundle = this.arguments
        if (bundle != null) {
            uid = bundle.getString("uid", "null")
        }
        Toast.makeText(activity, "$uid", Toast.LENGTH_LONG).show()
    }
}