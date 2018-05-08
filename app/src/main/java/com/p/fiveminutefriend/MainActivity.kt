package com.p.fiveminutefriend

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
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
            Toast.makeText(this, "Go to settings", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("uid", FirebaseAuth.getInstance().currentUser!!.uid)
            startActivity(intent)
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

        fab_new_chat_main_activity.setOnClickListener({
            startActivity(Intent(this, ChatActivity::class.java))
        })
    }

    private fun performSignOut() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }

}