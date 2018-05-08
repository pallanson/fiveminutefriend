package com.p.fiveminutefriend.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.p.fiveminutefriend.Model.Contact
import com.p.fiveminutefriend.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_item_contacts.view.*

class ContactsFragmentListAdapter (private val contacts : List<Contact>, val context: Context) :
        RecyclerView.Adapter<ContactsFragmentListAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.custom_item_contacts, parent, false))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.name.text = contacts[position].name
        holder.phone.text = contacts[position].phoneNumber
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
    }
}


