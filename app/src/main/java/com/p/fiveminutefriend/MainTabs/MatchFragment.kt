package com.p.fiveminutefriend.MainTabs


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.p.fiveminutefriend.R


/**
 * A simple [Fragment] subclass.
 */
class MatchFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_match_settings, container, false)
    }

}// Required empty public constructor