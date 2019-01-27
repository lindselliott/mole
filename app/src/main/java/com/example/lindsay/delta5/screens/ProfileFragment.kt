package com.example.lindsay.delta5.screens

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.lindsay.delta5.Application
import com.example.lindsay.delta5.entities.User
import com.example.lindsay.delta5.models.UserModel
import kotlinx.android.synthetic.main.fragment_profile.*

import com.example.lindsay.delta5.R


class ProfileFragment : Fragment() {
    private lateinit var application: Application
    lateinit var user: User

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var mainActivity = activity as MainActivity
        user = (mainActivity.application as Application).user!!

        setAllFields()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onDetach() {
        super.onDetach()
        saveUser()
    }

    private fun setAllFields() {
        //Age
        age_text.setText("${user.age}")

        // Sex
        sex_spinner.adapter = ArrayAdapter(
                activity,
                R.layout.sex_spinner_item,
                resources.getStringArray(R.array.sex_type)
        )
    }

    private fun saveUser(): Boolean {
        val age = if (age_text.text.toString().isBlank()) "" else  age_text.text.toString()
        val sex = sex_spinner.selectedItemPosition


        val userToSave = User(
                "1",
                age.toInt(),
                sex
        )

        return UserModel.saveUser(application.realm, userToSave)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                ProfileFragment()

    }
}
