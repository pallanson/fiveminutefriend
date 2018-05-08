package com.p.fiveminutefriend.MainTabs


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.p.fiveminutefriend.Adapters.RecentFragmentListAdapter
import com.p.fiveminutefriend.R
import com.p.fiveminutefriend.Model.User
import kotlinx.android.synthetic.main.fragment_recent.*


class RecentFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_recent, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val users = createTempList()
        val manager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL,
                false)

        recyclerview_recent.itemAnimator = DefaultItemAnimator()
        val adapter = RecentFragmentListAdapter(users, activity)
        recyclerview_recent.adapter = adapter
        recyclerview_recent.layoutManager = manager
    }

    private fun createTempList(): List<User> {
        return listOf<User>(User("Abbey", "Anderson"),
                User("Benedict", "Benson"),
                User("Chris", "Chlamydionus"),
                User("Derek", "Dingus"),
                User("Eren", "Estaflow"),
                User("Francis", "Flomerico"),
                User("Gary", "Garrison"),
                User("Henry", "Hikeamowntin"))
    }

}// Required empty public constructor
