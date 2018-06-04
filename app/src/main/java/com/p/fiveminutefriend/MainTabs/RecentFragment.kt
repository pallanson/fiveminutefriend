package com.p.fiveminutefriend.MainTabs


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.p.fiveminutefriend.Adapters.RecentFragmentListAdapter
import com.p.fiveminutefriend.R
import com.p.fiveminutefriend.Model.User
import kotlinx.android.synthetic.main.fragment_recent.*
import com.google.firebase.database.DataSnapshot
import com.p.fiveminutefriend.ChatActivity
import com.p.fiveminutefriend.ContactProfileFragment


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
        recyclerview_recent.layoutManager = manager
        recyclerview_recent.adapter = RecentFragmentListAdapter(users, object : RecentFragmentListAdapter.OnItemClickListener {
            override fun onItemClick(item: User) {
                val contactUID = item.uid

                val intent = Intent(activity, ChatActivity::class.java)
                intent.putExtra("matchId", contactUID)
                startActivity(intent)
            }
        })
    }

    private fun addUser(list : ArrayList<User>, set : HashSet<User>, user : User){
        if (!set.contains(user)) {
            list.add(user)
            recyclerview_recent!!.adapter.notifyItemInserted(list.size - 1)
        }
    }

    private fun createTempList(): List<User> {

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        val chatRef = FirebaseDatabase.getInstance().reference.child("Messages")
        var matchesList = ArrayList<User>()
        var recentMatches = HashSet<User>()

        chatRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (p0 != null) {
                    if (p0.key != uid && p0.hasChild(uid)){
                        val matchRef = FirebaseDatabase.getInstance().reference.child("Users/${p0.key}")
                        matchRef.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val matchUID = dataSnapshot.child("uid").value.toString()
                                val firstName = dataSnapshot.child("firstName").value.toString()
                                val lastName = dataSnapshot.child("lastName").value.toString()

                                addUser(matchesList, recentMatches, User(matchUID, firstName, lastName))
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                            }
                        })
                    }
                }
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }
        })

        return matchesList
    }
}
