package com.example.lindsay.delta5.screens

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import com.example.lindsay.delta5.Application
import com.example.lindsay.delta5.R

import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private lateinit var application: Application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        application = getApplication() as Application // Grab the application and cast it to our application class

        if(application.user == null) {
            Log.d("Delta", "User does not exist")
            switchToLoginRegister()
        } else {
            Log.d("Delta", "User exists")
            switchToMainApp()
        }
    }

    private fun switchToMainApp() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun switchToLoginRegister() {
        startActivity(Intent(this, LoginRegisterActivity::class.java))
    }

}
