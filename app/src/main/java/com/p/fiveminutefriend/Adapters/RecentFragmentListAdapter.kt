package com.p.fiveminutefriend.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.p.fiveminutefriend.R
import com.p.fiveminutefriend.Model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_item_contacts.view.*
import kotlinx.android.synthetic.main.custom_item_recents.view.*

class RecentFragmentListAdapter (private val recent : List<User>, val context: Context) :
        RecyclerView.Adapter<RecentFragmentListAdapter.RecentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        return RecentViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.custom_item_recents, parent, false))
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        val name = recent[position].firstName + " " + recent[position].lastName
        holder.name.text = name
        Picasso.get().load(R.drawable.mondom)
                .resize(90, 90)
                .centerCrop()
                .into(holder.image)
    }

    //TODO : Conditional to check whether a recent conversation is with a friend

    override fun getItemCount(): Int {
        return recent.size
    }

    class RecentViewHolder(recentRow: View) : RecyclerView.ViewHolder (recentRow) {
        val name = recentRow.text_name_recent_row
        val image = recentRow.image_profile_recent_row
        val friendCheck = recentRow.imageView_friend_check_recent_row
    }
}
