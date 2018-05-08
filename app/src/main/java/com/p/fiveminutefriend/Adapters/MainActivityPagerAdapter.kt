package com.p.fiveminutefriend.Adapters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import com.p.fiveminutefriend.MainTabs.ContactsFragment
import com.p.fiveminutefriend.MainTabs.MatchFragment
import com.p.fiveminutefriend.MainTabs.RecentFragment

/**
 * Created by martinhuynh on 05/05/2018.
 */



class MainActivityPagerAdapter(fm: android.support.v4.app.FragmentManager,
                               val numberOfTabs: Int,
                               private val bundle: Bundle) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        val fragment: Fragment = when (position) {
            0 -> MatchFragment()
            1 -> RecentFragment()
            2 -> ContactsFragment()
            else -> return null
        }

        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int {
        return numberOfTabs
    }
}

