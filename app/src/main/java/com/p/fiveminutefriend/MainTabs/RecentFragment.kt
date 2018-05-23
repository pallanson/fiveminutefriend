package com.p.fiveminutefriend.MainTabs


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

                val contactProfileFragment = ContactProfileFragment()
                val bundle = Bundle()
                bundle.putString("uid", contactUID)
                //contactProfileFragment.arguments(bundle)
                val manager = fragmentManager
                val transaction = manager.beginTransaction()
                transaction
                        .replace(R.id.layout_recent, contactProfileFragment)
                        .addToBackStack("View Contact")
                        .commit()
            }
        })
    }

    private fun createTempList(): List<User> {

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        val chatRef = FirebaseDatabase.getInstance().reference.child("Messages/$uid")
        var recentMatches = ArrayList<User>()

        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    for (matches in p0.children) {
                        val matchRef = FirebaseDatabase.getInstance().reference.child("Users/${matches.key}")

                        matchRef.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val firstName = dataSnapshot.child("firstName").value.toString()
                                val lastName = dataSnapshot.child("lastName").value.toString()

                                val p0 = User(firstName, lastName)
                                recentMatches.add(p0)
                                recyclerview_recent.adapter.notifyItemInserted(recentMatches.size - 1)
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
        return recentMatches
    }
}
