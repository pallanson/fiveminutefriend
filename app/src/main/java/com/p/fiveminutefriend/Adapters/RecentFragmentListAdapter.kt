package com.p.fiveminutefriend.Adapters

import com.squareup.picasso.Picasso
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.p.fiveminutefriend.Constants
import com.p.fiveminutefriend.Model.User
import com.p.fiveminutefriend.R
import kotlinx.android.synthetic.main.custom_item_recents.view.*
import kotlinx.android.synthetic.main.fragment_profile.*


class RecentFragmentListAdapter(private val items: List<User>,
                           private val listener: OnItemClickListener) : RecyclerView.Adapter<RecentFragmentListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: RecentFragmentListAdapter.ViewHolder?, position: Int) {
        holder!!.bind(items[position], listener)
        Picasso.get()
                .load(R.drawable.mondom)
                .resize(90, 90)
                .centerCrop()
                .into(holder.image)
        val storageReference = FirebaseStorage
                .getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_STORAGE_REFERENCE +
                        items[position]?.uid)

        storageReference.child("profilePic")
                .downloadUrl
                .addOnSuccessListener {
                    Picasso.get()
                        .load(it.toString())
                        .resize(90, 90)
                        .centerCrop()
                        .into(holder.image)
                }
    }

    interface OnItemClickListener {
        fun onItemClick(item: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_item_recents, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(recentRow: View) : RecyclerView.ViewHolder(recentRow) {

        val name = recentRow.text_name_recent_row
        val image = recentRow.image_profile_recent_row
        val friendCheck = recentRow.imageView_friend_check_recent_row

        fun bind(item: User, listener: OnItemClickListener) {
            name.text = item.firstName
            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }
}