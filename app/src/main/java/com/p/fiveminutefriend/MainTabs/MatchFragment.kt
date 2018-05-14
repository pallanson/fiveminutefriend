package com.p.fiveminutefriend.MainTabs


import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.p.fiveminutefriend.ChatActivity

import com.p.fiveminutefriend.R
import kotlinx.android.synthetic.main.fragment_match.*

/**
 * A simple [Fragment] subclass.
 */
class MatchFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_match.setOnClickListener({
            val intent = Intent(activity, ChatActivity::class.java)
            val options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity,
                            button_match as View,
                            "timer")

            startActivity(intent, options.toBundle())
        })

        fab_filter_match.setOnClickListener({
            val matchSettingsDialog = AlertDialog.Builder(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                matchSettingsDialog.setView(R.layout.dialog_match_settings)
            }
            matchSettingsDialog.setTitle("Match Settings")
                    .setPositiveButton("Ok") { _, _ ->
                        Toast.makeText(context, "Settings Changed", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel") {_, _ ->
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
        })
    }

}// Required empty public constructor
