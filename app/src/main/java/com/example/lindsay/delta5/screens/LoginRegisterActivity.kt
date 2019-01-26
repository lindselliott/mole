package com.example.lindsay.delta5.screens

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.lindsay.delta5.R

import kotlinx.android.synthetic.main.activity_login_register.*

class LoginRegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
        setSupportActionBar(toolbar) // toolbar is the id of the appBar in the layout
    }

}
