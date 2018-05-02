package com.p.fiveminutefriend


import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import kotlinx.android.synthetic.main.fragment_register.*


/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        button_create_account_register.setOnClickListener({
            createUser(edit_email_register.text.toString().trim(),
                    edit_first_name_register.text.toString().trim(),
                    edit_last_name_register.text.toString().trim(),
                    edit_email_register.text.toString().trim(),
                    edit_password_register.text.toString().trim(),
                    text_select_age_register.text.toString().toInt(),
                    text_gender_register.text.toString().trim(),
                    text_language_register.text.toString().trim())
        })

        val genderValues = arrayOf("Male", "Female", "Other")
        val genderPicker = NumberPicker(activity)

        genderPicker.minValue = 0
        genderPicker.maxValue = genderValues.size - 1
        genderPicker.displayedValues = genderValues
        genderPicker.wrapSelectorWheel = true

        return inflater!!.inflate(R.layout.fragment_register, container, false)
    }

    fun createUser(firstName: String,
                   lastName: String,
                   username: String,
                   email: String,
                   password: String,
                   age: Int,
                   gender: String,
                   language: String) {


    }

}
