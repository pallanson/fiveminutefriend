package com.p.fiveminutefriend.MainTabs


import android.os.Bundle
import android.renderscript.Sampler
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.bassaer.chatmessageview.model.ChatUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.p.fiveminutefriend.Adapters.RecentFragmentListAdapter
import com.p.fiveminutefriend.R
import com.p.fiveminutefriend.Model.User
import kotlinx.android.synthetic.main.fragment_recent.*
import com.google.firebase.database.DataSnapshot




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

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        val chatRef = FirebaseDatabase.getInstance().reference.child("Messages/$uid")
        //var recentMatches: MutableList<User> = arrayListOf<User>()
        var recentMatches = ArrayList<User>()

        chatRef.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    for (matches in p0.children) {
                        val matchRef = FirebaseDatabase.getInstance().reference.child("Users/${matches.key}")

                        matchRef.addValueEventListener( object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val firstName = dataSnapshot.child("firstName").value.toString()
                                val lastName = dataSnapshot.child("lastName").value.toString()

                                val p0 = User(firstName, lastName)
                                Toast.makeText(activity, p0.firstName, Toast.LENGTH_LONG).show()

                                recentMatches.add(p0)
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                            }
                        })
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })

        /*chatRef.addChildEventListener( object: ChildEventListener {
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })*/

        return recentMatches
        /*listOf<User>(User("Abbey", "Anderson"),
                User("Benedict", "Benson"),
                User("Chris", "Chlamydionus"),
                User("Derek", "Dingus"),
                User("Eren", "Estaflow"),
                User("Francis", "Flomerico"),
                User("Gary", "Garrison"),
                User("Henry", "Hikeamowntin"))*/
    }

}// Required empty public constructor
