package com.p.fiveminutefriend

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.p.fiveminutefriend.Adapters.MainActivityPagerAdapter
import com.p.fiveminutefriend.SignIn.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_match.*


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
            fab_new_chat_main_activity.hide()
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
            val matchSettingsDialog = AlertDialog.Builder(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                matchSettingsDialog.setView(R.layout.dialog_match_settings)
            }
            matchSettingsDialog.setTitle("Match Settings")
                    .setPositiveButton("Ok") { _, _ ->
                        Toast.makeText(this, "Settings Changed", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel") {_, _ ->
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
        })
    }

    private fun performSignOut() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }

}