package com.p.fiveminutefriend

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.zip.Inflater

/**
 * Created by Phil on 5/3/2018.
 */
class PasswordChangeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {


        return inflater!!.inflate(R.layout.fragment_password_change, container, false)
    }
}