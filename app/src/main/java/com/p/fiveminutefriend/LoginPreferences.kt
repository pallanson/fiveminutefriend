package com.p.fiveminutefriend

import android.content.Context

class LoginPreferences(context: Context) {

    val PREFERENCE_NAME = "loginPreferences"
    val PREFERENCE_EMAIL = "email"
    val PREFERENCE_PASSWORD = "password"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getEmail() : String {
        return preference.getString(PREFERENCE_EMAIL, "")
    }

    fun setEmail(newEmail: String) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_EMAIL, newEmail)
        editor.apply()
    }

    fun getPassword() : String {
        return preference.getString(PREFERENCE_PASSWORD, "")
    }

    fun setPassword(newPassword: String) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_PASSWORD, newPassword)
        editor.apply()
    }

}