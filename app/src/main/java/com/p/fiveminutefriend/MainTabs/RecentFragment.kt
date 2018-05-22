package com.p.fiveminutefriend.MainTabs


import android.os.Bundle
import android.renderscript.Sampler
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val recentRef = FirebaseDatabase.getInstance().reference.child("Users/$uid/matches")
        val recentMatches = ArrayList<User>()

        recentRef.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (matches in dataSnapshot.children) {
                        val matchRef = FirebaseDatabase.getInstance().reference.child("Users/$matches")

                        matchRef.addValueEventListener( object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    val recentsIterator = dataSnapshot.children.iterator()

                                    while (recentsIterator.hasNext()) {
                                        val contactsSnapshot = recentsIterator.next()

                                        val matchedUser = User(
                                                contactsSnapshot.child("uid").value.toString(),
                                                contactsSnapshot.child("firstName").value.toString(),
                                                contactsSnapshot.child("lastName").value.toString(),
                                                contactsSnapshot.child("username").value.toString(),
                                                contactsSnapshot.child("email").value.toString(),
                                                contactsSnapshot.child("language").value.toString(),
                                                //TODO: Figure out how to pass in information from Firebase as Int and not Any?
                                                26,
                                                0
                                        )
                                        recentMatches.add(matchedUser)
                                    }
                                }
                            }
                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })
                    }
                    /*theirUser = ChatUser(1, dataSnapshot.child("username").value.toString(), userIcon)
                    canChat = dataSnapshot.hasChild("matches/$uid")*/
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
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
