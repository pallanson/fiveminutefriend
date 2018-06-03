package com.p.fiveminutefriend.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.p.fiveminutefriend.Model.Contact
import com.p.fiveminutefriend.Model.User
import com.p.fiveminutefriend.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_item_contacts.view.*

class ContactsFragmentListAdapter (private val contacts : List<User>, private val listener: OnItemClickListener) :
        RecyclerView.Adapter<ContactsFragmentListAdapter.ContactViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_item_contacts, parent, false)
        return ContactsFragmentListAdapter.ContactViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder!!.bind(contacts[position], listener)
        holder.name.text = contacts[position].username
        holder.phone.text = "${contacts[position].firstName} ${contacts[position].lastName}"
        Picasso.get().load(R.drawable.mondom)
                .resize(90, 90)
                .centerCrop()
                .into(holder.image)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    class ContactViewHolder(contactRow: View) : RecyclerView.ViewHolder (contactRow) {
        val name = contactRow.text_name_contacts_row!!
        val phone = contactRow.text_phone_contacts_row!!
        val image = contactRow.image_profile_contacts_row!!

        fun bind(item: User, listener: ContactsFragmentListAdapter.OnItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }
}


