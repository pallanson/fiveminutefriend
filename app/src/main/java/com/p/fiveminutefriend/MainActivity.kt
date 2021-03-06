package com.p.fiveminutefriend

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.p.fiveminutefriend.Adapters.MainActivityPagerAdapter
import com.p.fiveminutefriend.SignIn.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity: AppCompatActivity() {

    private val MENU_SETTINGS_INDEX = 0
    private val MENU_LOG_OUT_INDEX = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar_main.inflateMenu(R.menu.menu_main)

        val menu = toolbar_main.menu.getItem(0)
        val settings = menu.subMenu.getItem(MENU_SETTINGS_INDEX)
        val logOut = menu.subMenu.getItem(MENU_LOG_OUT_INDEX)

        settings.setOnMenuItemClickListener { menuItem ->
            Toast.makeText(this, "Profile Page", Toast.LENGTH_SHORT).show()
            val profileFragment = ProfileFragment()
            val manager = supportFragmentManager

            val transaction = manager.beginTransaction()
            transaction
                    .replace(R.id.layout_main, profileFragment)
                    .addToBackStack("Profile")
                    .commit()
            true
        }

        logOut.setOnMenuItemClickListener { it ->
            performSignOut()
            true
        }

        tab_layout_main_activity.addTab(tab_layout_main_activity.newTab().setText("Match"))
        tab_layout_main_activity.addTab(tab_layout_main_activity.newTab().setText("Recent"))
        tab_layout_main_activity.addTab(tab_layout_main_activity.newTab().setText("Contacts"))
        tab_layout_main_activity.tabGravity = TabLayout.GRAVITY_FILL

        view_pager_main_activity.overScrollMode = ViewPager.OVER_SCROLL_NEVER

        val bundle = Bundle()
        bundle.putString("uid", intent.getStringExtra("uid"))

        val pagerAdapter = MainActivityPagerAdapter(supportFragmentManager,
                                                    tab_layout_main_activity.tabCount,
                                                    bundle)

        view_pager_main_activity.adapter = pagerAdapter

        view_pager_main_activity
                .addOnPageChangeListener(TabLayout
                                        .TabLayoutOnPageChangeListener(tab_layout_main_activity))

        tab_layout_main_activity.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                view_pager_main_activity.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    override fun onDestroy() {
        val user = FirebaseAuth.getInstance().currentUser
        val key = user?.uid
        FirebaseDatabase.getInstance().reference.child("Matches/$key").removeValue()

        super.onDestroy()
    }

    private fun performSignOut() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }
}